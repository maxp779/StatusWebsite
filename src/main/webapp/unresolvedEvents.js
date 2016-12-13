jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.commonFunctions.setupNavBar();
        global.ajaxFunctions.getUnresolvedEvents(function () {
            vueFunctions.loadUnresolvedEventsComponent();

            jQuery(document).on("click", ".eventLink", function () {
                var clickedElement = this;
                var eventId = clickedElement.dataset.eventId;
                var url = global.serverApi.requests.geteventpage;
                window.location = url + "?eventId=" + eventId;
            });
        });
    });
});