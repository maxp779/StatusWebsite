package session;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.ErrorCodes;
import core.GlobalValues;
import core.ServletUtils;
import core.StandardOutputObject;
import core.UserObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Servlet handles user authentication, it is called when the user has
 * filled in and submitted their details.
 *
 * @author max
 */
@WebServlet(name = "Login", urlPatterns =
{
    "/login"
})
public class Login extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(Login.class);

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException
    {        
//        Gson gson = new Gson();
//        //AppSettings response = gson.fromJson(appsettings.json, Response.class);
//        String fileName ="configuration.json";
//        
//        
//        File file=new File("configuration.json");
//        String path = file.toString();
//        // Read from File to String
//        JsonObject jsonObject = new JsonObject();
//        
//        try {
//            JsonParser parser = new JsonParser();
//            JsonElement jsonElement = parser.parse(new FileReader(fileName));
//            jsonObject = jsonElement.getAsJsonObject();
//        } catch (FileNotFoundException e) {
//           
//        } catch (IOException ioe){
//        
//        }
        
        log.trace("doPost()");
        String loginDetailsJson = ServletUtils.getPostRequestJson(request);
        Map<String, String> loginDetailsMap = ServletUtils.convertJsonFormDataToMap(loginDetailsJson);
        
        String username = loginDetailsMap.get("username");
        //Map<String,String> userCredentials = DatabaseAccess.getUserCredentialsFromEmail(loginEmail);
        Map<String,String> userMap = GlobalValues.getUSERS();
        
        
        StandardOutputObject outputObject = new StandardOutputObject();
        if (!userMap.containsKey(username))
        {
            log.trace("account dosent exist");
            outputObject.setSuccess(false);
            outputObject.setErrorCode(ErrorCodes.LOGIN_FAILED);
            writeOutput(response, outputObject);
            return;
        }
        
        String storedPassword = userMap.get(username);
        String loginPassword = loginDetailsMap.get("password");
        if (!storedPassword.equals(loginPassword))
        {
            log.trace("password incorrect");
            outputObject.setSuccess(false);
            outputObject.setErrorCode(ErrorCodes.LOGIN_FAILED);
            writeOutput(response, outputObject);

        } else
        {
            log.trace("password correct");  
            createNewSession(request, loginDetailsMap);
            outputObject.setSuccess(true);
            writeOutput(response, outputObject);
        }

    }

    private void writeOutput(HttpServletResponse response, StandardOutputObject outputObject)
    {
        log.trace("writeOutput()");
        String outputJSON = outputObject.getJSONString();
        log.debug(outputJSON);
        try (PrintWriter out = response.getWriter())
        {
            out.write(outputJSON);
        } catch (IOException ex)
        {
            log.error(ErrorCodes.SENDING_CLIENT_DATA_FAILED.toString(), ex);
        }
    }

    private void createNewSession(HttpServletRequest request, Map<String,String> userCredentials)
    {
        log.trace("createNewSession()");
        HttpSession session = request.getSession(true);
        UserObject user = new UserObject();
        user.setUsername(userCredentials.get("username"));
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(GlobalValues.getSESSION_TIMEOUT_VALUE());
        log.debug("new session created for: "+ user.toString());
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
