package database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains methods for setting up the database and also getting
 * a connection to communicate with the database.
 *
 * @author max
 */
public class DatabaseUtils
{
    private static final Logger log = LoggerFactory.getLogger(DatabaseUtils.class);
    private static DataSource dataSource;

    /**
     * Sets up the JNDI datasource that will be used to obtain connection
     * objects
     */
    protected static void setupDatasource()
    {
        String jndiname = "jdbc/db";
        try
        {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
        } catch (NamingException e)
        {
            // Handle error that it's not configured in JNDI.
            throw new IllegalStateException(jndiname + " is missing in JNDI!", e);
        }
    }

    /**
     * Fetches a database connection object from the JNDI datasource object
     *
     * @return a Connection object
     */
    protected static Connection getDatabaseConnection()
    {
        Connection connection = null;
        try
        {
            connection = dataSource.getConnection();
        } catch (SQLException ex)
        {
            java.util.logging.Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
    
    @Deprecated
    protected static void loadDatabaseDriver()
    {
        log.trace("loadDatabaseDriver()");
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex)
        {
            log.error("load database driver failed", ex);
        }
    }
    
    protected static void dersgisterDatabaseDriver()
    {
        log.trace("dersgisterDatabaseDriver()");
        ClassLoader aClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> driverEnumeration = DriverManager.getDrivers();

        while (driverEnumeration.hasMoreElements())
        {
            Driver currentDriver = driverEnumeration.nextElement();

            //if currentDriver was registered by this application deregister it
            if (currentDriver.getClass().getClassLoader().equals(aClassLoader))
            {
                try
                {
                    DriverManager.deregisterDriver(currentDriver);
                } catch (SQLException ex)
                {
                    log.error("deregistering database driver failed", ex);
                }
            } else
            {
                //currentDriver is not linked with this application
                //do not deregister it
            }
        }
    }
}
