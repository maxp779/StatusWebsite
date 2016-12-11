jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.commonFunctions.setupNavBar();
        resolvedEvents.setupDatepickers(function () {
            resolvedEvents.getResolvedEventsBetweenDates(function () {
                vueFunctions.loadResolvedEventsComponent();
                resolvedEvents.setupEvents();
            });
        });
    });
});

var resolvedEvents = function () {

    function getResolvedEventsBetweenDates(callback)
    {
        var from = $('#datepicker1').data("DateTimePicker").date();
        var to = $('#datepicker2').data("DateTimePicker").date();

        var toServer = {};
        toServer.from = from.unix(); //UTC unix
        toServer.to = to.unix();

        jQuery.ajax({
            url: global.serverApi.requests.getresolvedevents,
            type: "get",
            data: toServer,
            contentType: "application/json",
            dataType: "json",
            success: function (returnObject)
            {
                console.log("getResolvedEventsBetweenDates");

                console.log(returnObject);
                if (returnObject.success === true)
                {
                    global.setGlobalValues.setResolvedEventsArray(returnObject.data);
                } else
                {
                    document.getElementById("feedback").innerHTML = "<div class=\"alert alert-danger\" role=\"alert\">" + global.serverApi.errorCodes[returnObject.errorCode] + " please try again</div>";
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

    function setupEvents()
    {
        $("#getEventsButton").on("click", function (e) {
            getResolvedEventsBetweenDates();
        });
    }

    /**
     * datepicker1 is from
     * datepicker2 is to
     * @param {type} callback
     * @returns {undefined}
     */
    function setupDatepickers(callback)
    {
        $('#datepicker2').datetimepicker({
            viewMode: 'days',
            format: 'DD/MM/YYYY'
        });
        $('#datepicker1').datetimepicker({
            useCurrent: false, //Important! See issue #1075
            viewMode: 'days',
            format: 'DD/MM/YYYY'
        });

        //prevents days in the future being selected
        $("#datepicker2").on("dp.change", function (e) {
            $('#datepicker1').data("DateTimePicker").minDate(e.date);
        });
        $("#datepicker1").on("dp.change", function (e) {
            $('#datepicker2').data("DateTimePicker").maxDate(e.date);
        });

        //by default show from today
        $('#datepicker1').data("DateTimePicker").defaultDate(new Date());
        
        //to one month in the past
        var oneMonthAgo = new Date();
        oneMonthAgo.setMonth((new Date().getMonth() - 1));
        $('#datepicker2').data("DateTimePicker").defaultDate(oneMonthAgo);

        $('#datepicker1').data("DateTimePicker").showTodayButton(true);

        if (callback)
        {
            callback();
        }
    }

    return{
        getResolvedEventsBetweenDates: getResolvedEventsBetweenDates,
        setupDatepickers: setupDatepickers,
        setupEvents: setupEvents
    };
}();