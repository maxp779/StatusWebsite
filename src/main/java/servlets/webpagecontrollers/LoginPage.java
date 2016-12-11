package servlets.webpagecontrollers;

import core.GlobalValues;
import session.SessionManager;
import java.io.IOException;
import javax.servlet.ServletContext;
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
@WebServlet(name = "LoginPage", urlPatterns =
{
    "/getloginpage"
})
public class LoginPage extends HttpServlet
{

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

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
        ServletContext servletContext = this.getServletContext();
        
        //redirect to admin page if user is already logged in
        if (SessionManager.sessionValidate(request))
        {
            String webPageURL = servletContext.getContextPath() + GlobalValues.getADMIN_PAGE_URL();
            response.sendRedirect(webPageURL);
        } else
        {
            String webPageURL = servletContext.getContextPath() + GlobalValues.getLOGIN_PAGE_URL();
            response.sendRedirect(webPageURL);
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
