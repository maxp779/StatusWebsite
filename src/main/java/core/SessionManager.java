package core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet deals with validation of sessions, it also invalidates sessions
 * when requested.
 *
 * @author max
 */
public class SessionManager
{
    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);

    /**
     * This method returns true if the HttpSession attached to the request is
     * still valid.
     *
     * @param request
     * @return true if session is valid, false otherwise
     */
    public static boolean sessionValidate(HttpServletRequest request)
    {
        log.trace("sessionValidate()");
        HttpSession session = request.getSession(false);
        boolean sessionValid;

        if (session == null) //path for invalid session
        {
            log.debug("session invalid, session is null");
            sessionValid = false;
        }else //path for valid session
        {
            log.debug("session valid");
            sessionValid = true;
        }
        return sessionValid;
    }

    public static void httpSessionRemove(HttpSession session)
    {
        log.trace("httpSessionRemove()");
        session.invalidate();
    }

}
