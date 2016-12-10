package core;

/**
 * All requests the client can make to the server are listed here
 * @author max
 */
public enum Request
{    
    frontcontroller,
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
    getcurrenteventspage,
    getloginpage,
    getresolvedeventspage,
    getallcomments,
    getallevents,
    getcurrentevents,
    geteventcomments,
    getresolvedevents,
    getserverapi
}
