package servlets.crud;

import database.databasemodels.Comment;
import core.ErrorCodes;
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
@WebServlet(name = "UpdateComment", urlPatterns =
{
    "/updatecomment"
})
public class UpdateComment extends HttpServlet
{

    private static final Logger log = LoggerFactory.getLogger(UpdateComment.class);

    
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
        Comment updatedComment = ServletUtils.deserializeCommentJson(commentJsonString);
        
        log.debug("doPost() updated comment is:"+updatedComment.toString());

        boolean success = DatabaseAccess.updateComment(updatedComment);
        StandardOutputObject outputObject = new StandardOutputObject();
        outputObject.setSuccess(success);
        outputObject.setData(updatedComment);
        if (success)
        {
            log.info("comment updated successfully");
            writeOutput(response, outputObject);
        } else
        {
            outputObject.setErrorCode(ErrorCodes.UPDATE_COMMENT_FAILED);
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
