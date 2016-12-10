package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An object representing an instance of the current user. It is barebones
 * currently but can be expanded on if needed.
 * 
 * @author max
 */
public class UserObject
{
    private static final Logger log = LoggerFactory.getLogger(UserObject.class);
    private String username;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
    
    
}
