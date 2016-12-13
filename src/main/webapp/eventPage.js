jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        var eventId = global.commonFunctions.getQueryVariable("eventId");
        global.ajaxFunctions.getEvent(eventId, function () {
            global.ajaxFunctions.getEventComments(eventId, function () {
                vueFunctions.loadEventComponents(function () {
                });
            });
        });
    });
});
