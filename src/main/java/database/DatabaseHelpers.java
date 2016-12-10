package database;

import core.Event;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains various helper methods for the database. For example
 * formatting the output of a resultset into a Java object of some type, or converting
 * the database's column names underscore_case into camelCase.
 * @author max
 */
public class DatabaseHelpers
{

    private static final Logger log = LoggerFactory.getLogger(DatabaseUtils.class);

    protected static List convertResultSetToList(ResultSet aResultSet) throws SQLException
    {
        log.trace("convertResultSetToList()");
        log.debug(aResultSet.toString());
        //turn resultset into arraylist with maps
        //each Map represents a single row
        ResultSetMetaData resultSetMetaData = aResultSet.getMetaData();
        List mainList = new ArrayList<>();
        int columnCount = resultSetMetaData.getColumnCount();
        while (aResultSet.next())
        {
            int currentColumn = 1;
            Map currentRecord = new HashMap();

            while (currentColumn <= columnCount)
            {
                String columnName = resultSetMetaData.getColumnName(currentColumn);
                columnName = convertUnderscoreToCamelCase(columnName);
                String columnValue = aResultSet.getString(currentColumn);
                currentRecord.put(columnName, columnValue);
                currentColumn++;
            }
            mainList.add(currentRecord);
        }
        log.debug(mainList.toString());
        return mainList;
    }

    protected static String convertUnderscoreToCamelCase(String input)
    {
        log.trace("convertUnderscoreToCamelCase()");

        char[] inputArray = input.toCharArray();
        StringBuilder output = new StringBuilder();
        boolean capitolizeNextChar = false;
        for (int count = 0; count < inputArray.length; count++)
        {
            char currentChar = inputArray[count];

            if (currentChar != '_' && capitolizeNextChar)
            {
                currentChar = Character.toUpperCase(currentChar);
                output.append(currentChar);
                capitolizeNextChar = false;
            } else if (currentChar == '_')
            {
                capitolizeNextChar = true;
            } else
            {
                output.append(currentChar);
            }
        }
        return output.toString();
    }

    /**
     * This method turns a resultset object with a single row into an Event object.
     * Is it hard coded with the current database column names so any changes there will
     * break this method.
     * @param aResultSet
     * @return an Event object with the values of the resultset
     * @throws SQLException 
     */
    protected static Event convertResultSetToEvent(ResultSet aResultSet) throws SQLException
    {
        log.trace("convertResultSetToEvent()");
        log.debug(aResultSet.toString());
        ResultSetMetaData resultSetMetaData = aResultSet.getMetaData();
        Event anEvent = null;
        int columnCount = resultSetMetaData.getColumnCount();
        if (aResultSet.next())
        {
            anEvent = new Event();
            int currentColumn = 1;
            while (currentColumn <= columnCount)
            {
                String columnName = resultSetMetaData.getColumnName(currentColumn);

                switch (columnName)
                {
                    case "event_id":
                        {
                            String columnValue = aResultSet.getString(currentColumn);
                            anEvent.setEventId(columnValue);
                            break;
                        }
                    case "event_text":
                        {
                            String columnValue = aResultSet.getString(currentColumn);
                            anEvent.setEventText(columnValue);
                            break;
                        }
                    case "start_time_unix":
                        {
                            Long columnValue = aResultSet.getLong(currentColumn);
                            anEvent.setStartTimeUnix(columnValue);
                            break;
                        }
                    case "start_timestamp":
                        {
                            Timestamp columnValue = aResultSet.getTimestamp(currentColumn);
                            anEvent.setStartTimestamp(columnValue.toLocalDateTime());
                            break;
                        }
                    case "event_status":
                        {
                            String columnValue = aResultSet.getString(currentColumn);
                            anEvent.setStatus(columnValue);
                            break;
                        }
                    case "last_updated_unix":
                        {
                            Long columnValue = aResultSet.getLong(currentColumn);
                            anEvent.setLastUpdatedUnix(columnValue);
                            break;
                        }
                    case "last_updated_timestamp":
                        {
                            Timestamp columnValue = aResultSet.getTimestamp(currentColumn);
                            anEvent.setLastUpdatedTimestamp(columnValue.toLocalDateTime());
                            break;
                        }
                    case "is_resolved":
                        {
                            boolean columnValue = aResultSet.getBoolean(currentColumn);
                            anEvent.setResolved(columnValue);
                            break;
                        }
                    case "resolved_time_unix":
                        {
                            Long columnValue = aResultSet.getLong(currentColumn);
                            anEvent.setResolvedTimeUnix(columnValue);
                            break;
                        }
                    case "resolved_timestamp":
                        {
                            Timestamp columnValue = aResultSet.getTimestamp(currentColumn);
                            anEvent.setResolvedTimestamp(columnValue.toLocalDateTime());
                            break;
                        }
                    case "user_id":
                        {
                            String columnValue = aResultSet.getString(currentColumn);
                            anEvent.setUserId(columnValue);
                            break;
                        }
                    default:
                        break;
                }
            }
            currentColumn++;
        }
        return anEvent;
    }

}