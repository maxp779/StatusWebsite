package core;

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
    boolean resolved;
    String status;
    String title;
    Long startTimeUnix;
    LocalDateTime startTimestamp;
    Long lastUpdatedUnix;
    LocalDateTime lastUpdatedTimestamp;
    Long resolvedTimeUnix;
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
        return resolved;
    }

    public void setResolved(boolean resolved)
    {
        this.resolved = resolved;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
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
        return lastUpdatedUnix;
    }

    public void setLastUpdatedUnix(Long lastUpdatedUnix)
    {
        this.lastUpdatedUnix = lastUpdatedUnix;
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
                + " title:" + this.title
                + System.lineSeparator()
                + " eventText:" + this.eventText
                + System.lineSeparator()
                + " status:" + this.status
                + System.lineSeparator()
                + " isResolved:" + this.resolved + "]";
    }

}
