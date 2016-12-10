package core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Various useful methods for the servlet classes to use. E.g getting request data
 * in various formats.
 * 
 * @author max
 */
public class ServletUtils
{

    private static final Logger log = LoggerFactory.getLogger(ServletUtils.class);

    /**
     * This gets the request data which should be a string in JSON format then
     * returns it.
     *
     * @param request
     * @return a JSON string with the request data
     */
    public static String getPostRequestJson(HttpServletRequest request)
    {
        log.trace("getPOSTRequestJSONString()");

        BufferedReader reader = null;
        try
        {
            reader = request.getReader();
        } catch (IOException ex)
        {
            log.error("error getting reader from request object", ex);
        }
        StringBuilder buffer = new StringBuilder();
        String currentLine;

        try
        {
            //reader.readLine() is within the while head to avoid "null" being
            //appended on at the end, this happens if it is in the body
            while ((currentLine = reader.readLine()) != null)
            {
                buffer.append(currentLine);
            }
        } catch (IOException ex)
        {
            log.error("error using reader, possible null reader object", ex);
        }

        String jsonString = buffer.toString();
        log.debug(jsonString);
        return jsonString;
    }

    public static Map<String, String> convertJSONStringToMap(String aJsonString)
    {
        log.trace("convertJSONStringToMap()");
        log.debug("aJSONString:" + aJsonString);
        Gson gson = new Gson();
        Type stringStringMap = new TypeToken<LinkedHashMap<String, String>>()
        {
        }.getType();
        Map<String, String> outputMap = gson.fromJson(aJsonString, stringStringMap);
        log.debug(outputMap.toString());
        return outputMap;
    }

    public static String convertMapToJSONString(Map aMap)
    {
        log.trace("convertMapToJSONString()");
        Gson gson = new Gson();
        String aJsonString = gson.toJson(aMap);
        log.debug(aJsonString);
        return aJsonString;
    }

    /**
     * This method deals with values from an HTML form when jQuerys
     * serializeArray() method is used. The JSON String sent to the server will
     * be in the form: [{name=email, value=test@test.com}, {name=password,
     * value=testtest}] This method will extract the values and return a single
     * map like so: ["email":"test@test.com", "password":"testtest"]
     *
     * @param aJSONArray a string with JSON formatting
     * @return Map<String,String> containing the relevant values from an HTML
     * form
     */
    public static Map<String, String> convertJsonFormDataToMap(String aJSONArray)
    {
        log.trace("convertJSONFormDataToMap()");
        Gson gson = new Gson();
        Type arrayListMap = new TypeToken<ArrayList<Map>>()
        {
        }.getType();

        //list will be in the form
        //list=[{name=email, value=test@test.com}, {name=password, value=testtest}]
        List<Map> loginList = gson.fromJson(aJSONArray, arrayListMap);
        Map<String, String> output = new HashMap<>();

        for (Map<String, String> currentMap : loginList)
        {
            String currentKey = currentMap.get("name");
            String currentValue = currentMap.get("value");
            if (currentValue.equals(""))
            {
                currentValue = null;
            }
            output.put(currentKey, currentValue);
        }
        log.debug(output.toString());
        return output;
    }

    public static UserObject getCurrentUser(HttpServletRequest request)
    {
        return (UserObject) request.getSession().getAttribute("user");
    }
    
    public static Comment deserializeCommentJson(String aJsonString)
    {
        log.trace("deserializeCommentJson()");
        Gson gson = new Gson();
        Comment commentObject = gson.fromJson(aJsonString, Comment.class);
        return commentObject;
    }
        
    public static Event deserializeEventJson(String aJsonString)
    {
        log.trace("deserializeEventJson()");
        Gson gson = new Gson();
        Event eventObject = gson.fromJson(aJsonString, Event.class);
        return eventObject;
    }
}