var global = function () {

    var feedbackHtml = function () {
        var createEventSuccess = "<span class='label label-success'><span class='glyphicon glyphicon-ok' aria-hidden='true'></span> Create event success</span>";
        var createEventFailed = "<span class='label label-danger'><span class='glyphicon glyphicon-remove' aria-hidden='true'></span> Create event failed</span>";
        var updateEventSuccess = "<span class='label label-success'><span class='glyphicon glyphicon-ok' aria-hidden='true'></span> Event update success</span>";
        var updateEventFailed = "<span class='label label-danger'><span class='glyphicon glyphicon-remove' aria-hidden='true'></span> Event update failed</span>";
        var createCommentSuccess = "<span class='label label-success'><span class='glyphicon glyphicon-ok' aria-hidden='true'></span> Post comment success</span>";
        var createCommentFailed = "<span class='label label-danger'><span class='glyphicon glyphicon-remove' aria-hidden='true'></span> Post comment failed</span>";
        var updateCommentSuccess = "<span class='label label-success'><span class='glyphicon glyphicon-ok' aria-hidden='true'></span> Edit comment success</span>";
        var updateCommentFailed = "<span class='label label-danger'><span class='glyphicon glyphicon-remove' aria-hidden='true'></span> Edit comment failed</span>";
        var ajaxRequestFailed = "<span class='label label-danger'><span class='glyphicon glyphicon-remove' aria-hidden='true'></span> Request failed</span>";

        return{
            createEventSuccess: createEventSuccess,
            createEventFailed: createEventFailed,
            updateEventSuccess: updateEventSuccess,
            updateEventFailed: updateEventFailed,
            createCommentSuccess: createCommentSuccess,
            createCommentFailed: createCommentFailed,
            updateCommentSuccess: updateCommentSuccess,
            updateCommentFailed: updateCommentFailed,
            ajaxRequestFailed: ajaxRequestFailed
        };
    }();

    var commonFunctions = function () {

        function setFeedbackElement(htmlElement, htmlContent, displayTime = 5000, callback)
        {
            htmlElement.innerHTML = htmlContent;
            
            setTimeout(function () {
                htmlElement.innerHTML = "";
            }, displayTime);
            
            if (callback)
            {
                callback();
            }
        }

        function setupRssFeed()
        {
            //put rss link at top
            var rssRequestUrl = global.serverApi.requests.rss;
            jQuery('head').append("<link rel='alternate' type='application/rss+xml' title='Status Website RSS feed' href=" + rssRequestUrl + "/>");

            //put clickable icon on page
            var rssLinkElement = document.getElementById("rssLink");
            if (!isUndefinedOrNull(rssLinkElement))
            {
                rssLinkElement.innerHTML = "<a href=" + rssRequestUrl + "><img src='/img/rss.png' style='width:20px;height:20px;'></a>";
            }
        }

        function getQueryVariable(variable)
        {
            var query = window.location.search.substring(1);
            var vars = query.split("&");
            for (var i = 0; i < vars.length; i++) {
                var pair = vars[i].split("=");
                if (pair[0] == variable) {
                    return pair[1];
                }
            }
            return(false);
        }

        function getComment(commentId)
        {
            for (var property in globalValues.currentEventCommentsArray)
            {
                if (globalValues.currentEventCommentsArray[property].commentId === commentId)
                {
                    return jQuery.extend(true, {}, globalValues.currentEventCommentsArray[property]);
                }
            }
        }

        /**
         * This function converts a forms serializeArray() output into a single
         * object.
         * 
         * e.g this array:
         * 
         * [{"name":"eventTitle", "value":"Server downtime"},{"name":"eventText", "value":"Description of event goes here"}]
         * 
         * will be converted to:
         * 
         * {"eventTitle":"Server downtime", "eventText":"Description of event goes here"}
         *
         * @param {type} formArray an array derived from serializeArray()
         * @returns a javascript object with the values from formArray
         */
        function convertFormArrayToJson(formArray)
        {
            var outputObject = {};
            for (var index = 0; index < formArray.length; index++)
            {
                var currentObject = formArray[index];
                outputObject[currentObject.name] = currentObject.value;
            }
            return outputObject;
        }

        function updateArrayObjects(arrayToUpdate, arrayWithNewObjects)
        {
            while (arrayToUpdate.length > 0)
            {
                arrayToUpdate.pop();
            }
            for (var count = 0; count < arrayWithNewObjects.length; count++)
            {
                arrayToUpdate.push(arrayWithNewObjects[count]);
            }
        }

        function isUndefinedOrNull(aVariable)
        {
            var output;
            if (typeof aVariable === 'undefined' || aVariable === null || aVariable === "null")
            {
                output = true;
            } else
            {
                output = false;
            }
            return output;
        }

        /**
         * This method updates the globalValues local storage object with the latest values
         * from the server
         * @param {type} callback
         * @returns {undefined}
         */
        function setGlobalValuesLocalStorage(callback)
        {
            localStorage.setItem("globalValues", JSON.stringify(globalValues));
            if (callback)
            {
                callback();
            }
        }

        return{
            isUndefinedOrNull: isUndefinedOrNull,
            updateArrayObjects: updateArrayObjects,
            setupRssFeed: setupRssFeed,
            setGlobalValuesLocalStorage: setGlobalValuesLocalStorage,
            getComment: getComment,
            convertFormArrayToJson: convertFormArrayToJson,
            getQueryVariable: getQueryVariable,
            setFeedbackElement: setFeedbackElement
        };
    }();

    var globalValues = function () {

        var unresolvedEventsArray = [];
        var resolvedEventsArray = [];
        var currentEventCommentsArray = [];
        var currentEvent = {}; //if an individual event is being viewed it is stored here
        var currentComment = {}; //for storing the current comment being editted
        var loginState = false;

        return {
            unresolvedEventsArray: unresolvedEventsArray,
            resolvedEventsArray: resolvedEventsArray,
            currentEventCommentsArray: currentEventCommentsArray,
            currentEvent: currentEvent,
            currentComment: currentComment,
            loginState: loginState
        };
    }();

    var setGlobalValues = function () {
        function setUnresolvedEventsArray(newArray, callback) {

            //sort by last updated time in descending order           
            newArray.sort(function (first, second) {
                return second.lastUpdatedTimeUnix - first.lastUpdatedTimeUnix;

            });

            commonFunctions.updateArrayObjects(globalValues.unresolvedEventsArray, newArray);
            if (callback) {
                callback();
            }
        }

        function setResolvedEventsArray(newArray, callback) {

            //sort by resolved time in descending order           
            newArray.sort(function (first, second) {
                return second.resolvedTimeUnix - first.resolvedTimeUnix;

            });

            commonFunctions.updateArrayObjects(globalValues.resolvedEventsArray, newArray);
            if (callback) {
                callback();
            }
        }
        function setCurrentEventCommentsArray(newArray, callback) {

            //sort by post time in descending order           
            newArray.sort(function (first, second) {
                return second.postTimeUnix - first.postTimeUnix;

            });
            commonFunctions.updateArrayObjects(globalValues.currentEventCommentsArray, newArray);
            if (callback) {
                callback();
            }
        }

        function setAdminEventsArray(newArray, callback) {
            commonFunctions.updateArrayObjects(globalValues.adminEventsArray, newArray);

            //sort by start time
            globalValues.adminEventsArray.sort(function (a, b) {
                if (a.startTimeUnix > b.startTimeUnix) {
                    return -1;
                }
                if (a.startTimeUnix < b.startTimeUnix) {
                    return 1;
                }
                return 0;
            });

            if (callback) {
                callback();
            }
        }

        return{
            setUnresolvedEventsArray: setUnresolvedEventsArray,
            setResolvedEventsArray: setResolvedEventsArray,
            setCurrentEventCommentsArray: setCurrentEventCommentsArray,
            setAdminEventsArray: setAdminEventsArray
        };
    }();

    var ajaxFunctions = function () {

        function getLoginState(callback)
        {
            jQuery.ajax({
                url: global.serverApi.requests.getloginstate,
                type: "get",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        globalValues.loginState = returnObject.data;
                    } else
                    {
                        console.log("getLoginState() failed");
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

        function getEvent(eventId, callback)
        {
            var toServer = {};
            toServer.eventId = eventId;

            jQuery.ajax({
                url: global.serverApi.requests.getsingleevent,
                type: "get",
                data: toServer,
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        globalValues.currentEvent = returnObject.data;
                    } else
                    {
                        console.log("getEvent() failed");
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

        function getEventComments(eventId, callback)
        {
            var toServer = {};
            toServer.eventId = eventId;
            jQuery.ajax({
                url: global.serverApi.requests.geteventcomments,
                type: "get",
                data: toServer,
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        global.setGlobalValues.setCurrentEventCommentsArray(returnObject.data);
                    } else
                    {
                        global.setGlobalValues.setCurrentEventCommentsArray([]);
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

        function getUnresolvedEvents(callback)
        {
            jQuery.ajax({
                url: global.serverApi.requests.getunresolvedevents,
                type: "get",
                dataType: "json",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        global.setGlobalValues.setUnresolvedEventsArray(returnObject.data);
                    } else
                    {
                        global.setGlobalValues.setUnresolvedEventsArray([]);
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


        function getServerApi(callback)
        {
            console.log("getServerApi()");
            var storedApi = JSON.parse(localStorage.getItem("statusWebsiteServerApi"));
            if (!commonFunctions.isUndefinedOrNull(storedApi)) //if serverApi is in localStorage
            {
                global.serverApi = storedApi;
                if (callback)
                {
                    callback();
                }
            } else //if not we fetch it from the server
            {
                jQuery.ajax({
                    dataType: "json",
                    type: "GET",
                    url: "/getserverapi",
                    success: function (returnObject)
                    {
                        if (returnObject.success === true)
                        {
                            global.serverApi = returnObject.data;
                            localStorage.setItem("statusWebsiteServerApi", JSON.stringify(returnObject.data));
                            if (callback)
                            {
                                callback();
                            }
                        } else
                        {
                            console.log("getServerApi() failed, server may be down");
                        }
                    },
                    error: function (xhr, status, error)
                    {
                        console.log("Ajax request failed:" + error.toString());
                    }
                });
            }
        }

        function getResolvedEventsBetweenDates(from, to, callback)
        {
            var toServer = {};
            toServer.from = from;
            toServer.to = to;

            document.getElementById("getEventsButton").innerHTML = "<span class='glyphicon glyphicon-refresh spinning'></span> Loading...";
            jQuery.ajax({
                url: global.serverApi.requests.getresolvedevents,
                type: "get",
                data: toServer,
                contentType: "application/json",
                dataType: "json",
                success: function (returnObject)
                {
                    console.log("getResolvedEventsBetweenDates");
                    document.getElementById("getEventsButton").innerHTML = "Get events";
                    console.log(returnObject);
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

        return{
            getServerApi: getServerApi,
            getUnresolvedEvents: getUnresolvedEvents,
            getResolvedEventsBetweenDates: getResolvedEventsBetweenDates,
            getEventComments: getEventComments,
            getEvent: getEvent,
            getLoginState: getLoginState
        };
    }();

    var serverApi = {};

    return{
        serverApi: serverApi,
        commonFunctions: commonFunctions,
        ajaxFunctions: ajaxFunctions,
        globalValues: globalValues,
        setGlobalValues: setGlobalValues,
        feedbackHtml: feedbackHtml
    };
}();