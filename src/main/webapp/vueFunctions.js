var vueFunctions = function () {
    
    function loadNavBarComponent(callback)
    {
        // create an instance
        var navBar = {};
        
        var navBar = new vueComponents.NavBar({
            replace: false,
            data: {
                serverApi: global.serverApi,
                loginState: global.globalValues.loginState
            }
        });
        // mount it on an element
        navBar.$mount('#navBar');

        if (callback)
        {
            callback();
        }
    }
    
    function loadEventComponents(callback)
    {
        // create an instance
        var event = {};
        
        var event = new vueComponents.Event({
            replace: false,
            data: {
                event: global.globalValues.currentEvent,
                eventStatusCodes: vueComponents.eventStatusCodes
            }
        });
        // mount it on an element
        event.$mount('#event');
        
        var eventComments = new vueComponents.EventComments({
            replace: false,
            data: {
                event: global.globalValues.currentEvent,
                eventComments: global.globalValues.currentEventCommentsArray,
                eventStatusCodes: vueComponents.eventStatusCodes
            }
        });
        eventComments.$mount('#eventComments');

        if (callback)
        {
            callback();
        }
    }

    function loadAdminEventComponents(callback)
    {
        // create an instance
        var adminEvent = new vueComponents.AdminEvent({
            replace: false,
            data: {
                event: global.globalValues.currentEvent,
                eventStatusCodes: vueComponents.eventStatusCodes
            }
        });
        // mount it on an element
        adminEvent.$mount('#adminEvent');
        
        var adminEventComments = new vueComponents.AdminEventComments({
            replace: false,
            data: {
                eventComments: global.globalValues.currentEventCommentsArray,
                eventStatusCodes: vueComponents.eventStatusCodes
            }
        });
        adminEventComments.$mount('#adminEventComments');

        if (callback)
        {
            callback();
        }
    }
    

    function loadUnresolvedEventsComponent(callback)
    {
        // create an instance
        var unresolvedEventsList = new vueComponents.UnresolvedEventsList({
            replace: false,
            data: {
                unresolvedEventsArray: global.globalValues.unresolvedEventsArray,
                eventStatusCodes: vueComponents.eventStatusCodes,
                serverApi : global.serverApi
            }
        });
        // mount it on an element
        unresolvedEventsList.$mount('#unresolvedEventsList');

        if (callback)
        {
            callback();
        }
    }

    function loadResolvedEventsComponent(callback)
    {
        // create an instance
        var resolvedEventsList = new vueComponents.ResolvedEventsList({
            replace: false,
            data: {
                resolvedEventsArray: global.globalValues.resolvedEventsArray,
                eventStatusCodes: vueComponents.eventStatusCodes,
                serverApi : global.serverApi
            }
        });
        // mount it on an element
        resolvedEventsList.$mount('#resolvedEventsList');
        if (callback)
        {
            callback();
        }
    }

    function loadMainAdminComponent(callback)
    {       
        // create an instance
        var adminUnresolvedEventsList = new vueComponents.AdminUnresolvedEventsList({
            replace: false,
            data: {
                adminUnresolvedEventsArray: global.globalValues.unresolvedEventsArray,
                eventStatusCodes: vueComponents.eventStatusCodes,
                serverApi : global.serverApi

            }
        });

        // mount it on an element
        adminUnresolvedEventsList.$mount('#adminUnresolvedEvents');
        
        
        var adminResolvedEventsList = new vueComponents.AdminResolvedEventsList({
            replace: false,
            data: {
                adminResolvedEventsArray: global.globalValues.resolvedEventsArray,
                eventStatusCodes: vueComponents.eventStatusCodes,
                serverApi : global.serverApi
            }
        });
        adminResolvedEventsList.$mount('#adminResolvedEvents');

        if (callback)
        {
            callback();
        }
    }

    function loadDatepickers(callback)
    {
        // create an instance
        var datepickers = new vueComponents.Datepickers({
            replace: false,
        });
        // mount it on an element
        datepickers.$mount('#datepickers');
        if (callback)
        {
            callback();
        }
    }

    return{
        loadUnresolvedEventsComponent: loadUnresolvedEventsComponent,
        loadResolvedEventsComponent: loadResolvedEventsComponent,
        loadDatepickers: loadDatepickers,
        loadMainAdminComponent: loadMainAdminComponent,
        loadAdminEventComponents:loadAdminEventComponents,
        loadEventComponents:loadEventComponents,
        loadNavBarComponent:loadNavBarComponent
    };
}();

