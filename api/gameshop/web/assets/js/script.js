document.addEventListener('DOMContentLoaded', function() {
    var form = document.forms.namedItem("login");
    form.addEventListener('submit', function(ev) {
        var username = document.querySelectorAll("input[type=text]")[0].value;
        var password = document.querySelectorAll("input[type=password]")[0].value;

        console.log("Username -> ", username);
        console.log("Password -> ", password);

        xhr = new XMLHttpRequest();

        xhr.open('POST', 'http://localhost:8080/gameshop_war_exploded/api/login');
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.withCredentials = true;
        xhr.onload = function() {
            if (xhr.status === 200) {
                console.log("received a 200 dude !");
                console.log("Cookies --> " + document.cookie);
                var cookie = document.cookie;
                sessionStorage.setItem("token", cookie);
                var response = JSON.parse(xhr.responseText);
                console.log("RESPONSE --> ", response);
                console.log("HEADERS --> ", xhr.getAllResponseHeaders());
                //window.location.href=response["redirect_url"];
                //sessionStorage.setItem("token", cookie);
            } else if (xhr.status === 500) {
                alert('Oops somethign went wrong, please try later' + xhr.status);
            } else {
                alert('Wrong credentials' + xhr.status);
            }
        };

        var data = 'username=' + username + '&password=' + password;
        xhr.send(encodeURI(data));
        ev.preventDefault();
    });
});