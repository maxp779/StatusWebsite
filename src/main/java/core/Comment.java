package core;

import java.time.LocalDateTime;

/**
 * A class representing a Comment instance
 * @author max
 */
public class Comment
{
    String userId;
    String eventId;
    String commentId;
    String commentText;
    long postTimeUnix;
    LocalDateTime postTimestamp;

    public long getPostTimeUnix()
    {
        return postTimeUnix;
    }

    public void setPostTimeUnix(long postTimeUnix)
    {
        this.postTimeUnix = postTimeUnix;
    }

    public LocalDateTime getPostTimestamp()
    {
        return postTimestamp;
    }

    public void setPostTimestamp(LocalDateTime postTimestamp)
    {
        this.postTimestamp = postTimestamp;
    }

    
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

    public String getCommentId()
    {
        return commentId;
    }

    public void setCommentId(String commentId)
    {
        this.commentId = commentId;
    }

    public String getCommentText()
    {
        return commentText;
    }

    public void setCommentText(String commentText)
    {
        this.commentText = commentText;
    }
    
    @Override
    public String toString()
    {
        return "[userId:" + this.userId 
                + System.lineSeparator()
                + " commentId: " + this.commentId 
                + System.lineSeparator()
                + " eventId:" + this.eventId 
                + System.lineSeparator()
                + " commentText:" + this.commentText + "]";
    }
    
}
