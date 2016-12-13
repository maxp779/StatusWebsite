package core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains important values all in one location
 *
 * Some of this is currently hardcoded such a login credentials
 * and database connection string but in future will be moved out of here and
 * fetched from config.json file
 * 
 * @author max
 */
public class GlobalValues
{
    //login credentials
    private static final Map<String, String> USERS;
    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("maxpower", "123");
        aMap.put("testuser", "123");
        USERS = Collections.unmodifiableMap(aMap);
    }
    
    //urls
    private static final String LOGIN_PAGE_URL = "/login.html";
    private static final String ADMIN_PAGE_URL = "/adminPage.html";
    private static final String ADMIN_EVENT_PAGE_URL = "/adminEventPage.html";
    private static final String EVENT_PAGE_URL = "/eventPage.html";
    private static final String ERROR_PAGE_URL = "/error.html";
    private static final String UNRESOLVED_EVENTS_PAGE_URL = "/unresolvedEvents.html";
    private static final String RESOLVED_EVENTS_PAGE_URL = "/resolvedEvents.html";

    //login page request, this is where the user is redirected to when attempting to access unauthorized resources
    private static final Request LOGIN_PAGE_REQUEST = Request.getloginpage;
    private static final Request UNRESOLVED_EVENTS_PAGE = Request.getunresolvedeventspage;
    private static final Request RSS_REQUEST = Request.rss;
       
    //misc values
    private static final int SESSION_TIMEOUT_VALUE = 3600; //0 or less will never timeout, this value is in seconds   

    //database values
    private static final String DATABASE_URL = "server=127.0.0.1; uid=status_website_user; pwd=c86addf8-849d-4603-ba63-baa5e1f2b5c9; database=status_website_ef;";

    //requests which require authentication i.e user needs to be logged in to do this stuff
    private static final String[] AUTH_RESOURCES =
    {
        Request.addevent.toString(),
        Request.updateevent.toString(),
        Request.seteventresolved.toString(),
        Request.seteventunresolved.toString(),        
        Request.deleteevent.toString(),
        Request.addcomment.toString(),
        Request.updatecomment.toString(),
        Request.deletecomment.toString(),
        Request.getadminpage.toString(),
        "adminPage.html",
        "adminEventPage.html"
    };

    public static String getEVENT_PAGE_URL()
    {
        return EVENT_PAGE_URL;
    } 

    public static String getADMIN_EVENT_PAGE_URL()
    {
        return ADMIN_EVENT_PAGE_URL;
    }

    public static Request getUNRESOLVED_EVENTS_PAGE()
    {
        return UNRESOLVED_EVENTS_PAGE;
    } 

    public static Request getRSS_REQUEST()
    {
        return RSS_REQUEST;
    }
    
    public static String getERROR_PAGE_URL()
    {
        return ERROR_PAGE_URL;
    }
    
    public static Map<String, String> getUSERS() {
        return USERS;
    }

    public static Request getLOGIN_PAGE_REQUEST() {
        return LOGIN_PAGE_REQUEST;
    }

    public static int getSESSION_TIMEOUT_VALUE() {
        return SESSION_TIMEOUT_VALUE;
    }

    public static String getDATABASE_URL() {
        return DATABASE_URL;
    }

    public static String[] getAUTH_RESOURCES() {
        return AUTH_RESOURCES;
    }

    public static String getADMIN_PAGE_URL() {
        return ADMIN_PAGE_URL;
    }

    public static String getUNRESOLVED_EVENTS_PAGE_URL() {
        return UNRESOLVED_EVENTS_PAGE_URL;
    }

    public static String getRESOLVED_EVENTS_PAGE_URL() {
        return RESOLVED_EVENTS_PAGE_URL;
    }

    public static String getLOGIN_PAGE_URL() {
        return LOGIN_PAGE_URL;
    }   
}