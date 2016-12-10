package sessionservlets;

import core.ErrorCodes;
import core.GlobalValues;
import core.SessionManager;
import core.StandardOutputObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet logs out the user by invalidating their current session then it
 * redirects them back to the appropriate login page.
 *
 * @author max
 */
@WebServlet(name = "Logout", urlPatterns =
{
    "/logout"
})
public class Logout extends HttpServlet
{

    private static final Logger log = LoggerFactory.getLogger(Logout.class);

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

        SessionManager.httpSessionRemove(request.getSession());
        ServletContext sc = request.getServletContext();

        StandardOutputObject outputObject = new StandardOutputObject();
        boolean success = request.getSession(false) == null;
        outputObject.setSuccess(success);

        if (success)
        {
            outputObject.setData(sc.getContextPath() + GlobalValues.getLOGIN_PAGE_URL());
        } else
        {
            outputObject.setErrorCode(ErrorCodes.LOGOUT_FAILED);
        }

        writeOutput(response, outputObject);
    }

    private void writeOutput(HttpServletResponse response, StandardOutputObject outputObject)
    {
        log.trace("writeOutput()");
        String outputJSON = outputObject.getJSONString();
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
