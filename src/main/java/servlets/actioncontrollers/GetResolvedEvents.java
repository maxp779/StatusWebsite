package servlets.actioncontrollers;

import core.ErrorCodes;
import core.StandardOutputObject;
import database.DatabaseAccess;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deals with queries for resolved events between two dates
 * 
 * @author max
 */
@WebServlet(name = "GetResolvedEvents", urlPatterns =
{
    "/getresolvedevents"
})
public class GetResolvedEvents extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(GetResolvedEvents.class);

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
        
        Long fromDate = Long.parseLong(request.getParameter("from"));
        Long toDate = Long.parseLong(request.getParameter("to"));              
        List resolvedEvents = DatabaseAccess.getResolvedEvents(fromDate, toDate);
        StandardOutputObject output = new StandardOutputObject();
        
        //even if resolvedEvents is null it means no events were found which is still success
        output.setSuccess(true);
        if (resolvedEvents != null)
        {
            output.setData(resolvedEvents);
            writeOutput(response, output);
        } else
        {
            output.setData(new ArrayList<>());
            output.setErrorCode(ErrorCodes.GET_RESOLVED_EVENTS_FAILED);
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
