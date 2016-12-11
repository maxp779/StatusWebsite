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
        //get data from form, it is formatted as an array of json objects with the
        //form data held in name/value pairs like so:
        //[{"name":"username", "value":"maxpower"},{"name":"password", "value":"123"}]
        var formData = $("#loginForm").serializeArray();

        $.ajax({
            url: global.serverApi.requests.login,
            type: "POST",
            data: JSON.stringify(formData),
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