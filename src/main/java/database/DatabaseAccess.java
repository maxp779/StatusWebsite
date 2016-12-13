package database;

import database.databasemodels.Comment;
import core.ErrorCodes;
import database.databasemodels.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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
            addCommentStatement.setLong(5, aComment.getPostTimeUnix());          
            addCommentStatement.setTimestamp(6, Timestamp.valueOf(aComment.getPostTimestamp()));

            queryResult = addCommentStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return queryResult != 0;
    }

    /**
     * This method is called when an events last updated time needs to be
     * updated.
     *
     * If for example a comment is created/updated/deleted for an event then the event is
     * considered to have been updated and this method is called.
     *
     * @param eventId the event to be updated
     * @return boolean indicating success
     */
    public static boolean setEventLastUpdatedTime(String eventId, long currentUtcSecond, LocalDateTime currentUtcLocalDateTime)
    {
        log.trace("setEventLastUpdatedTime()");
        String setEventLastUpdatedTimeSql = "UPDATE events SET last_updated_time_unix=?, last_updated_timestamp=? WHERE event_id=?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement setEventLastUpdatedTimeStatement = databaseConnection.prepareStatement(setEventLastUpdatedTimeSql);)
        {
            setEventLastUpdatedTimeStatement.setLong(1, currentUtcSecond);
            setEventLastUpdatedTimeStatement.setTimestamp(2, Timestamp.valueOf(currentUtcLocalDateTime));

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
        String addEventSql = "INSERT INTO events (event_id, user_id, event_title, event_text, event_status, is_resolved, last_updated_time_unix, "
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
            addEventStatement.setBoolean(6, anEvent.isResolved());
            addEventStatement.setLong(7, anEvent.getLastUpdatedUnix());
            addEventStatement.setTimestamp(8, Timestamp.valueOf(anEvent.getLastUpdatedTimestamp()));           
            addEventStatement.setLong(9, anEvent.getStartTimeUnix());
            addEventStatement.setTimestamp(10, Timestamp.valueOf(anEvent.getStartTimestamp()));

            queryResult = addEventStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        //UpdateRssFeed.updateFeed();
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
        //boolean updateLinkedEvent = setEventLastUpdatedTime(aComment.getEventId());
        return queryResult != 0;
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
        return queryResult != 0;
    }

    public static boolean setEventResolved(Event anEvent)
    {
        log.trace("setEventResolved()");
        String setEventResolvedSql = "UPDATE events SET is_resolved=?, resolved_time_unix=?, resolved_timestamp=?, last_updated_time_unix=?, last_updated_timestamp=? WHERE event_id = ?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement setEventResolvedStatement = databaseConnection.prepareStatement(setEventResolvedSql);)
        {
            setEventResolvedStatement.setBoolean(1, true);
            setEventResolvedStatement.setLong(2, anEvent.getResolvedTimeUnix());
            setEventResolvedStatement.setTimestamp(3, Timestamp.valueOf(anEvent.getResolvedTimestamp()));
            setEventResolvedStatement.setLong(4, anEvent.getLastUpdatedUnix());
            setEventResolvedStatement.setTimestamp(5, Timestamp.valueOf(anEvent.getLastUpdatedTimestamp()));
            setEventResolvedStatement.setString(6, anEvent.getEventId());

            queryResult = setEventResolvedStatement.executeUpdate();

        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        //UpdateRssFeed.updateFeed();
        return queryResult != 0;
    }

    public static boolean setEventUnresolved(Event anEvent)
    {
        log.trace("setEventUnresolved()");
        String setEventUnresolvedSql = "UPDATE events SET is_resolved=?, resolved_time_unix=?, resolved_timestamp=?, last_updated_time_unix=?, last_updated_timestamp=? WHERE event_id = ?";

        int queryResult = 0;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement setEventUnresolvedStatement = databaseConnection.prepareStatement(setEventUnresolvedSql);)
        {
            setEventUnresolvedStatement.setBoolean(1, false);
            setEventUnresolvedStatement.setNull(2, java.sql.Types.BIGINT);
            setEventUnresolvedStatement.setNull(3, java.sql.Types.TIMESTAMP);
                        setEventUnresolvedStatement.setLong(4, anEvent.getLastUpdatedUnix());
            setEventUnresolvedStatement.setTimestamp(5, Timestamp.valueOf(anEvent.getLastUpdatedTimestamp()));
            setEventUnresolvedStatement.setString(6, anEvent.getEventId());

            queryResult = setEventUnresolvedStatement.executeUpdate();

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
                + "SET event_title=?, event_text=?, event_status=?, last_updated_time_unix=?, last_updated_timestamp=?,"
                + "start_time_unix=?, start_timestamp=? "
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
            updateEventStatement.setLong(6, anEvent.getStartTimeUnix());
            updateEventStatement.setTimestamp(7, Timestamp.valueOf(anEvent.getStartTimestamp()));      
            updateEventStatement.setString(8, anEvent.getEventId());

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
    public static List getEvents(long fromDate, long toDate)
    {
        log.trace("getEvents()");
        //select all events 
        //where start_time_unix is less than the fromDate 
        //AND where start_time_unix is greater than the toDate
        String getEventsSql = "SELECT * FROM events WHERE start_time_unix <=? AND start_time_unix >=?";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getEventsStatement = databaseConnection.prepareStatement(getEventsSql);)
        {            
            getEventsStatement.setLong(1, fromDate);          
            getEventsStatement.setLong(2, toDate);
          
            try (ResultSet resultSet = getEventsStatement.executeQuery())
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

    public static List<Event> getSingleEvent(String eventId)
    {
        log.trace("getSingleEvent()");
        String getSingleEventSql = "SELECT * FROM events WHERE event_id = ?";

        List<Event> eventList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getSingleEventStatement = databaseConnection.prepareStatement(getSingleEventSql);)
        {
            getSingleEventStatement.setString(1, eventId);
            try (ResultSet resultSet = getSingleEventStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    eventList = DatabaseHelpers.convertResultSetToEvent(resultSet);
                }
            }
        } catch (SQLException ex)
        {
            log.error(ErrorCodes.DATABASE_ERROR.toString(), ex);
        }
        return eventList;
    }
    
    
    /**
     * Gets all events which are not yet resolved.
     * @return a List of unsresolved events
     */
    public static List getUnresolvedEvents()
    {
        log.trace("getUnresolvedEvents()");
        String getUnresolvedEventsSql = "SELECT * FROM events WHERE is_resolved = ?";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getUnresolvedEventsStatement = databaseConnection.prepareStatement(getUnresolvedEventsSql);)
        {
            getUnresolvedEventsStatement.setBoolean(1, false);
            try (ResultSet resultSet = getUnresolvedEventsStatement.executeQuery())
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
        //where is_resolved is true
        //AND where resolvedTimeUnix is less than the fromDate 
        //AND where resolvedTimeUnix is greater than the toDate
        String getResolvedEventsSql = "SELECT * FROM events WHERE is_resolved = ? AND resolved_time_unix <=? AND resolved_time_unix >=?";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getResolvedEventsStatement = databaseConnection.prepareStatement(getResolvedEventsSql);)
        {
            getResolvedEventsStatement.setBoolean(1, true);   
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

    public static List getEventComments(String eventId)
    {
        log.trace("getEventComments()");
        String getEventCommentsSql = "SELECT * FROM comments WHERE event_id = ?";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getEventCommentsStatement = databaseConnection.prepareStatement(getEventCommentsSql);)
        {
            getEventCommentsStatement.setString(1, eventId);
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
    public static List<Event> getUnresolvedEventsForRss()
    {
        log.trace("getUnresolvedEventsForRss()");
        String getUnresolvedEventsSql = "SELECT * FROM events WHERE is_resolved = ?";

        List resultSetList = null;
        try (Connection databaseConnection = DatabaseUtils.getDatabaseConnection();
                PreparedStatement getUnresolvedEventsStatement = databaseConnection.prepareStatement(getUnresolvedEventsSql);)
        {
            getUnresolvedEventsStatement.setBoolean(1, false);        
            //getUnresolvedEventsStatement.setLong(2, DatabaseHelpers.getCurrentUtcSeconds());

            try (ResultSet resultSet = getUnresolvedEventsStatement.executeQuery())
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
