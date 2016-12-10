package controllerservlets;

import core.ErrorCodes;
import core.ServletUtils;
import core.StandardOutputObject;
import core.Comment;
import core.UserObject;
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
@WebServlet(name = "AddComment", urlPatterns =
{
    "/addcomment"
})
public class AddComment extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(AddComment.class);

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
        String commentJsonString = ServletUtils.getPostRequestJson(request);       
        Comment newComment = ServletUtils.deserializeCommentJson(commentJsonString);
        UserObject currentUser = ServletUtils.getCurrentUser(request);        
        newComment.setUserId(currentUser.getUsername());
        
        log.debug("doPost() comment to be added:"+newComment.toString());

        boolean success = DatabaseAccess.addComment(newComment);
        StandardOutputObject outputObject = new StandardOutputObject();
        outputObject.setSuccess(success);
        
        outputObject.setData(newComment);
        if (success)
        {
            log.info("comment added successfully");
            writeOutput(response, outputObject);
        } else
        {
            outputObject.setErrorCode(ErrorCodes.ADD_COMMENT_FAILED);
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
