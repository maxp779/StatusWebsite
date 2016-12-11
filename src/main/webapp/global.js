var global = function () {

    var commonFunctions = function () {

//        function updateObjectProperties(objectToUpdate, objectWithNewProperties)
//        {
//            for (var property in objectWithNewProperties)
//            {
//                objectToUpdate[property] = objectWithNewProperties[property];
//            }
//        }

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

        function setupNavBar()
        {
            document.getElementById("activeEventsNav").href = global.serverApi.requests.getactiveeventspage;
            document.getElementById("resolvedEventsNav").href = global.serverApi.requests.getresolvedeventspage;
            document.getElementById("loginNav").href = global.serverApi.requests.getloginpage;
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
            setupNavBar: setupNavBar,
            setGlobalValuesLocalStorage: setGlobalValuesLocalStorage
        };
    }();

    var globalValues = function () {

        var activeEventsArray = [];
        var resolvedEventsArray = [];
        var currentEventCommentsArray = [];

        /**
         * This if condition fetches any stored globalValues from localStorage
         * automatically.
         */
        var storedGlobalValues = localStorage.getItem("globalValues");
        if (!commonFunctions.isUndefinedOrNull(storedGlobalValues))
        {
            storedGlobalValues = JSON.parse(storedGlobalValues);
            activeEventsArray = storedGlobalValues.activeEventsArray;
            resolvedEventsArray = storedGlobalValues.resolvedEventsArray;
            currentEventCommentsArray = storedGlobalValues.currentEventCommentsArray;
        }

        return {
            activeEventsArray: activeEventsArray,
            resolvedEventsArray: resolvedEventsArray,
            currentEventCommentsArray: currentEventCommentsArray //comments for the event that is currently being looked at on the event.html page
        };
    }();

    var setGlobalValues = function () {
        function setActiveEventsArray(newArray, callback) {
            commonFunctions.updateArrayObjects(globalValues.activeEventsArray, newArray);
            commonFunctions.setGlobalValuesLocalStorage();
            if (callback) {
                callback();
            }
        }
        function setResolvedEventsArray(newArray, callback) {
            commonFunctions.updateArrayObjects(globalValues.resolvedEventsArray, newArray);
            commonFunctions.setGlobalValuesLocalStorage();
            if (callback) {
                callback();
            }
        }
        function setCurrentEventCommentsArray(newArray, callback) {
            commonFunctions.updateArrayObjects(globalValues.currentEventCommentsArray, newArray);
            commonFunctions.setGlobalValuesLocalStorage();
            if (callback) {
                callback();
            }
        }

        return{
            setActiveEventsArray: setActiveEventsArray,
            setResolvedEventsArray: setResolvedEventsArray,
            setCurrentEventCommentsArray: setCurrentEventCommentsArray
        };
    }();

    var ajaxFunctions = function () {
        function getServerApi(callback)
        {
            console.log("getServerApi()");
            var storedApi = JSON.parse(localStorage.getItem("serverApi"));
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
                            localStorage.setItem("serverApi", JSON.stringify(returnObject.data));
                            if (callback)
                            {
                                callback();
                            }
                        } else
                        {
                            console.log("Error: Failed to fetch API from server, server may be down");
                        }

                    },
                    error: function (xhr, status, error)
                    {
                        console.log("Ajax request failed:" + error.toString());
                    }
                });
            }
        }
        return{
            getServerApi: getServerApi
        };
    }();

    var serverApi = {};

    return{
        serverApi: serverApi,
        commonFunctions: commonFunctions,
        ajaxFunctions: ajaxFunctions,
        globalValues: globalValues,
        setGlobalValues: setGlobalValues
    };
}();