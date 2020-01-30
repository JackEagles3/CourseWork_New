

function pageLoad() {



    let LocationHtml = '<table class = "tables" border="1" >' + //Starts off the html which will be added to the div, and creates the start of the table
        '<tr>' +
        '<th>Id</th>' +
        '<th class="last">Name</th>' +

        '</tr>';

    fetch('/Location/ListLocations', {method: 'get'}).then(response => response.json()).then(Locations => { //Calls the API method for listing locations via the server

        for (let LocationName of Locations) {//Goes through all of the locations and adds all the details to the table rows

            LocationHtml += `<tr>` +
                `<td>${LocationName.id}</td>` +
                `<td>${LocationName.Name}</td>` +
                `<td class="last">` +
                `<button class='editButton' data-id='${LocationName.id}'>Edit</button>  ` + //Adds buttons to the tables
                `<button class='deleteButton' data-id='${LocationName.id}'>Delete</button>  ` +
                `<button class='viewButton' data-id='${LocationName.id}'>View Location</button>`+
                `</td>` +
                `</tr>`;

        }

        LocationHtml += `<tr>` +  //Adds the location of everywhere
            `<td></td>` +
            `<td>All</td>` +
            `<td class="last">` +
            `<button class='viewButton' data-id='${LocationName.id}'>View Location</button>`+
            `</td>` +
            `</tr>`;

        LocationHtml += `</table>`; //Closes Table

        document.getElementById("listDiv").innerHTML = LocationHtml; //Adds the table html to the div to be displayed
        document.getElementById("LocationName").innerText = '';


        //All of the code belows adds the functions to the buttons which are within the tables
        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.addEventListener("click", editLocation);
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.addEventListener("click", deleteLocation);
        }

        let viewButtons = document.getElementsByClassName("viewButton");
        for (let button of viewButtons) {
            button.addEventListener("click", viewLocation);
        }

        checkLogin();

    });



    document.getElementById("saveButton").addEventListener("click", saveEditLocation);


    document.getElementById("cancelButton").addEventListener("click", cancelEditLocation);



}

function viewLocation(event){

    const location = event.target.getAttribute("data-id");

    window.location.href = "/client/ItemDetails.html?location="+location;

}



function editLocation(event){



        const id = event.target.getAttribute("data-id");

        fetch('/Location/get/' + id, {method: 'get'}
        ).then(response => response.json()
        ).then(location => {

            if (location.hasOwnProperty('error')) {
                alert(location.error);
            } else {

                document.getElementById("editHeading").innerHTML = 'Editing ' + location.name + ':';

                document.getElementById("LocationId").value = id;
                document.getElementById("LocationName").value = location.name;


            }
        });

    }

function deleteLocation(event) {


    const ok = confirm("Are you sure?");

    if (ok === true) {

        let id = event.target.getAttribute("data-id");
        let formData = new FormData();
        formData.append("id", id);

        fetch('/Location/DeleteLocation', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    pageLoad();
                }
            }
        );
    }

}



function saveEditLocation(event) {



        event.preventDefault();

        if (document.getElementById("LocationName").value.trim() === '') { //Gets the location name
            alert("Please provide a Location name.");
            return;
        }


        const id = document.getElementById("LocationId").value; //Gets Id of location
        const form = document.getElementById("LocationForm"); //Gets new Location name
        const formData = new FormData(form); //adds the form into form data

        let apiPath = ''; //Adds the correct path
        if (id === '') {
            apiPath = '/Location/AddLocation';
        } else {
            apiPath = '/Location/UpdateLocation';
        }

        fetch(apiPath, {method: 'post', body: formData} //Fetches the correct api with the form data included
        ).then(response => response.json()
        ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error); //Alerts user of error if there is one
            }

            pageLoad(); //If there is no error, reload the page
        });



}


    function cancelEditLocation(event) {

        event.preventDefault();

        document.getElementById("editHeading").innerHTML = 'Add New Location:';

        document.getElementById("LocationId").value = '';
        document.getElementById("LocationName").value = '';


    }


function checkLogin() {

  

    let username = Cookies.get("username");

    let logInHTML = '';

    if (username === undefined) {

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "hidden";
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "hidden";
        }

        let viewButtons = document.getElementsByClassName("viewButton");
        for (let button of viewButtons) {
            button.style.visibility = "hidden";
        }


        document.getElementById("SignIn").innerHTML = `<button onclick="window.location.href = '/client/login.html'">Log In</button>`;

    } else {

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "visible";
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "visible";
        }

        let viewButtons = document.getElementsByClassName("viewButton");
        for (let button of viewButtons) {
            button.style.visibility = "visible";
        }

        document.getElementById("SignIn").innerHTML = `<button onclick="window.location.href = '/client/login.html?logout'">Log Out</button>`;


    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}

function LoadSalesOrder(){ window.location.href = "/client/saleOrder.html"}
function LoadPurchaseOrder(){ window.location.href = "/client/purchaseOrder.html"}


function index(){window.location.href = "http://localhost:8081/client/index.html";}