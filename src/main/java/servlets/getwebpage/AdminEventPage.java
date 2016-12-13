/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets.getwebpage;

import core.GlobalValues;
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
 * This is a page that will show an individual event with comments and all
 * details etc. It is intended to be shown when an event from a list is clicked
 * on.
 *
 * @author max
 */
@WebServlet(name = "AdminEventPage", urlPatterns =
{
    "/getadmineventpage"
})
public class AdminEventPage extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(AdminEventPage.class);

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
        ServletContext servletContext = this.getServletContext();
        String webPageURL = servletContext.getContextPath() + GlobalValues.getADMIN_EVENT_PAGE_URL() + "?eventId=" + eventId;
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
