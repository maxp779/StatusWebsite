package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class contains the commands the client will send, e.g
 * "login" and also a list of error codes.
 * 
 * It also contains a number of methods involved in setting up these
 * values so they can be easily marshalled into a json.
 *
 * @author max
 */
public class ServerApi
{
    private static EnumSet<Request> requestEnumSet = EnumSet.allOf(Request.class);
    private static EnumSet<ErrorCodes> errorCodesEnumSet = EnumSet.allOf(ErrorCodes.class);

    private static List<Request> REQUESTS_API_LIST;
    private static Map<String, String> REQUESTS_API_MAP_STRING;
    private static Map<ErrorCodes, Integer> ERROR_CODES_MAP;
    private static Map<String, String> ERROR_CODES_MAP_STRING;

    public static void setupServerAPI()
    {
        REQUESTS_API_LIST = setupREQUESTS_API_LIST();
        REQUESTS_API_MAP_STRING = setupREQUESTS_API_MAP_STRING(REQUESTS_API_LIST);
        ERROR_CODES_MAP = setupERROR_CODES_MAP();
        ERROR_CODES_MAP_STRING = setupERROR_CODES_MAP_STRING(ERROR_CODES_MAP);
    }

    private static List<Request> setupREQUESTS_API_LIST()
    {
        List<Request> outputList = new ArrayList<>();
        Iterator iterator = requestEnumSet.iterator();
        while (iterator.hasNext())
        {
            outputList.add((Request) iterator.next());
        }
        return outputList;
    }

    private static Map<ErrorCodes, Integer> setupERROR_CODES_MAP()
    {
        Map<ErrorCodes, Integer> outputMap = new HashMap<>();
        int errorNumber = 1; //0 would mean everything is fine so we start at 1
        for(ErrorCodes code : errorCodesEnumSet)
        {
            //EnumSet docs promises a natural ordering of the enums
            //so errorNumber should always be attached to the same error
            outputMap.put(code, errorNumber);
            errorNumber++;

        }
        return outputMap;
    }

    /**
     * Returns a map with only strings, useful for when JSON is needed.
     * The error number is changed to the key and the errorCode is repurposed as the value
     * as the client will have no access to Enum class ErrorCode. So searching for an error by number
     * will be easier.
     * @param errorCodesMap
     * @return Map<String,String> version of ERROR_CODES_MAP
     */
    private static Map<String, String> setupERROR_CODES_MAP_STRING(Map<ErrorCodes, Integer> errorCodesMap)
    {
        Map<String, String> outputMap = new HashMap<>();
        Iterator iterator = errorCodesMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry pair = (Map.Entry) iterator.next();
            outputMap.put(pair.getValue().toString(), pair.getKey().toString());
        }
        return outputMap;
    }

    /**
     * Returns a map with only strings, useful for when JSON is needed.
     *
     * @param serverAPIMap
     * @return Map<String,String> version of REQUESTS_API_MAP
     */
    private static Map<String, String> setupREQUESTS_API_MAP_STRING(List<Request> requestsAPIMap)
    {
        Map<String, String> outputMap = new HashMap<>();
        for (Request aRequest : requestsAPIMap)
        {
            outputMap.put(aRequest.toString(), "/" + aRequest.toString());
        }
        return outputMap;
    }

    public static Map<String, String> getREQUESTS_API_MAP_STRING()
    {
        return Collections.unmodifiableMap(REQUESTS_API_MAP_STRING);
    }
    public static Integer getErrorCodeInteger(ErrorCodes code)
    {
        return ERROR_CODES_MAP.get(code);
    }

    public static Map<String, String> getERROR_CODES_MAP_STRING()
    {
        return Collections.unmodifiableMap(ERROR_CODES_MAP_STRING);
    }
}
