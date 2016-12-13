package servlets.crud.helperclasses;

import com.google.gson.Gson;
import core.ErrorCodes;
import core.ServerApi;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standard object to contain data to be sent to the client, can be converted to
 * a json string easily.
 *
 * @author max
 */
public class StandardOutputObject
{

    private static final Logger log = LoggerFactory.getLogger(StandardOutputObject.class);
    private boolean success = false;
    private Integer errorCode = 0;
    private Object data = null;

    /**
     * Uses googles gson library to turn this object into a json string to be
     * sent to the client. The JSON has the format: 
     * { 
     * "success":true,
     * "errorCode":{"10":"Some error description"},
     * "data":{"data":someData} 
     * }
     *
     * @return A json String representation of this object
     */
    public String getJSONString()
    {
        log.trace("getJSONString()");
        Gson gson = new Gson();
        Map<String, Object> tempMap = new HashMap();
        tempMap.put("success", success);
        tempMap.put("errorCode", errorCode);
        tempMap.put("data", data);

        String JSONString = gson.toJson(tempMap);
        log.debug(JSONString);
        return JSONString;
    }

    /**
     * Lets the client know if the request was successful or not. For example
     * valid json may have been returned by the server but if success=false then
     * a database query may have failed for some reason.
     *
     * @param aBoolean was the request successful or not
     */
    public void setSuccess(boolean aBoolean)
    {
        success = aBoolean;
    }

    /**
     * Takes an ErrorCodes Enum argument and sets the errorCode parameter of this
     * object to the appropriate numeric code e.g ErrorCode.PASSWORD_TOO_SHORT
     * may be set to 10 ErrorCode.LOGIN_FAILED may be 11 etc
     *
     * @param code
     */
    public void setErrorCode(ErrorCodes code)
    {
        errorCode = ServerApi.getErrorCodeInteger(code);
    }

    /**
     * Takes any other output the client needs or may find useful.
     *
     * @param input an Object which is easily marshalled into json
     */
    public void setData(Object input)
    {
        data = input;
    }
    
    @Override
    public String toString()
    {
        return this.getJSONString();
    }
}
