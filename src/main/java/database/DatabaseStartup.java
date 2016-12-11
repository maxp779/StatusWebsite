package database;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class loads the database driver and sets up the datasource object
 * on startup.
 * 
 * It also deregisters the database driver when the application closes.
 *
 * @author max
 */
@WebListener
public class DatabaseStartup implements ServletContextListener
{
    private static final Logger log = LoggerFactory.getLogger(DatabaseStartup.class);

    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        log.trace("contextInitialized()");
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");

        DatabaseUtils.loadDatabaseDriver();
        DatabaseUtils.setupDatasource();
        
        //PopulateDatabase.populateDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event)
    {
        log.trace("contextDestroyed()");

        //deregister database driver **this may not even be needed**
        DatabaseUtils.dersgisterDatabaseDriver();
    }

}
