jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.ajaxFunctions.getLoginState(function () {
            vueFunctions.loadNavBarComponent(function () {
                jQuery("#adminNav").addClass("active");
            });
        });
        vueFunctions.loadDatepickers(function () {
            admin.setupDatepickers(function () {
                global.ajaxFunctions.getUnresolvedEvents(function () {

                    var from = jQuery('#datepicker1').data("DateTimePicker").date();
                    var to = jQuery('#datepicker2').data("DateTimePicker").date();
                    var fromUnix = from.unix(); //UTC unix
                    var toUnix = to.unix();
                    global.ajaxFunctions.getResolvedEventsBetweenDates(fromUnix, toUnix, function () {
                        vueFunctions.loadMainAdminComponent(function () {
                            admin.events.setupEvents();
                            global.commonFunctions.setupRssFeed();
                        });
                    });
                });
            });
        });
    });
});


var admin = function () {

    var ajaxFunctions = function () {
        function getAdminResolvedEventsList(callback)
        {
            var from = jQuery('#datepicker1').data("DateTimePicker").date();
            var to = jQuery('#datepicker2').data("DateTimePicker").date();

            var toServer = {};
            toServer.from = from.unix(); //UTC unix
            toServer.to = to.unix();
            document.getElementById("getEventsButton").innerHTML = "<span class='glyphicon glyphicon-refresh spinning'></span> Loading...";
            jQuery.ajax({
                url: global.serverApi.requests.getresolvedevents,
                type: "get",
                data: toServer,
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    document.getElementById("getEventsButton").innerHTML = "Get events";
                    if (returnObject.success === true)
                    {
                        global.setGlobalValues.setResolvedEventsArray(returnObject.data);
                    } else
                    {
                        global.setGlobalValues.setResolvedEventsArray([]);
                    }
                    if (callback)
                    {
                        callback();
                    }
                },
                error: function (xhr, status, error)
                {
                    console.log("Ajax request failed:" + error.toString());
                }
            });
        }

        function deleteEvent(eventId, isResolvedEvent, callback)
        {
            var toServer = {};
            toServer.eventId = eventId;
            jQuery.ajax({
                url: global.serverApi.requests.deleteevent,
                type: "post",
                data: JSON.stringify(toServer),
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        if (isResolvedEvent)
                        {
                            getAdminResolvedEventsList();
                        } else
                        {
                            global.ajaxFunctions.getUnresolvedEvents();
                        }
                    } else
                    {
                        console.log("deleteEvent() failed");
                    }
                    if (callback)
                    {
                        callback();
                    }
                },
                error: function (xhr, status, error)
                {
                    console.log("Ajax request failed:" + error.toString());
                }
            });
        }

        function createNewEvent(event, callback)
        {
            var feedbackElement = document.getElementById("createNewEventFeedback");
            jQuery.ajax({
                url: global.serverApi.requests.addevent,
                type: "post",
                data: JSON.stringify(event),
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        global.ajaxFunctions.getUnresolvedEvents();
                        document.getElementById("createEventForm").reset();
                        global.commonFunctions.setFeedbackElement(feedbackElement, global.feedbackHtml.createEventSuccess);
                    } else
                    {
                        global.commonFunctions.setFeedbackElement(feedbackElement, global.feedbackHtml.createEventFailed);
                    }
                    if (callback)
                    {
                        callback();
                    }
                },
                error: function (xhr, status, error)
                {
                    console.log("Ajax request failed:" + error.toString());
                    global.commonFunctions.setFeedbackElement(feedbackElement, global.feedbackHtml.requestFailed);
                }
            });
        }

        function setEventToResolved(eventId, callback)
        {
            var toServer = {};
            toServer.eventId = eventId;
            jQuery.ajax({
                url: global.serverApi.requests.seteventresolved,
                type: "post",
                data: JSON.stringify(toServer),
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        global.ajaxFunctions.getUnresolvedEvents();
                        getAdminResolvedEventsList();

                    } else
                    {
                        console.log("setEventToResolved() failed");
                    }
                    if (callback)
                    {
                        callback();
                    }
                },
                error: function (xhr, status, error)
                {
                    console.log("Ajax request failed:" + error.toString());
                }
            });
        }

        function setEventToUnresolved(eventId, callback)
        {
            var toServer = {};
            toServer.eventId = eventId;
            jQuery.ajax({
                url: global.serverApi.requests.seteventunresolved,
                type: "post",
                data: JSON.stringify(toServer),
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        global.ajaxFunctions.getUnresolvedEvents();
                        getAdminResolvedEventsList();

                    } else
                    {
                        console.log("setEventToUnresolved() failed")
                    }
                    if (callback)
                    {
                        callback();
                    }
                },
                error: function (xhr, status, error)
                {
                    console.log("Ajax request failed:" + error.toString());
                }
            });
        }

        return{
            deleteEvent: deleteEvent,
            getAdminResolvedEventsList: getAdminResolvedEventsList,
            createNewEvent: createNewEvent,
            setEventToResolved: setEventToResolved,
            setEventToUnresolved: setEventToUnresolved
        };

    }();

    var events = function () {
        function setupEvents()
        {
            jQuery(document).on("click", "#getEventsButton", function (e) {
                var from = jQuery('#datepicker1').data("DateTimePicker").date();
                var to = jQuery('#datepicker2').data("DateTimePicker").date();
                var fromUnix = from.unix(); //UTC unix
                var toUnix = to.unix();
                global.ajaxFunctions.getResolvedEventsBetweenDates(fromUnix, toUnix);
            });

            jQuery(document).on("click", "#logoutButton", function () {
                ajaxFunctions.logout();
            });

            jQuery(document).on("click", ".deleteResolvedEventButton", function () {
                var clickedElement = this;
                var eventId = clickedElement.dataset.eventId;
                ajaxFunctions.deleteEvent(eventId, true);
            });

            jQuery(document).on("click", ".deleteUnresolvedEventButton", function () {
                var clickedElement = this;
                var eventId = clickedElement.dataset.eventId;
                ajaxFunctions.deleteEvent(eventId, false);
            });

            jQuery(document).on("click", ".setResolvedButton", function () {
                var clickedElement = this;
                var eventId = clickedElement.dataset.eventId;
                ajaxFunctions.setEventToResolved(eventId);
            });

            jQuery(document).on("click", ".setUnresolvedButton", function () {
                var clickedElement = this;
                var eventId = clickedElement.dataset.eventId;
                ajaxFunctions.setEventToUnresolved(eventId);
            });

            jQuery("#createEventForm").submit(function (event) {
                event.preventDefault(); //this prevents the default actions of the form           
                var formData = jQuery("#createEventForm").serializeArray();
                var newEvent = global.commonFunctions.convertFormArrayToJson(formData);

                var startDateMoment = jQuery('#createEventDatepicker').data("DateTimePicker").date();
                var startTimeUnix = startDateMoment.unix();
                newEvent.startTimeUnix = startTimeUnix;
                ajaxFunctions.createNewEvent(newEvent);
            });

            jQuery(document).on("click", "#getEventsButton", function (e) {
                var from = jQuery('#datepicker1').data("DateTimePicker").date();
                var to = jQuery('#datepicker2').data("DateTimePicker").date();
                var fromUnix = from.unix(); //UTC unix
                var toUnix = to.unix();
                global.ajaxFunctions.getResolvedEventsBetweenDates(fromUnix, toUnix);
            });
        }
        return{
            setupEvents: setupEvents
        };
    }();


    /**
     * datepicker1 is from
     * datepicker2 is to
     * @param {type} callback
     * @returns {undefined}
     */
    function setupDatepickers(callback)
    {
        jQuery('#datepicker2').datetimepicker({
            viewMode: 'days',
            format: 'DD/MM/YYYY'
        });
        jQuery('#datepicker1').datetimepicker({
            useCurrent: false, //Important! See issue #1075
            viewMode: 'days',
            format: 'DD/MM/YYYY'
        });

        //prevents days in the future being selected
        jQuery("#datepicker1").on("dp.change", function (e) {
            jQuery('#datepicker2').data("DateTimePicker").minDate(e.date);
        });
        jQuery("#datepicker2").on("dp.change", function (e) {
            jQuery('#datepicker1').data("DateTimePicker").maxDate(e.date);
        });

        //from one month in the past
        var oneMonthAgo = new Date();
        oneMonthAgo.setMonth((new Date().getMonth() - 1));
        jQuery('#datepicker1').data("DateTimePicker").defaultDate(oneMonthAgo);
        jQuery('#datepicker1').data("DateTimePicker").viewDate(oneMonthAgo);

        //to the end of today on the current date
        var endOfDay = new Date();
        endOfDay.setHours(23, 59, 59, 999);
        jQuery('#datepicker2').data("DateTimePicker").defaultDate(endOfDay);

        jQuery('#datepicker1').data("DateTimePicker").showTodayButton(true);
        jQuery('#datepicker2').data("DateTimePicker").showTodayButton(true);

        //datepicker for the create new event form
        jQuery('#createEventDatepicker').datetimepicker({
            viewMode: 'days',
            format: 'DD/MM/YYYY h:mm A'
        });

        jQuery('#createEventDatepicker').data("DateTimePicker").defaultDate(new Date());
        jQuery('#createEventDatepicker').data("DateTimePicker").showTodayButton(true);

        if (callback)
        {
            callback();
        }
    }


    return{
        ajaxFunctions: ajaxFunctions,
        events: events,
        setupDatepickers: setupDatepickers
    };
}();
