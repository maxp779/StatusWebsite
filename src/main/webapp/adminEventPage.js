jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.commonFunctions.setupRssFeed();
        var eventId = global.commonFunctions.getQueryVariable("eventId");
        global.ajaxFunctions.getEvent(eventId, function () {
            global.ajaxFunctions.getEventComments(eventId, function () {
                vueFunctions.loadAdminEventComponents(function () {
                    adminEventPage.populateUpdateEventForm();
                    adminEventPage.events.setupEvents();
                });
            });
        });
    });
});


var adminEventPage = function () {

    var events = function () {

        var setupEvents = function () {
            jQuery("#updateEventForm").submit(function (event) {
                event.preventDefault(); //this prevents the default actions of the form           
                var formData = jQuery("#updateEventForm").serializeArray();
                var formattedFormData = global.commonFunctions.convertFormArrayToJson(formData);

                var updatedEvent = jQuery.extend(true, {}, global.globalValues.currentEvent);

                for (var property in formattedFormData)
                {
                    updatedEvent[property] = formattedFormData[property];
                }

                var startDateMoment = jQuery('#updateEventDatepicker').data("DateTimePicker").date();
                var startTimeUnix = startDateMoment.unix();
                updatedEvent.startTimeUnix = startTimeUnix;

                ajaxFunctions.updateEvent(updatedEvent);
            });

            jQuery("#createCommentForm").submit(function (event) {
                event.preventDefault(); //this prevents the default actions of the form           
                var formData = jQuery("#createCommentForm").serializeArray();
                var newComment = global.commonFunctions.convertFormArrayToJson(formData);
                newComment.eventId = global.globalValues.currentEvent.eventId;

                ajaxFunctions.createNewComment(newComment);
            });

            jQuery(document).on("click", ".updateCommentButton", function () {
                var clickedElement = this;
                var commentId = clickedElement.dataset.commentId;
                global.globalValues.currentComment = global.commonFunctions.getComment(commentId);
                populateUpdateCommentForm(global.globalValues.currentComment);
            });

            jQuery(document).on("click", ".deleteCommentButton", function () {
                var clickedElement = this;
                var commentId = clickedElement.dataset.commentId;
                var commentToDelete = global.commonFunctions.getComment(commentId);
                delete commentToDelete.postTimestamp; //gson on the server has issues parsing this
                ajaxFunctions.deleteComment(commentToDelete);
            });

            jQuery("#updateCommentForm").submit(function (event) {
                event.preventDefault(); //this prevents the default actions of the form           
                var formData = jQuery("#updateCommentForm").serializeArray();
                var formattedFormData = global.commonFunctions.convertFormArrayToJson(formData);
                var updatedComment = global.globalValues.currentComment;
                updatedComment.commentText = formattedFormData.commentText;
                delete updatedComment.postTimestamp; //gson on the server has issues parsing this

                ajaxFunctions.updateComment(updatedComment);
            });

            jQuery(document).on("click", ".backButton", function () {
                window.history.back();
            });
        };

        return{
            setupEvents: setupEvents
        };
    }();

    var ajaxFunctions = function () {

        function createNewComment(comment, callback)
        {
            var feedbackElement = document.getElementById("createCommentFeedback");
            jQuery.ajax({
                url: global.serverApi.requests.addcomment,
                type: "post",
                data: JSON.stringify(comment),
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        global.ajaxFunctions.getEventComments(global.globalValues.currentEvent.eventId);
                        document.getElementById("newCommentText").value = "";
                        global.commonFunctions.setFeedbackElement(feedbackElement, global.feedbackHtml.createCommentSuccess);
                    } else
                    {
                        global.commonFunctions.setFeedbackElement(feedbackElement, global.feedbackHtml.createCommentFailed);
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

        function updateEvent(event, callback)
        {
            var feedbackElement = document.getElementById("updateEventFeedback");

            jQuery.ajax({
                url: global.serverApi.requests.updateevent,
                type: "post",
                data: JSON.stringify(event),
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        global.ajaxFunctions.getUnresolvedEvents();
                        global.commonFunctions.setFeedbackElement(feedbackElement, global.feedbackHtml.updateEventSuccess);

                    } else
                    {
                        global.commonFunctions.setFeedbackElement(feedbackElement, global.feedbackHtml.updateEventFailed);
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

        function updateComment(comment, callback)
        {
            var feedbackElement = document.getElementById("updateCommentFeedback");
            jQuery.ajax({
                url: global.serverApi.requests.updatecomment,
                type: "post",
                data: JSON.stringify(comment),
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        global.ajaxFunctions.getEventComments(global.globalValues.currentEvent.eventId);
                        global.commonFunctions.setFeedbackElement(feedbackElement, global.feedbackHtml.updateCommentSuccess);
                    } else
                    {
                        global.commonFunctions.setFeedbackElement(feedbackElement, global.feedbackHtml.updateCommentFailed);

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

        function deleteComment(comment, callback)
        {
            jQuery.ajax({
                url: global.serverApi.requests.deletecomment,
                type: "post",
                data: JSON.stringify(comment),
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        global.ajaxFunctions.getEventComments(global.globalValues.currentEvent.eventId);
                    } else
                    {
                        console.log("deleteComment() failed");
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
            createNewComment: createNewComment,
            deleteComment: deleteComment,
            updateEvent: updateEvent,
            updateComment: updateComment
        };

    }();

    function populateUpdateEventForm(callback)
    {
        jQuery('#updateEventDatepicker').datetimepicker({
            viewMode: 'days',
            format: 'DD/MM/YYYY h:mm A'
        });
        jQuery('#updateEventDatepicker').data("DateTimePicker").showTodayButton(true);

        document.getElementById("eventTitle").value = global.globalValues.currentEvent.eventTitle;
        document.getElementById("eventText").value = global.globalValues.currentEvent.eventText;
        document.getElementById("eventStatus").value = global.globalValues.currentEvent.eventStatus;
        var currentStartTime = global.globalValues.currentEvent.startTimeUnix;

        //*1000 because javascript deals in milliseconds rather than seconds
        jQuery('#updateEventDatepicker').data("DateTimePicker").defaultDate(new Date(currentStartTime * 1000));

        if (callback)
        {
            callback();
        }
    }

    function populateUpdateCommentForm(comment)
    {
        document.getElementById("currentCommentText").value = comment.commentText;
    }

    return{
        ajaxFunctions: ajaxFunctions,
        events: events,
        populateUpdateEventForm: populateUpdateEventForm,
        populateUpdateCommentForm: populateUpdateCommentForm
    };
}();