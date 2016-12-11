var vueFunctions = function () {

    function loadActiveEventsComponent()
    {
        // create an instance
        var activeEventsList = new vueComponents.ActiveEventsList({
            replace: false,
            data: {
                activeEventsArray: global.globalValues.activeEventsArray
            }
        });
        // mount it on an element
        activeEventsList.$mount('#activeEventsList');
    }
    
    function loadResolvedEventsComponent()
    {
        // create an instance
        var resolvedEventsList = new vueComponents.ResolvedEventsList({
            replace: false,
            data: {
                resolvedEventsArray: global.globalValues.resolvedEventsArray
            }
        });
        // mount it on an element
        resolvedEventsList.$mount('#resolvedEventsList');
    }

    return{
        loadActiveEventsComponent: loadActiveEventsComponent,
        loadResolvedEventsComponent:loadResolvedEventsComponent
    };
}();

