jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.commonFunctions.setupNavBar();
        global.commonFunctions.setupRssFeed();
        global.ajaxFunctions.getUnresolvedEvents(function () {
            vueFunctions.loadUnresolvedEventsComponent();

        });
    });
});