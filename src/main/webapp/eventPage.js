jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.commonFunctions.setupRssFeed();
        var eventId = global.commonFunctions.getQueryVariable("eventId");
        global.ajaxFunctions.getEvent(eventId, function () {
            global.ajaxFunctions.getEventComments(eventId, function () {
                vueFunctions.loadEventComponents(function () {

                    jQuery(document).on("click", ".backButton", function () {
                        window.history.back();
                    });
                });
            });
        });
    });
});
