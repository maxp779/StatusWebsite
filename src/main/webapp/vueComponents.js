var vueComponents = function () {

var ActiveEventsList = Vue.extend({
template: `<ul class="list-group">
    <div v-if="activeEventsArray.length !== 0">
        <li v-for="event in activeEventsArray" class="list-group-item" v-bind:key="event.eventId">
            <div class="row">
                <div class="col-sm-12">
                    <a href="#" v-bind:data-event-uuid="event.eventId">
                        <h4 class="list-group-item-heading">{{event.eventTitle}}</h4>
                    </a>
                    <p class="list-group-item-text operational">Severity: {{event.eventStatus}}</p>   
                    <p class="list-group-item-text">Last updated: {{event.lastUpdatedTimestamp}}</p>   
                </div>
            </div>
        </li>
    </div>
    <div v-else>
        <li class="list-group-item">
            <div class="row">
                <div class="col-sm-12">
                    <h1><small><p class="text-center">No active or scheduled events</p></small></h1>
                </div>
            </div>
        </li>
    </div>
</ul>`});
        var ResolvedEventsList = Vue.extend({
        template: `<ul class="list-group">
    <div v-if="resolvedEventsArray.length !== 0">
        <li v-for="event in resolvedEventsArray" class="list-group-item" v-bind:key="event.eventId">
            <div class="row">
                <div class="col-sm-12">
                    <a href="#" v-bind:data-event-uuid="event.eventId">
                        <h4 class="list-group-item-heading">{{event.eventTitle}}</h4>
                    </a>
                    <p class="list-group-item-text operational">Severity: {{event.eventStatus}}</p>   
                    <p class="list-group-item-text">Time resolved: {{event.resolvedTimestamp}}</p>   
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
    
        return{
        ActiveEventsList: ActiveEventsList,
                ResolvedEventsList: ResolvedEventsList
        };
}();
