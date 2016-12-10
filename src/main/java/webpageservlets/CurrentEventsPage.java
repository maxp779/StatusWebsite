/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webpageservlets;

import core.GlobalValues;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet determines whether to send the client to the mobile login page
 * or the desktop login page, if they are already logged in it will send the
 * client to the appropriate main page
 *
 * @author max
 */
@WebServlet(name = "CurrentEventsPage", urlPatterns =
{
    "/getcurrenteventspage"

})
public class CurrentEventsPage extends HttpServlet
{

    private static final Logger log = LoggerFactory.getLogger(CurrentEventsPage.class);

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
        ServletContext sc = this.getServletContext();
        String webPageURL = sc.getContextPath() + GlobalValues.getCURRENT_EVENTS_PAGE_URL();
        response.sendRedirect(webPageURL);
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
