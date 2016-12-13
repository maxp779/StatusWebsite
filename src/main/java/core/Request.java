package core;

/**
 * All requests the client can make to the server are listed here
 * @author max
 */
public enum Request
{    
    login,
    logout,
    addevent,
    updateevent,
    seteventresolved,
    seteventunresolved,
    deleteevent,
    addcomment,
    updatecomment,
    deletecomment,
    getadminpage,
    getadmineventpage,
    geteventpage,
    getunresolvedeventspage,
    getloginpage,
    getresolvedeventspage,
    getallcomments,
    getevents,
    getsingleevent,
    getunresolvedevents,
    getresolvedevents,
    geteventcomments,
    getserverapi,
    rss
}
