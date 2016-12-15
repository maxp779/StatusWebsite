package configurationmodel;

import java.util.Map;

/**
 * Object to store usernames and passwords for administrators.
 * 
 * @author max
 */
public class Config
{
    private Map<String, String> Administrators;

    public Map<String, String> getAdministrators()
    {
        return Administrators;
    }

    public void setAdministrators(Map<String, String> Administrators)
    {
        this.Administrators = Administrators;
    }
}
