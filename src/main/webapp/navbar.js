jQuery(document).ready(function () {
    document.getElementById("homeNav").href = global.serverApi.requests.getcurrenteventspage;
    document.getElementById("resolvedEventsNav").href = global.serverApi.requests.getresolvedeventspage;
    document.getElementById("loginNav").href = global.serverApi.requests.getloginpage;
});