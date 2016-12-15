package rss;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import database.databasemodels.Event;
import core.GlobalValues;
import database.DatabaseAccess;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet was not created by myself, it was obtained from here:
 * https://rometools.github.io/rome/RssAndAtOMUtilitiEsROMEV0.5AndAboveTutorialsAndArticles/RssAndAtOMUtilitiEsROMEV0.5TutorialUsingROMEWithinAServletToCreateAndReturnAFeed.html
 * I have modified the code to output the unresolved events.
 *
 * @originalauthor Alejandro Abdelnur
 * @modified by Max Power
 */
@WebServlet(name = "RssServlet", urlPatterns =
{
    "/rss",
    "/rss/"
})
public class RssServlet extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(RssServlet.class);
    private static final String DEFAULT_FEED_TYPE = "default.feed.type";
    private static final String FEED_TYPE = "type";
    private static final String MIME_TYPE = "application/xml; charset=UTF-8";
    private static final String COULD_NOT_GENERATE_FEED_ERROR = "Could not generate feed";

    private static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    private String _defaultFeedType;

    public void init()
    {
        _defaultFeedType = getServletConfig().getInitParameter(DEFAULT_FEED_TYPE);
        _defaultFeedType = (_defaultFeedType != null) ? _defaultFeedType : "atom_0.3";
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        try
        {
            SyndFeed feed = getFeed(req);

            String feedType = req.getParameter(FEED_TYPE);
            feedType = (feedType != null) ? feedType : _defaultFeedType;
            feed.setFeedType(feedType);

            res.setContentType(MIME_TYPE);
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, res.getWriter());
        } catch (FeedException ex)
        {
            String msg = COULD_NOT_GENERATE_FEED_ERROR;
            log(msg, ex);
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
        }
    }

    protected SyndFeed getFeed(HttpServletRequest request) throws IOException, FeedException
    {
        ServletContext servletContext = this.getServletContext();
        String rssLink = servletContext.getContextPath() + GlobalValues.getRSS_REQUEST();

        SyndFeed feed = new SyndFeedImpl();
        feed.setTitle("Status website RSS feed");
        feed.setLink(rssLink);
        feed.setDescription("This feed has been created using ROME (Java syndication utilities)");

        List entries = new ArrayList();
        SyndEntry entry;
        SyndContent description;

        List<Event> unresolvedEvents = getUnresolvedEvents();

        if (unresolvedEvents != null)
        {
            String eventUrl = request.getScheme() //"http"
                    + "://"
                    + request.getServerName() //"statuswebsite"
                    + ":"
                    + request.getServerPort(); //"8080"

            for (Event currentEvent : unresolvedEvents)
            {
                entry = new SyndEntryImpl();
                entry.setTitle(currentEvent.getEventTitle());
                entry.setLink(eventUrl + "/geteventpage?eventId=" + currentEvent.getEventId());
                entry.setUri(eventUrl + "/geteventpage?eventId=" + currentEvent.getEventId());

                try
                {
                    LocalDateTime startTimestamp = currentEvent.getStartTimestamp();
                    entry.setPublishedDate(DATE_PARSER.parse(startTimestamp.toString()));
                } catch (ParseException ex)
                {
                    // IT CANNOT HAPPEN WITH THIS SAMPLE
                }
                description = new SyndContentImpl();
                description.setType("text/plain");
                description.setValue(currentEvent.getEventText());
                entry.setDescription(description);
                entries.add(entry);
            }
        }
        else
        {
            //unresolvedEvents was null, no entries to add
        }

        feed.setEntries(entries);

        return feed;
    }

    private List<Event> getUnresolvedEvents()
    {
        List<Event> output;
        output = DatabaseAccess.getUnresolvedEventsForRss();

        //sort by start time
        Collections.sort(output);
        return output;
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
