/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import database.databasemodels.Comment;
import database.databasemodels.Event;
import database.DatabaseAccess;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author max
 */
public class PopulateDatabase
{
    public static void populateDatabase()
    {
        Event currentEvent;
        List<Event> eventList = new ArrayList<>();
        List<String> eventUUIDs = new ArrayList<>();
        Random rng = new Random();
        for (int count = 0; count < 30; count++)
        {
            String eventUUID = UUID.randomUUID().toString();
            eventUUIDs.add(eventUUID);

            currentEvent = new Event();
            currentEvent.setEventId(eventUUID);
            currentEvent.setEventTitle("Event title number:" + rng.nextInt(101));
            currentEvent.setEventText("We are currently mitigating an inbound distributed denial of service (DDoS) attack which is causing a proportion of packet loss on some inbound routes.\n"
                    + "\n"
                    + "We are currently working on reducing the inbound load to restore normal service levels.\n"
                    + "\n"
                    + "A further update will be provided when we have further information.");
            ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
            currentEvent.setStartTimeUnix(nowUTC.toEpochSecond());
            currentEvent.setStartTimestamp(LocalDateTime.now());
            currentEvent.setEventStatus("ModerateDisruption");
            currentEvent.setLastUpdatedUnix(nowUTC.toEpochSecond());
            currentEvent.setLastUpdatedTimestamp(LocalDateTime.now());
            currentEvent.setIsActive(rng.nextBoolean());
            currentEvent.setUserId("maxpower");
            eventList.add(currentEvent);
        }

        List<Comment> commentList = new ArrayList<>();
        for (int count = 0; count < 100; count++)
        {
            Comment currentComment = new Comment();
            currentComment.setCommentId(UUID.randomUUID().toString());
            currentComment.setCommentText("We believe the source of the attack is now under control and we will continue to monitor for activity.\n"
                    + "");
            currentComment.setEventId(eventUUIDs.get(rng.nextInt(30)));
            ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
            currentComment.setPostTimeUnix(nowUTC.toEpochSecond());
            currentComment.setPostTimestamp(LocalDateTime.now());
            currentComment.setUserId("commenter779");
            commentList.add(currentComment);
        }

        for (Event anEvent : eventList)
        {
            DatabaseAccess.addEvent(anEvent);
        }
        for (Comment aComment : commentList)
        {
            DatabaseAccess.addComment(aComment);
        }
    }
}
