jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.commonFunctions.setupNavBar();
        activeEvents.getAllActiveEvents(function(){
            vueFunctions.loadActiveEventsComponent();
        });
    });
});

var activeEvents = function () {

    function getAllActiveEvents(callback)
    {
        jQuery.ajax({
            url: global.serverApi.requests.getactiveevents,
            type: "get",
            dataType: "json",
            success: function (returnObject)
            {
                if (returnObject.success === true)
                {
                    global.setGlobalValues.setActiveEventsArray(returnObject.data);
                } else
                {
                    document.getElementById("feedback").innerHTML = "<div class=\"alert alert-danger\" role=\"alert\">" + global.serverApi.errorCodes[returnObject.errorCode] + " please try again</div>";
                }
                
                if(callback)
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
        getAllActiveEvents: getAllActiveEvents
    };
}();