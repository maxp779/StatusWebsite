/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import controllerservlets.*;
import core.Request;
import sessionservlets.*;
import webpageservlets.*;

/**
 * This is the servlet that routes all client requests to the appropriate
 * resource, it also attaches a jsessionid to the request object if there is a
 * jsessionid. Certain servlets require additional commands e.g
 * /AuthenticationServlet/mobileLoginPage as those servlets will serve both
 * mobile and desktop clients and take appropriate action
 *
 * @author max
 */
@WebServlet(name = "frontcontroller", urlPatterns =
{
    "/frontcontroller/*"
})
public class FrontController extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private static final Map<String, String> requestToServletMapping;

    static
    {
        requestToServletMapping = new HashMap<>();

        //webpage requests
        requestToServletMapping.put(Request.getcurrenteventspage.toString(), "/" + CurrentEventsPage.class.getSimpleName());
        requestToServletMapping.put(Request.getresolvedeventspage.toString(), "/" + ResolvedEventsPage.class.getSimpleName());
        requestToServletMapping.put(Request.getloginpage.toString(), "/" + LoginPage.class.getSimpleName());
        requestToServletMapping.put(Request.getadminpage.toString(), "/" + AdminPage.class.getSimpleName());

        //database action requests
        requestToServletMapping.put(Request.login.toString(), "/" + Login.class.getSimpleName());
        requestToServletMapping.put(Request.logout.toString(), "/" + Logout.class.getSimpleName());
        requestToServletMapping.put(Request.addcomment.toString(), "/" + AddComment.class.getSimpleName());
        requestToServletMapping.put(Request.addevent.toString(), "/" + AddEvent.class.getSimpleName());
        requestToServletMapping.put(Request.deletecomment.toString(), "/" + DeleteComment.class.getSimpleName());
        requestToServletMapping.put(Request.deleteevent.toString(), "/" + DeleteEvent.class.getSimpleName());
        requestToServletMapping.put(Request.updatecomment.toString(), "/" + UpdateComment.class.getSimpleName());
        requestToServletMapping.put(Request.seteventresolved.toString(), "/" + SetEventResolved.class.getSimpleName());
        requestToServletMapping.put(Request.seteventunresolved.toString(), "/" + SetEventUnresolved.class.getSimpleName());
        requestToServletMapping.put(Request.updateevent.toString(), "/" + UpdateEvent.class.getSimpleName());
        requestToServletMapping.put(Request.getallcomments.toString(), "/" + GetAllComments.class.getSimpleName());
        requestToServletMapping.put(Request.getallevents.toString(), "/" + GetAllEvents.class.getSimpleName());
        requestToServletMapping.put(Request.getcurrentevents.toString(), "/" + GetCurrentEvents.class.getSimpleName());
        requestToServletMapping.put(Request.geteventcomments.toString(), "/" + GetEventComments.class.getSimpleName());
        requestToServletMapping.put(Request.getresolvedevents.toString(), "/" + GetResolvedEvents.class.getSimpleName());
        requestToServletMapping.put(Request.getserverapi.toString(), "/" + GetServerApi.class.getSimpleName());

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        log.trace("processRequest()");
        log.info(request.getRequestURI());
        log.info("querystring:" + request.getQueryString());
        String currentRequest = request.getRequestURI();
        currentRequest = currentRequest.replaceAll("/" + FrontController.class.getSimpleName() + "/", "");
        log.info(currentRequest);

        if (requestToServletMapping.containsKey(currentRequest))
        {
            String servletName = requestToServletMapping.get(currentRequest);
            forwardRequest(request, response, servletName);
        } else
        {
            log.error("invalid request");
            forwardRequest(request, response, "/ErrorPage");
        }

    }

    private void forwardRequest(HttpServletRequest request, HttpServletResponse response, String servletName) throws ServletException, IOException
    {
        log.trace("forwardRequest()");
        log.info(servletName);
        RequestDispatcher rd = request.getRequestDispatcher(servletName);
        rd.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        processRequest(request, response);
    }

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
        processRequest(request, response);
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
