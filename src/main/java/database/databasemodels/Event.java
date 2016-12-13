package database.databasemodels;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.function.ToLongFunction;

/**
 * A class representing an instance of an Event
 * 
 * @author max
 */
public class Event implements Comparable<Event>
{
    private String userId;
    private String eventId;
    private String eventText;
    private boolean isResolved;
    private String eventStatus;
    private String eventTitle;
    private long startTimeUnix;
    private LocalDateTime startTimestamp;
    private long lastUpdatedTimeUnix;
    private LocalDateTime lastUpdatedTimestamp;
    private long resolvedTimeUnix;
    private LocalDateTime resolvedTimestamp;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public String getEventText()
    {
        return eventText;
    }

    public void setEventText(String eventText)
    {
        this.eventText = eventText;
    }

    public boolean isResolved()
    {
        return isResolved;
    }

    public void setIsResolved(boolean isResolved)
    {
        this.isResolved = isResolved;
    }

    public String getEventStatus()
    {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus)
    {
        this.eventStatus = eventStatus;
    }

    public String getEventTitle()
    {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle)
    {
        this.eventTitle = eventTitle;
    }

    public Long getStartTimeUnix()
    {
        return startTimeUnix;
    }

    public void setStartTimeUnix(Long startTimeUnix)
    {
        this.startTimeUnix = startTimeUnix;
    }

    public LocalDateTime getStartTimestamp()
    {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp)
    {
        this.startTimestamp = startTimestamp;
    }

    public Long getLastUpdatedUnix()
    {
        return lastUpdatedTimeUnix;
    }

    public void setLastUpdatedUnix(Long lastUpdatedUnix)
    {
        this.lastUpdatedTimeUnix = lastUpdatedUnix;
    }

    public LocalDateTime getLastUpdatedTimestamp()
    {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(LocalDateTime lastUpdatedTimestamp)
    {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public Long getResolvedTimeUnix()
    {
        return resolvedTimeUnix;
    }

    public void setResolvedTimeUnix(Long resolvedTimeUnix)
    {
        this.resolvedTimeUnix = resolvedTimeUnix;
    }

    public LocalDateTime getResolvedTimestamp()
    {
        return resolvedTimestamp;
    }

    public void setResolvedTimestamp(LocalDateTime resolvedTimestamp)
    {
        this.resolvedTimestamp = resolvedTimestamp;
    }
    
    

    @Override
    public String toString()
    {
        return "[userId:" + this.userId
                + System.lineSeparator()
                + " eventId: " + this.eventId
                + System.lineSeparator()
                + " title:" + this.eventTitle
                + System.lineSeparator()
                + " eventText:" + this.eventText
                + System.lineSeparator()
                + " status:" + this.eventStatus
                + System.lineSeparator()
                + " isResolved:" + this.isResolved + "]";
    }

    @Override
    public int compareTo(Event anEvent)
    {
        int isGreater = -1;
        if(this.startTimeUnix > anEvent.getStartTimeUnix())
        {
            isGreater = 1;
        }
        else if(this.startTimeUnix == anEvent.getStartTimeUnix())
        {
            isGreater = 0;
        }    
        return isGreater;
    }


}
