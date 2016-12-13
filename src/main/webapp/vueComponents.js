var vueComponents = function () {

var UnresolvedEventsList = Vue.extend({
template: `<ul class="list-group spacer">
    <div v-if="unresolvedEventsArray.length !== 0">
        <li v-for="event in unresolvedEventsArray" class="list-group-item" v-bind:key="event.eventId">
            <div class="row">
                <div class="col-sm-12">
                    <a href="#" v-bind:data-event-id="event.eventId" class="eventLink">
                        <h4 class="list-group-item-heading">{{event.eventTitle}}</h4>
                    </a>       
                    <div v-html="eventStatusCodes[event.eventStatus]"></div> 
                    <p class="list-group-item-text">Start time: {{new Date(event.startTimeUnix*1000).toLocaleString()}}</p>
                    <p class="list-group-item-text">Last updated: {{new Date(event.lastUpdatedTimeUnix*1000).toLocaleString()}}</p>
                </div>
            </div>
        </li>
    </div>
    <div v-else>
        <li class="list-group-item">
            <div class="row">
                <div class="col-sm-12">
                    <h1><small><p class="text-center">No unresolved or scheduled events</p></small></h1>
                </div>
            </div>
        </li>
    </div>
</ul>`});
        var ResolvedEventsList = Vue.extend({
        template: `<ul class="list-group spacer">
    <div v-if="resolvedEventsArray.length !== 0">
        <li v-for="event in resolvedEventsArray" class="list-group-item" v-bind:key="event.eventId">
            <div class="row">
                <div class="col-sm-12">
                    <a href="#" v-bind:data-event-id="event.eventId" class="eventLink">
                        <h4 class="list-group-item-heading">{{event.eventTitle}}</h4>
                    </a>
                    <div v-html="eventStatusCodes[event.eventStatus]"></div> 
                    <p class="list-group-item-text">Time resolved: {{new Date(event.resolvedTimeUnix*1000).toLocaleString()}}</p>   
                </div>
            </div>
        </li>
    </div>
    <div v-else>
        <li class="list-group-item">
            <div class="row">
                <div class="col-sm-12">
                    <h1><small><p class="text-center">No resolved events for the selected time period</p></small></h1>
                </div>
            </div>
        </li>
    </div>
</ul>`});
        var Datepickers = Vue.extend({
        template: `         <div class="row">
                                <div class='col-sm-5'>
                                    <span class="h4 pull-left">From: </span>
                                    <div class="form-group">
                                        <div class='input-group date' id='datepicker1'>
                                            <input type='text' class="form-control" />
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class='col-sm-5'>
                                    <span class="h4 pull-left">To: </span>
                                    <div class="form-group">
                                        <div class='input-group date' id='datepicker2'>
                                            <input type='text' class="form-control" />
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class='col-sm-2'>
                                    <button id="getEventsButton" type="button" class="btn btn-primary btn-sm pull-right">
                                        Get events
                                    </button>
                                </div>
                            </div>`});
        var AdminUnresolvedEventsList = Vue.extend({
        template: `<ul class="list-group spacer">
    <div v-if="adminUnresolvedEventsArray.length !== 0">
        <li v-for="event in adminUnresolvedEventsArray" class="list-group-item" v-bind:key="event.eventId">
            <div class="row">
                <div class="col-sm-12">
                    <a href="#" v-bind:data-event-id="event.eventId" class="eventLink">
                        <h4 class="list-group-item-heading">{{event.eventTitle}}</h4>
                    </a>
                    <div v-html="eventStatusCodes[event.eventStatus]"></div>
                    <p class="list-group-item-text">Start time: {{new Date(event.startTimeUnix*1000).toLocaleString()}}</p>
                    <p class="list-group-item-text">Last updated: {{new Date(event.lastUpdatedTimeUnix*1000).toLocaleString()}}</p>        
                    <button type="button" class="btn btn-default btn-xs pull-left setResolvedButton spacer" v-bind:data-event-id="event.eventId"><span class="glyphicon glyphicon-ok" style="color:green"></span> Click to set as resolved</button>
                    <button type="button" class="btn btn-default btn-xs pull-right deleteUnresolvedEventButton spacer" v-bind:data-event-id="event.eventId"><span class="glyphicon glyphicon-minus" style="color:red"></span> Delete</button>
                </div>
            </div>
        </li>
    </div>
    <div v-else>
        <li class="list-group-item">
            <div class="row">
                <div class="col-sm-12">
                    <h1><small><p class="text-center">No active/scheduled events</p></small></h1>
                </div>
            </div>
        </li>
    </div>
</ul>`});
        var AdminResolvedEventsList = Vue.extend({
        template: `<ul class="list-group spacer">
    <div v-if="adminResolvedEventsArray.length !== 0">
        <li v-for="event in adminResolvedEventsArray" class="list-group-item" v-bind:key="event.eventId">
            <div class="row">
                <div class="col-sm-12">
                    <a href="#" v-bind:data-event-id="event.eventId" class="eventLink">
                        <h4 class="list-group-item-heading">{{event.eventTitle}}</h4>
                    </a>
                    <div v-html="eventStatusCodes[event.eventStatus]"></div> 
                    <p class="list-group-item-text">Time resolved: {{new Date(event.resolvedTimeUnix*1000).toLocaleString()}}</p>
                    <button type="button" class="btn btn-default btn-xs pull-left setUnresolvedButton spacer" v-bind:data-event-id="event.eventId"><span class="glyphicon glyphicon-exclamation-sign" style="color:red"></span> Click to set as unresolved</button>
                    <button type="button" class="btn btn-default btn-xs pull-right deleteResolvedEventButton spacer" v-bind:data-event-id="event.eventId"><span class="glyphicon glyphicon-minus" style="color:red"></span> Delete</button>
                </div>
            </div>
        </li>
    </div>
    <div v-else>
        <li class="list-group-item">
            <div class="row">
                <div class="col-sm-12">
                    <h1><small><p class="text-center">No events to show for the selected time period</p></small></h1>
                </div>
            </div>
        </li>
    </div>
</ul>`});
        var AdminEvent = Vue.extend({
        template: `            <div class="row">
                <div class="col-sm-12">
                <h3>Modify Event</h3>
                        <form id="updateEventForm">
                            <div class="form-group">
                                <label for="eventTitle">Event title:</label>
                                <input type="text" class="form-control" id="eventTitle" name="eventTitle" required>
                            </div>
                            <div class="form-group">
                                <label for="eventText">Description:</label>
                                <textarea rows="8" cols="50" id="eventText" class="form-control" name="eventText" required></textarea>
                            </div>
                            <div class="form-group">
                                <label for="eventStatus">Current status:</label>
                                <select class="form-control" id="eventStatus" name="eventStatus" required>
                                    <option value="0" class="operational">Operational</option>
                                    <option value="1" class="plannedMaintenance">Planned maintenance</option>
                                    <option value="2" class="minorDisruption">Minor disruption</option>
                                    <option value="3" class="moderateDisruption">Moderate disruption</option>
                                    <option value="4" class="severeDisruption">Severe disruption</option>
                                    <option value="5" class="securityBreach">Security breach</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="startTime">Current start time:</label>
                                <div class='input-group date' id='updateEventDatepicker'>
                                    <input type='text' class="form-control" id="startTime" name="startTime" required/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary spacer">Modify event</button>
                            <div id="updateEventFeedback"></div>
                        </form>
        </div>
</div>`});
        var AdminEventComments = Vue.extend({
        template: ` <div class="row">
                <div class="col-sm-12">
<ul class="list-group">
        <form id="postCommentForm">                   
            <div class="form-group">
                <label for="commentText">Post new comment:</label>
                <textarea rows="8" cols="50" id="eventText" class="form-control" name="commentText" required></textarea>
            </div>                        
            <button type="submit" class="btn btn-primary spacer">Post</button>
        <div id="postCommentFeedback"></div>
        </form>
        
    <h3>Comments:</h3>
    <div v-if="eventComments.length !== 0">
        <li v-for="comment in eventComments" class="list-group-item" v-bind:key="comment.commentId">
            <div class="row">
                <div class="col-sm-12">
                    <p class="list-group-item-text">Posted by: {{comment.userId}}</p>
                    <p class="list-group-item-text">On: {{new Date(comment.postTimeUnix*1000).toLocaleString()}}</p>
                    <p class="list-group-item-text spacer">{{comment.commentText}}</p>

                    <button type="button" class="btn btn-default btn-xs pull-left updateCommentButton spacer" v-bind:data-comment-id="comment.commentId" data-toggle="modal" data-target="#updateCommentModal"><span class="glyphicon glyphicon-edit"></span> Edit</button>
                    <button type="button" class="btn btn-default btn-xs pull-right deleteCommentButton spacer" v-bind:data-comment-id="comment.commentId"><span class="glyphicon glyphicon-minus" style="color:red"></span> Delete</button>
                </div>
            </div>
        </li>
    </div>
    <div v-else>
        <li class="list-group-item">
            <div class="row">
                <div class="col-sm-12">
                    <h1><small><p class="text-center">No comments for this event</p></small></h1>
                </div>
            </div>
        </li>
    </div>
</ul>
 </div>
 </div>`});
        var Event = Vue.extend({
        template: `<div class="row">
                <div class="col-sm-12">
            <div v-if="Object.keys(event).length != 0 && event.constructor === Object">
                <h3>{{event.eventTitle}}</h3>    
                    <div v-html="eventStatusCodes[event.eventStatus]" class="spacer"></div> 
                    <p class="list-group-item-text spacer">Start time: {{new Date(event.startTimeUnix*1000).toLocaleString()}}</p>
                    <p class="list-group-item-text spacer">Last updated: {{new Date(event.lastUpdatedTimeUnix*1000).toLocaleString()}}</p>       
                    <div v-if="event.isResolved === true">
                        <p class="list-group-item-text spacer">Resolved: Yes, the event was resolved at {{new Date(event.resolvedTimeUnix*1000).toLocaleString()}}</p>
                    </div>
                    <div v-else>
                        <p class="list-group-item-text spacer">Resolved: No</p>
                    </div>
                    <p class="list-group-item-text spacer">Decription: {{event.eventText}}</p>
            </div>
            <div v-else>
                <h3>Sorry this event appears to have been deleted</h3>    
            </div>
                </div>
</div>`});
        var EventComments = Vue.extend({
        template: `             <div v-if="Object.keys(event).length != 0 && event.constructor === Object">

<div class="row">
                <div class="col-sm-12">        
    <h3>Comments:</h3>
    <div v-if="eventComments.length !== 0">
        <li v-for="comment in eventComments" class="list-group-item" v-bind:key="comment.commentId">
            <div class="row">
                <div class="col-sm-12">
                    <p class="list-group-item-text">Posted by: {{comment.userId}}</p>
                    <p class="list-group-item-text">On: {{new Date(comment.postTimeUnix*1000).toLocaleString()}}</p>
                    <p class="list-group-item-text spacer">{{comment.commentText}}</p>
                </div>
            </div>
        </li>
    </div>
    <div v-else>
        <li class="list-group-item">
            <div class="row">
                <div class="col-sm-12">
                    <h1><small><p class="text-center">No comments for this event</p></small></h1>
                </div>
            </div>
        </li>
    </div>
</ul>
 </div>
 </div>
</div>`});
        //not a vue component
        var eventStatusCodes = {
        0: "<p class='list-group-item-text operational'>Status: Operational</p>",
                1: "<p class='list-group-item-text plannedMaintenance'>Status: Planned maintenance</p>",
                2: "<p class='list-group-item-text minorDisruption'>Status: Minor disruption</p>",
                3: "<p class='list-group-item-text moderateDisruption'>Status: Moderate disruption</p>",
                4: "<p class='list-group-item-text severeDisruption'>Status: Severe disruption</p>",
                5: "<p class='list-group-item-text securityBreach'>Status: Security breach</p>"
        };
        return{
        UnresolvedEventsList: UnresolvedEventsList,
                ResolvedEventsList: ResolvedEventsList,
                AdminResolvedEventsList:AdminResolvedEventsList,
                AdminUnresolvedEventsList:AdminUnresolvedEventsList,
                Datepickers:Datepickers,
                AdminEvent:AdminEvent,
                AdminEventComments:AdminEventComments,
                Event:Event,
                EventComments:EventComments,
                eventStatusCodes:eventStatusCodes
        };
}();
