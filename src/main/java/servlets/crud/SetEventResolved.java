package servlets.crud;

import core.ErrorCodes;
import database.databasemodels.Event;
import servlets.crud.helperclasses.ServletUtils;
import servlets.crud.helperclasses.StandardOutputObject;
import database.DatabaseAccess;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "UpdateEventResolvedState", urlPatterns =
{
    "/seteventresolved"
})
public class SetEventResolved extends HttpServlet
{

    private static final Logger log = LoggerFactory.getLogger(SetEventResolved.class);

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
        Event resolvedEvent = ServletUtils.deserializeEventJson(eventJsonString);    
        resolvedEvent.setResolvedTimeUnix(ServletUtils.getCurrentUtcSeconds());
        resolvedEvent.setResolvedTimestamp(ServletUtils.getCurrentUtcLocalDateTime());
        resolvedEvent.setLastUpdatedUnix(ServletUtils.getCurrentUtcSeconds());
        resolvedEvent.setLastUpdatedTimestamp(ServletUtils.getCurrentUtcLocalDateTime());
        
        log.debug("doPost() event to be set to resolved is:"+resolvedEvent.toString());
        boolean success = DatabaseAccess.setEventResolved(resolvedEvent);
        StandardOutputObject outputObject = new StandardOutputObject();
        outputObject.setSuccess(success);
        outputObject.setData(resolvedEvent);
        if (success)
        {
            log.info("event set to resolved");
            writeOutput(response, outputObject);
        } else
        {
            outputObject.setErrorCode(ErrorCodes.SET_EVENT_RESOLVED_FAILED);
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
