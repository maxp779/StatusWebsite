package configurationmodel;

import java.util.Map;

/**
 * Currently not used, will be used when user info is fetched from file rather than hardcoded
 * @author max
 */
public class Users
{
    Map<String, String> Administrators;

    public Map<String, String> getAdministrators()
    {
        return Administrators;
    }

    public void setAdministrators(Map<String, String> Administrators)
    {
        this.Administrators = Administrators;
    }
}