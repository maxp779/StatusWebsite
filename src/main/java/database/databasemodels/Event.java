package database.databasemodels;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * A class representing an instance of an Event
 * 
 * @author max
 */
public class Event
{
    String userId;
    String eventId;
    String eventText;
    boolean isResolved;
    String eventStatus;
    String eventTitle;
    long startTimeUnix;
    LocalDateTime startTimestamp;
    long lastUpdatedTimeUnix;
    LocalDateTime lastUpdatedTimestamp;
    long resolvedTimeUnix;
    LocalDateTime resolvedTimestamp;

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

}
