package servlets.crud;

import database.databasemodels.Event;
import core.ErrorCodes;
import servlets.crud.helperclasses.ServletUtils;
import servlets.crud.helperclasses.StandardOutputObject;
import core.UserObject;
import database.DatabaseAccess;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author max
 */
@WebServlet(name = "AddEvent", urlPatterns =
{
    "/addevent"
})
public class AddEvent extends HttpServlet
{

    private static final Logger log = LoggerFactory.getLogger(AddEvent.class);

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        log.trace("doPost()");
        String eventJsonString = ServletUtils.getPostRequestJson(request);
        Event newEvent = ServletUtils.deserializeEventJson(eventJsonString);
        UserObject currentUser = ServletUtils.getCurrentUser(request);
        newEvent.setUserId(currentUser.getUsername());
        
        newEvent.setEventId(UUID.randomUUID().toString());
        newEvent.setIsResolved(false);
        newEvent.setLastUpdatedUnix(ServletUtils.getCurrentUtcSeconds());
        newEvent.setLastUpdatedTimestamp(ServletUtils.getCurrentUtcLocalDateTime());
        newEvent.setStartTimestamp(ServletUtils.getCurrentUtcLocalDateTime(newEvent.getStartTimeUnix()));

        log.debug("doPost() event to be added:" + newEvent.toString());

        boolean success = DatabaseAccess.addEvent(newEvent);
        StandardOutputObject outputObject = new StandardOutputObject();
        outputObject.setSuccess(success);
        outputObject.setData(newEvent);
        if (success)
        {
            log.info("event added successfully");
            writeOutput(response, outputObject);
        } else
        {
            outputObject.setErrorCode(ErrorCodes.ADD_EVENT_FAILED);
            writeOutput(response, outputObject);
        }
    }

    private void writeOutput(HttpServletResponse response, StandardOutputObject output)
    {
        log.trace("writeOutput()");
        String outputJSON = output.getJSONString();
        log.debug(outputJSON);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter())
        {
            out.print(outputJSON);
        } catch (IOException ex)
        {
            log.error(ErrorCodes.SENDING_CLIENT_DATA_FAILED.toString(), ex);
        }
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
