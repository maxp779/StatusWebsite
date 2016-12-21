jQuery(document).ready(function () {
    global.ajaxFunctions.getServerApi(function () {
        global.ajaxFunctions.getLoginState(function () {
            vueFunctions.loadNavBarComponent(function () {
                jQuery("#unresolvedEventsNav").addClass("active");
            });
        });
        global.ajaxFunctions.getUnresolvedEvents(function () {
            vueFunctions.loadUnresolvedEventsComponent();
            global.commonFunctions.setupRssFeed();
        });
    });
});