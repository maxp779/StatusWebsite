package database;

import com.sun.syndication.io.FeedException;
import database.databasemodels.Comment;
import core.ErrorCodes;
import database.databasemodels.Event;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rss.UpdateRssFeed;

/**
 * This class contains all methods for accessing the database, most of these have
 * self explanatory names.
 * 
 * @author max
 */
public class DatabaseAccess
{
    private static final Logger log = LoggerFactory.getLogger(DatabaseAccess.class);

    public static boolean addComment(Comment aComment)
    {
        log.trace("addComment()");
        String addCommentSql = "INSERT INTO comments (comment_id, event_id, user_id, comment_text, post_time_unix, post_timestamp) VALUES (?,?,?,?,?,?)";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement addCommentStatement = databaseConnection.prepareStatement(addCommentSql);)
        {
            addCommentStatement.setString(1, aComment.getCommentId());
            addCommentStatement.setString(2, aComment.getEventId());
            addCommentStatement.setString(3, aComment.getUserId());
            addCommentStatement.setString(4, aComment.getCommentText());

            ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
            addCommentStatement.setLong(5, nowUTC.toEpochSecond());
            
            Timestamp timestamp = Timestamp.valueOf(nowUTC.toLocalDateTime());
            addCommentStatement.setTimestamp(6, timestamp);

            queryResult = addCommentStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        boolean updateLinkedEvent = setEventLastUpdatedTime(aComment.getEventId());
        return (queryResult != 0) && updateLinkedEvent;
    }

    /**
     * This method is called when an events last updated time needs to be
     * updated.
     *
     * If for example a new comment is posted to an event then the event is
     * considered to have been updated.
     *
     * @param eventId the event to be updated
     * @return boolean indicating success
     */
    private static boolean setEventLastUpdatedTime(String eventId)
    {
        log.trace("setEventLastUpdatedTime()");
        String setEventLastUpdatedTimeSql = "UPDATE events SET last_updated_unix=?, last_updated_timestamp=? WHERE event_id=?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement setEventLastUpdatedTimeStatement = databaseConnection.prepareStatement(setEventLastUpdatedTimeSql);)
        {
            ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
            setEventLastUpdatedTimeStatement.setLong(1, nowUTC.toEpochSecond());

            Timestamp timestamp = Timestamp.valueOf(nowUTC.toLocalDateTime());
            setEventLastUpdatedTimeStatement.setTimestamp(2, timestamp);

            setEventLastUpdatedTimeStatement.setString(3, eventId);

            queryResult = setEventLastUpdatedTimeStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return queryResult != 0;
    }

    public static boolean addEvent(Event anEvent)
    {
        log.trace("addEvent()");
        String addEventSql = "INSERT INTO events (event_id, user_id, event_title, event_text, event_status, is_active, last_updated_unix, "
                + "last_updated_timestamp, start_time_unix, start_timestamp) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement addEventStatement = databaseConnection.prepareStatement(addEventSql);)
        {
            addEventStatement.setString(1, anEvent.getEventId());
            addEventStatement.setString(2, anEvent.getUserId());
            addEventStatement.setString(3, anEvent.getEventTitle());
            addEventStatement.setString(4, anEvent.getEventText());
            addEventStatement.setString(5, anEvent.getEventStatus());
            addEventStatement.setBoolean(6, anEvent.isActive());

            ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
            Timestamp timestamp = Timestamp.valueOf(nowUTC.toLocalDateTime());

            addEventStatement.setLong(7, nowUTC.toEpochSecond());
            addEventStatement.setTimestamp(8, timestamp);
            addEventStatement.setLong(9, nowUTC.toEpochSecond());
            addEventStatement.setTimestamp(10, timestamp);

            queryResult = addEventStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        UpdateRssFeed.updateFeed();
        return queryResult != 0;
    }

    public static boolean deleteComment(Comment aComment)
    {
        log.trace("deleteComment()");
        String deleteCommentSql = "DELETE FROM comments WHERE comment_id = ?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement deleteCommentStatement = databaseConnection.prepareStatement(deleteCommentSql);)
        {
            deleteCommentStatement.setString(1, aComment.getCommentId());
            queryResult = deleteCommentStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        boolean updateLinkedEvent = setEventLastUpdatedTime(aComment.getEventId());
        return (queryResult != 0) && updateLinkedEvent;
    }

    public static boolean deleteEvent(Event anEvent)
    {
        log.trace("deleteEvent()");
        String deleteEventSql = "DELETE FROM events WHERE event_id = ?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement deleteEventStatement = databaseConnection.prepareStatement(deleteEventSql);)
        {
            deleteEventStatement.setString(1, anEvent.getEventId());
            queryResult = deleteEventStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return queryResult != 0;
    }

    public static boolean updateComment(Comment aComment)
    {
        log.trace("updateComment()");
        String updateCommentSql = "UPDATE comments SET comment_text = ? WHERE comment_id = ?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement updateCommentStatement = databaseConnection.prepareStatement(updateCommentSql);)
        {
            updateCommentStatement.setString(1, aComment.getCommentText());
            updateCommentStatement.setString(2, aComment.getCommentId());

            queryResult = updateCommentStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        boolean updateLinkedEvent = setEventLastUpdatedTime(aComment.getEventId());
        return (queryResult != 0) && updateLinkedEvent;
    }

    public static boolean setEventResolved(String eventId)
    {
        log.trace("setEventResolved()");
        String setEventResolvedSql = "UPDATE events SET is_active=?, resolved_time_unix=?, resolved_timestamp=? WHERE event_id = ?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement setEventResolvedStatement = databaseConnection.prepareStatement(setEventResolvedSql);)
        {
            setEventResolvedStatement.setBoolean(1, false);

            ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
            setEventResolvedStatement.setLong(2, nowUTC.toEpochSecond());

            Timestamp timestamp = Timestamp.valueOf(nowUTC.toLocalDateTime());
            setEventResolvedStatement.setTimestamp(3, timestamp);

            setEventResolvedStatement.setString(4, eventId);

            queryResult = setEventResolvedStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        UpdateRssFeed.updateFeed();
        return queryResult != 0;
    }

    public static boolean setEventActive(String eventId)
    {
        log.trace("setEventActive()");
        String setEventActiveSql = "UPDATE events SET is_active=?, resolved_time_unix=?, resolved_timestamp=? WHERE event_id = ?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement setEventActiveStatement = databaseConnection.prepareStatement(setEventActiveSql);)
        {
            setEventActiveStatement.setBoolean(1, false);
            setEventActiveStatement.setNull(2, java.sql.Types.BIGINT);
            setEventActiveStatement.setNull(3, java.sql.Types.TIMESTAMP);
            setEventActiveStatement.setString(4, eventId);

            queryResult = setEventActiveStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return queryResult != 0;
    }

    public static boolean updateEvent(Event anEvent)
    {
        log.trace("updateEvent()");
        String updateEventSql = "UPDATE events "
                + "SET event_title=?, event_text=?, event_status=?, last_updated_unix=?, last_updated_timestamp=?"
                + "WHERE event_id = ?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement updateEventStatement = databaseConnection.prepareStatement(updateEventSql);)
        {
            updateEventStatement.setString(1, anEvent.getEventTitle());
            updateEventStatement.setString(2, anEvent.getEventText());
            updateEventStatement.setString(3, anEvent.getEventStatus());
            updateEventStatement.setLong(4, anEvent.getLastUpdatedUnix());
            updateEventStatement.setTimestamp(5, Timestamp.valueOf(anEvent.getLastUpdatedTimestamp()));
            updateEventStatement.setString(6, anEvent.getEventId());

            queryResult = updateEventStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return queryResult != 0;
    }

//    public static boolean updateEventText(Event anEvent)
//    {
//        log.trace("updateEventText()");
//        String updateEventTextSql = "UPDATE events SET event_text = ? WHERE event_id = ?";
//
//        int queryResult = 0;
//        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
//                PreparedStatement updateEventTextStatement = databaseConnection.prepareStatement(updateEventTextSql);)
//        {
//            updateEventTextStatement.setString(1, anEvent.getEventText());
//            updateEventTextStatement.setString(2, anEvent.getEventId());
//
//            queryResult = updateEventTextStatement.executeUpdate();
//
//        } catch (SQLException ex)
//        {
//            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
//        }
//        return queryResult != 0;
//    }
//
//    public static boolean updateEventTitle(Event anEvent)
//    {
//        log.trace("updateEventTitle()");
//        String updateEventTitleSql = "UPDATE events SET event_title = ? WHERE event_id = ?";
//
//        int queryResult = 0;
//        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
//                PreparedStatement updateEventTitleStatement = databaseConnection.prepareStatement(updateEventTitleSql);)
//        {
//            updateEventTitleStatement.setString(1, anEvent.getTitle());
//            updateEventTitleStatement.setString(2, anEvent.getEventId());
//
//            queryResult = updateEventTitleStatement.executeUpdate();
//
//        } catch (SQLException ex)
//        {
//            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
//        }
//        return queryResult != 0;
//    }
    public static List getAllEvents()
    {
        log.trace("getAllEvents()");
        String getAllEventsSql = "SELECT * FROM events";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getAllEventsStatement = databaseConnection.prepareStatement(getAllEventsSql);)
        {
            try (ResultSet resultSet = getAllEventsStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    resultSetList = DatabaseHelpers.convertResultSetToList(resultSet);
                }
            }

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return resultSetList;
    }

//    public static List<Event> getEvent(String eventId)
//    {
//        log.trace("getEvent()");
//        String getEventSql = "SELECT * FROM events WHERE event_id = ?";
//
//        List<Event> eventList = null;
//        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
//                PreparedStatement getEventStatement = databaseConnection.prepareStatement(getEventSql);)
//        {
//            getEventStatement.setString(1, eventId);
//            try (ResultSet resultSet = getEventStatement.executeQuery())
//            {
//                if (resultSet.next())
//                {
//                    eventList = DatabaseHelpers.convertResultSetToEvent(resultSet);
//                }
//            }
//
//        } catch (SQLException ex)
//        {
//            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
//        }
//        return eventList;
//    }
    
    
    /**
     * Gets all events which are not yet resolved.
     * @return a List of unsresolved events
     */
    public static List getActiveEvents()
    {
        log.trace("getActiveEvents()");
        String getActiveEventsSql = "SELECT * FROM events WHERE is_active = ?";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getActiveEventsStatement = databaseConnection.prepareStatement(getActiveEventsSql);)
        {
            getActiveEventsStatement.setBoolean(1, true);
            try (ResultSet resultSet = getActiveEventsStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    resultSetList = DatabaseHelpers.convertResultSetToList(resultSet);
                }
            }

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return resultSetList;
    }

    public static List getResolvedEvents(long fromDate, long toDate)
    {
        log.trace("getResolvedEvents()");
        //select all events 
        //where is_active is false
        //AND where resolvedTimeUnix is less than the fromDate 
        //AND where resolvedTimeUnix is greater than the toDate
        String getResolvedEventsSql = "SELECT * FROM events WHERE is_active = ? AND resolved_time_unix <=? AND resolved_time_unix >=?";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getResolvedEventsStatement = databaseConnection.prepareStatement(getResolvedEventsSql);)
        {
            getResolvedEventsStatement.setBoolean(1, false);
            
            getResolvedEventsStatement.setLong(2, fromDate);          
            getResolvedEventsStatement.setLong(3, toDate);
          
            try (ResultSet resultSet = getResolvedEventsStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    resultSetList = DatabaseHelpers.convertResultSetToList(resultSet);
                }
            }

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return resultSetList;
    }

    public static List getAllComments()
    {
        log.trace("getAllComments()");
        String getAllCommentsSql = "SELECT * FROM comments";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getAllCommentsStatement = databaseConnection.prepareStatement(getAllCommentsSql);)
        {
            try (ResultSet resultSet = getAllCommentsStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    resultSetList = DatabaseHelpers.convertResultSetToList(resultSet);
                }
            }

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return resultSetList;
    }

    public static List getEventComments(Event anEvent)
    {
        log.trace("getEventComments()");
        String getEventCommentsSql = "SELECT * FROM comments WHERE event_id = ?";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getEventCommentsStatement = databaseConnection.prepareStatement(getEventCommentsSql);)
        {
            getEventCommentsStatement.setString(1, anEvent.getEventId());
            try (ResultSet resultSet = getEventCommentsStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    resultSetList = DatabaseHelpers.convertResultSetToList(resultSet);
                }
            }

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return resultSetList;
    }
    
    /**
     * Gets all events which are not yet resolved.
     * @return a List of unsresolved events
     */
    public static List<Event> getActiveEventsForRss()
    {
        log.trace("getActiveEventsForRss()");
        String getActiveEventsSql = "SELECT * FROM events WHERE is_active = ?";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getActiveEventsStatement = databaseConnection.prepareStatement(getActiveEventsSql);)
        {
            getActiveEventsStatement.setBoolean(1, true);
            try (ResultSet resultSet = getActiveEventsStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    resultSetList = DatabaseHelpers.convertResultSetToEvent(resultSet);
                }
            }

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return resultSetList;
    }
}
