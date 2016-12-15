package core;

import configurationmodel.ApplicationConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class sets up non database related things on startup. Anything that must be done before the
 * application is running should be done here in the contextInitialized()
 * method. This class also takes care of things when the application closes with
 * the contextDestroyed() method.
 *
 * @author max
 */
@WebListener
public class ApplicationStartup implements ServletContextListener
{
    private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);

    @Override
    public void contextInitialized(ServletContextEvent event)
    {     
        /**
         * sets logging level
         * logging levels are:
         * trace/debug/info/warning/error
         */
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
        log.trace("contextInitialized()");
        ApplicationConfig.setupConfig();
        ServerApi.setupServerAPI();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event)
    {
        log.trace("contextDestroyed()");
    }

}
