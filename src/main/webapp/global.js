var global = function () {

    var commonFunctions = function () {

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

        return{
            isUndefinedOrNull: isUndefinedOrNull
        };
    }();

    var ajaxFunctions = function () {
        function getServerApi(callback)
        {
            console.log("getServerApi()");
            jQuery.ajax({
                dataType: "json",
                type: "GET",
                url: "/getserverapi",
                success: function (returnObject)
                {
                    if (returnObject.success === true)
                    {
                        serverApi = returnObject.data;
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

        return{
            getServerApi: getServerApi
        };
    }();

    /**
     * For robustness serverApi is checked on each page load, if it has been cleared
     * from localStorage possibly due to user action it will be re-requested again.
     */
    var serverApi = function () {
        var storedApi = JSON.parse(localStorage.getItem("serverApi"));
        if (commonFunctions.isUndefinedOrNull(storedApi))
        {
            //getServerApi will store the fetched serverApi in localStorage automatically
            //so we can get it here
            ajaxFunctions.getServerApi(function () {
                var fetchedApi = localStorage.getItem("serverApi");
                global.serverApi = JSON.parse(fetchedApi);
            });
        } else
        {
            return storedApi;
        }
    }();

    return{
        serverApi: serverApi
    };
}();