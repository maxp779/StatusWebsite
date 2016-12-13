jQuery(document).ready(function () {

    global.ajaxFunctions.getServerApi(function () {
        global.commonFunctions.setupNavBar();
    });

    login.autologin();
    login.setupEvents();
});


var login = function () {

    function autologin()
    {
        //autologin for development
        document.getElementById("username").value = "maxpower";
        document.getElementById("password").value = "123";
    }

    function setupEvents()
    {
        jQuery('#loginForm').submit(function () {
            login.loginRequest();
            return false;
        });

        jQuery("#loginForm :input").change(function () {
            document.getElementById("feedback").innerHTML = "";
        });

        //auto selects form input text when clicked
        jQuery(document).on('click', 'input', function () {
            this.select();
        });
    }

    function loginRequest()
    {
        var formData = $("#loginForm").serializeArray();
        var toServer = global.commonFunctions.convertFormArrayToJson(formData);

        $.ajax({
            url: global.serverApi.requests.login,
            type: "POST",
            data: JSON.stringify(toServer),
            contentType: "application/json",
            dataType: "json",
            success: function (returnObject)
            {
                if (returnObject.success === true)
                {
                    localStorage.setItem("loginState", true);
                    window.location = global.serverApi.requests.getadminpage;
                } else
                {
                    document.getElementById("feedback").innerHTML = "<div class=\"alert alert-danger\" role=\"alert\">" + global.serverApi.errorCodes[returnObject.errorCode] + " please try again</div>";
                }
            },
            error: function (xhr, status, error)
            {
                console.log("Ajax request failed:" + error.toString());
            }
        });
    }
    return{
        loginRequest: loginRequest,
        autologin: autologin,
        setupEvents:setupEvents
    };
}();