function forgotPassword(){
    var email = document.getElementById("email");
    $.ajax({
        url: "/resetPassword" ,
        method:"POST",
        data: {
            "email" : email
        },
        success:function (data) {
            console.log("WORKED");
        },
        error: function () {
            console.log("Error");
        }
    });
}