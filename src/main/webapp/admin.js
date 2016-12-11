jQuery(document).ready(function () {

    global.ajaxFunctions.getServerApi();

    jQuery("#logoutButton").on("click", function () {
        admin.logout();
        return false;
    });
});

var admin = function () {

    function logout()
    {
        jQuery.ajax({
            url: global.serverApi.requests.logout,
            type: "POST",
            dataType: "json",
            success: function (returnObject)
            {
                if (returnObject.success === true)
                {
                    //localStorage.setItem("loginState", false);
                    window.location.href = returnObject.data;
                } else
                {
                    console.log("Error:" + global.serverApi.errorCodes[returnObject.errorCode]);
                }
            },
            error: function (xhr, status, error)
            {
                console.log("Ajax request failed:" + error.toString());
            }
        });
    }

    return{
        logout: logout
    }
}();
