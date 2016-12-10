package controllerservlets;

import core.ErrorCodes;
import core.Event;
import core.StandardOutputObject;
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
@WebServlet(name = "GetEvent", urlPatterns =
{
    "/GetEvent"
})
public class GetEvent extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(GetEvent.class);

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        log.trace("doGet()");
        String eventId = request.getParameter("eventId");
        Event event = DatabaseAccess.getEvent(eventId);
        StandardOutputObject output = new StandardOutputObject();

        if (event != null)
        {
            output.setSuccess(true);
            output.setData(event);
            writeOutput(response, output);
        } else
        {
            output.setSuccess(false);
            output.setErrorCode(ErrorCodes.GET_EVENT_FAILED);
            writeOutput(response, output);
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
