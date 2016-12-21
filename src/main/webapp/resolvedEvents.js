jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.ajaxFunctions.getLoginState(function () {
            vueFunctions.loadNavBarComponent(function () {
                jQuery("#resolvedEventsNav").addClass("active");
            });
        });
        vueFunctions.loadDatepickers(function () {
            resolvedEvents.setupDatepickers(function () {

                var from = jQuery('#datepicker1').data("DateTimePicker").date();
                var to = jQuery('#datepicker2').data("DateTimePicker").date();
                var fromUnix = from.unix(); //UTC unix
                var toUnix = to.unix();
                global.ajaxFunctions.getResolvedEventsBetweenDates(fromUnix, toUnix, function () {
                    vueFunctions.loadResolvedEventsComponent();
                    global.commonFunctions.setupRssFeed();
                    resolvedEvents.setupEvents();
                });
            });
        });
    });
});

var resolvedEvents = function () {

    function setupEvents()
    {
        jQuery("#getEventsButton").on("click", function (e) {
            var from = jQuery('#datepicker1').data("DateTimePicker").date();
            var to = jQuery('#datepicker2').data("DateTimePicker").date();
            var fromUnix = from.unix(); //UTC unix
            var toUnix = to.unix();
            global.ajaxFunctions.getResolvedEventsBetweenDates(fromUnix, toUnix);
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

        if (callback)
        {
            callback();
        }
    }

    return{
        setupDatepickers: setupDatepickers,
        setupEvents: setupEvents
    };
}();