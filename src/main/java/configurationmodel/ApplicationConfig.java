package configurationmodel;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class sets up the Config object by reading details from config.json.
 * Config.json should be stored in the root of the application.
 * The Config object contains login usernames and passwords.
 * 
 * @author max
 */
public class ApplicationConfig
{
    private static Config currentConfig;

    public static void setupConfig()
    {
        try
        {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream is = classLoader.getResourceAsStream("config.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            Gson gson = new Gson();
            currentConfig = gson.fromJson(sb.toString(), Config.class);
        } catch (IOException ex)
        {
            Logger.getLogger(ApplicationConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Config getCurrentConfig()
    {
        return currentConfig;
    }
}
