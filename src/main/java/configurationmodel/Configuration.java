/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationmodel;

import java.util.Map;

/**
 * Currently not used, will be used when config info is fetched from a file rather than hardcoded
 * 
 * @author max
 */
public class Configuration
{
    Map<String, String> ConnectionStrings;
    Users users;
    Map<String, String> StatusCodes;
    
    public String getDefaultConnectionString()
    {
        return ConnectionStrings.get("DefaultConnectionString");
    }

    public Map<String, String> getConnectionStrings()
    {
        return ConnectionStrings;
    }

    public void setConnectionStrings(Map<String, String> ConnectionStrings)
    {
        this.ConnectionStrings = ConnectionStrings;
    }

    public Users getUsers()
    {
        return users;
    }

    public void setUsers(Users users)
    {
        this.users = users;
    }

    public Map<String, String> getStatusCodes()
    {
        return StatusCodes;
    }

    public void setStatusCodes(Map<String, String> StatusCodes)
    {
        this.StatusCodes = StatusCodes;
    }
    
}
