jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.commonFunctions.setupNavBar();
        global.commonFunctions.setupRssFeed();
        vueFunctions.loadDatepickers(function () {
            resolvedEvents.setupDatepickers(function () {

                var from = jQuery('#datepicker1').data("DateTimePicker").date();
                var to = jQuery('#datepicker2').data("DateTimePicker").date();
                var fromUnix = from.unix(); //UTC unix
                var toUnix = to.unix();

                global.ajaxFunctions.getResolvedEventsBetweenDates(fromUnix, toUnix, function () {
                    vueFunctions.loadResolvedEventsComponent();
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

        jQuery(document).on("click", ".eventLink", function () {
            var clickedElement = this;
            var eventId = clickedElement.dataset.eventId;
            var url = global.serverApi.requests.geteventpage;
            window.location = url + "?eventId=" + eventId;
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
        jQuery("#datepicker2").on("dp.change", function (e) {
            jQuery('#datepicker1').data("DateTimePicker").minDate(e.date);
        });
        jQuery("#datepicker1").on("dp.change", function (e) {
            jQuery('#datepicker2').data("DateTimePicker").maxDate(e.date);
        });

        //by default show from one day in the future
        var date = new Date();
        date.setDate(date.getDate() + 1);
        jQuery('#datepicker1').data("DateTimePicker").defaultDate(date);

        //to one month in the past
        var oneMonthAgo = new Date();
        oneMonthAgo.setMonth((new Date().getMonth() - 1));
        jQuery('#datepicker2').data("DateTimePicker").defaultDate(oneMonthAgo);

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