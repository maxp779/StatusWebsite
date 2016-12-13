/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rss;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import database.databasemodels.Event;
import core.GlobalValues;
import database.DatabaseAccess;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author max
 */
public class UpdateRssFeed
{
    private static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    public static SyndFeed updateFeed()
    {

//        ServletContext servletContext = this.getServletContext();
//        String rssLink = servletContext.getContextPath() + GlobalValues.getRSS_REQUEST();

        SyndFeed feed = new SyndFeedImpl();
        feed.setTitle("Sample Feed (created with ROME)");
        feed.setLink("http://localhost:8080/rss");
        feed.setDescription("This feed has been created using ROME (Java syndication utilities");

        List entries = new ArrayList();
        SyndEntry entry;
        SyndContent description;

        //get active/scheduled events
        List<Event> activeEvents = DatabaseAccess.getUnresolvedEventsForRss();

        for (Event currentEvent : activeEvents)
        {
            entry = new SyndEntryImpl();
            entry.setTitle(currentEvent.getEventTitle());
            entry.setLink("http://localhost:8080/"+ GlobalValues.getUNRESOLVED_EVENTS_PAGE());
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

        feed.setEntries(entries);

        return feed;
    }

}
