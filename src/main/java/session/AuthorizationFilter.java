package session;

import core.GlobalValues;
import session.SessionManager;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class intercepts every request to the server and ascertains if the
 * request needs authorization or not.
 *
 * This class came with a lot of default code I dont want to touch in case it
 * breaks things.
 *
 * @author max
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns =
{
    "/*"
})
public class AuthorizationFilter implements Filter
{
    private static final Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public AuthorizationFilter()
    {
    }

    /**
     * This method does the actual filtering.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException
    {
        log.trace("doFilter()");
        HttpServletRequest aRequest = (HttpServletRequest) request;
        HttpServletResponse aResponse = (HttpServletResponse) response;
        String currentURL = aRequest.getRequestURL().toString();
        ServletContext sc = getFilterConfig().getServletContext();
        boolean sessionValid;

        if (!this.needsAuthentication(currentURL)) //if request does not need authentication, pass it on
        {
            chain.doFilter(request, response);
        } else
        {
            sessionValid = SessionManager.sessionValidate((HttpServletRequest) request);
            if (sessionValid)
            {
                if (currentURL.contains("/login.html") || currentURL.contains("/getloginpage")) //if valid session and request is for login page
                {
                    aResponse.sendRedirect(sc.getContextPath() + "/" + GlobalValues.getADMIN_PAGE_URL()); //redirect to admin page
                } else
                {
                    chain.doFilter(request, response);
                }
            } else//if invalid session, redirect to login page
            {
                aResponse.sendRedirect(sc.getContextPath() + "/" + GlobalValues.getLOGIN_PAGE_REQUEST());
            }
        }
    }

    /**
     * This method contains a list of all resources that require authentication
     *
     * @param url
     * @return if auth is required true is returned, false if it is not required
     */
    private boolean needsAuthentication(String url)
    {
        log.trace("needsAuthentication()");
        String[] authResources = GlobalValues.getAUTH_RESOURCES();
        for (String authRequest : authResources)
        {
            if (url.contains(authRequest))
            {
                log.debug("auth required for: " + url);
                return true;
            }
        }
        log.debug("auth not required for: " + url);
        return false;
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig()
    {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig)
    {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy()
    {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig)
    {
        this.filterConfig = filterConfig;
        if (filterConfig != null)
        {
            if (debug)
            {
                log("AuthorizationFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString()
    {
        if (filterConfig == null)
        {
            return ("AuthorizationFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthorizationFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response)
    {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals(""))
        {
            try
            {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex)
            {
            }
        } else
        {
            try
            {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex)
            {
            }
        }
    }

    public static String getStackTrace(Throwable t)
    {
        String stackTrace = null;
        try
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex)
        {
        }
        return stackTrace;
    }

    public void log(String msg)
    {
        filterConfig.getServletContext().log(msg);
    }

}
////
