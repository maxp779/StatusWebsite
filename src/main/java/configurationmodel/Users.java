package configurationmodel;

import java.util.Map;

/**
 *
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