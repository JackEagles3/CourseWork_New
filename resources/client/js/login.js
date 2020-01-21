function pageLoad() {

    if(window.location.search === '?logout') {
        document.getElementById('content').innerHTML = '<h1>Logging out, please wait...</h1>';
        logout();
    } else {
        document.getElementById("loginButton").addEventListener("click", login);
        document.getElementById("newUser").addEventListener("click", newUser);
    }

}

function login(event) {


    if ((document.getElementById("loginButton").innerText) === "Add user"){
        event.preventDefault();

        const form = document.getElementById("loginForm");
        const formData = new FormData(form);

        fetch("/LogIn/AddUser", {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                pageLoad();
            }
        });

    }else {
        event.preventDefault();

        const form = document.getElementById("loginForm");
        const formData = new FormData(form);

        fetch("/LogIn/login", {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {

                Cookies.set("username", responseData.UserName);
                Cookies.set("token", responseData.token);
                Cookies.set("Role", responseData.Role);

                console.log(Cookies.toString())


                window.location.href = '/client/index.html';
            }
        });
    }


}

function logout() {

    fetch("/LogIn/logout", {method: 'post'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {

            alert(responseData.error);

        } else {

            Cookies.remove("username");
            Cookies.remove("token");
            Cookies.remove("Role");

            window.location.href = '/client/index.html';

        }
    });

}

function newUser(){
    if (document.getElementById("newUser").innerText === "New User") {
        document.getElementById("H").innerText = "New User";
        document.getElementById("loginButton").innerText = "Add user";
        document.getElementById("newUser").innerText = "Log In"
    }else{
        document.getElementById("H").innerText = "Please log in to continue...";
        document.getElementById("loginButton").innerText = "LogIn";
        document.getElementById("newUser").innerText = "New User"
    }

}
function index(){window.location.href = "http://localhost:8081/client/index.html";}