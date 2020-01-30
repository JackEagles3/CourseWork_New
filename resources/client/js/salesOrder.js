function pageLoad() {


    let OrderHtml = '<table class = "tables" border="1" >' + //Starts off the html which will be added to the div, and creates the start of the table
        '<tr>' +
        '<th>ID</th>' +
        '<th>UserID</th>' +
        '<th class="last">Date</th>' +
        '</tr>';

    fetch('/SalesOrder/ReadSaleOrder', {method: 'get'}).then(response => response.json()).then(Orders => { //Calls the API method for listing locations via the server

        for (let Order of Orders) {//Goes through all of the locations and adds all the details to the table rows

            OrderHtml += `<tr>` +
                `<td>${Order.SaleID}</td>` +
                `<td>${Order.UserId}</td>` +
                `<td>${Order.Date}</td>` +
                `<td class="last">` +
                `<button class='view' data-id='${Order.SaleID}'>View Sale Details</button> ` + //Adds buttons to the tables
                `<button class='delete' data-id='${Order.SaleID}'>Delete</button>` +
                `</td>` +
                `</tr>`;

        }

        OrderHtml += `</table>`; //Closes Table

        document.getElementById("listDiv").innerHTML = OrderHtml; //Adds the table html to the div to be displayed

        //All of the code belows adds the functions to the buttons which are within the tables
        let viewButtons = document.getElementsByClassName("view");
        for (let button of viewButtons) {
            button.addEventListener("click", viewDetails);
        }
        let viewButtons2 = document.getElementsByClassName("delete");
        for (let button of viewButtons2) {
            button.addEventListener("click", deleteButton);
        }

    });


}

function index(){window.location.href = "http://localhost:8081/client/index.html";}

function viewDetails(event){

    document.getElementById("AddNew").style.visibility = "hidden";
    //document.getElementById("AddNew2").style.visibility = "hidden";
    //document.getElementById("AddNew3").innerText = "";
    //document.getElementById("new").innerText = "";



    const SaleID = event.target.getAttribute("data-id");
    let Details = '<table class = "tables" border="1" >' + //Starts off the html which will be added to the div, and creates the start of the table
        '<tr>' +
        '<th>Item Name</th>' +
        '<th>Quantity</th>' +
        '<th class="last">Price</th>' +
        '</tr>';

    fetch('/SalesOrderDetails/ReadSalesOrderDetails/' + SaleID, {method: 'get'}).then(response => response.json()).then(Orders => { //Calls the API method for listing locations via the server

        for (let Order of Orders) {//Goes through all of the locations and adds all the details to the table rows

            Details += `<tr>` +
                `<td>${Order.ItemID}</td>` +
                `<td>${Order.Quantity}</td>` +
                `<td>${Order.Price}</td>` +
                `<td class="last">` +
                `<button class='edit' data-id='${Order.ItemID}'>Edit</button> ` + //Adds buttons to the tables
                `<button class='delete' data-id='${Order.ItemID}'>Delete</button>` +
                `</td>` +
                `</tr>`;

        }

        Details += `</table>` +
            `<button id="NewDetails" onclick="NewDetail()">Add New Details</button>`; //Closes Table

        document.getElementById("listDiv").innerHTML = Details; //Adds the table html to the div to be displayed
        let view = document.getElementsByClassName("edit");
        for (let button of view) {
            button.addEventListener("click", editdetails);
        }

        let de = document.getElementsByClassName("delete");
        for (let button of de) {
            button.addEventListener("click", deleteButton2);
        }
        Cookies.set("PurchaseId", event.target.getAttribute("data-id"));

    });
}

function editdetails(event) {
    document.getElementById("NewDetails").style.visibility = "hidden";


    document.getElementById("listDiv").innerHTML +=  `<form id="DetailsForm">`+
        `<input type="hidden" name="PurchaseId" id="PurchaseId">` +
        `<input type="hidden" name="ItemId" id="ItemId">` +
        `<div class="ItemDiv">`+
        `<label for="Quantity">Quantity: </label>`+
        `<input type="text" name="Quantity" id="Quantity">`+
        `</div> `+

        `<div class="ItemDiv">`+
        `<label for="Price">Price: </label>`+
        `<input type="text" name="Price" id="Price">`+
        `</div> `+

        `</form>`;

    document.getElementById("listDiv").innerHTML +=  `<button onclick="EditConfirm()">Confirm</button>`;

    document.getElementById("PurchaseID").value = event.target.getAttribute("data-id");

    document.getElementById("ItemId").value = "0";



}

function EditConfirm() {


    debugger
    const form = document.getElementById("DetailsForm");
    const formData = new FormData(form);



    fetch('/SalesOrderDetails/UpdateSalesOrderDetails', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        alert("Yes");
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }


    });

    pageLoad();

}
function deleteButton2(event) {
    const ok = confirm("Are you sure?");

    if (ok === true) {

        let id = event.target.getAttribute("data-id");
        let formData = new FormData();
        formData.append("id", id);

        fetch('/SalesOrderDetails/DeleteSalesOrderDetails', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    pageLoad();
                }
            }
        );
        pageLoad();
    }}

function NewDetail() {

    document.getElementById("NewDetails").style.visibility = "hidden";


    document.getElementById("listDiv").innerHTML +=  `<form id="DetailsForm">`+
        `<input type="hidden" name="PurchaseId" id="PurchaseId">` +
        `<div class="ItemDiv" id="Item">`+

        ` </div>`+
        `<div class="ItemDiv">`+
        `<label for="Quantity">Quantity: </label>`+
        `<input type="text" name="Quantity" id="Quantity">`+
        `</div> `+

        `<div class="ItemDiv">`+
        `<label for="Price">Price: </label>`+
        `<input type="text" name="Price" id="Price">`+
        `</div> `+

        `</form>`;

    document.getElementById("listDiv").innerHTML +=  `<button onclick="Confirm()">Confirm</button>`;

    fetch('/Inventory/List', {method: 'get'}).then(response => response.json()).then(Locations => { //Calls the API method for listing locations via the server

        let LocationsHtml = `<label for="ItemName">Item Name: </label>` +
            `<select name="ItemName" id="ItemName">`;


        for (let LocationName of Locations) {//Goes through all of the locations and adds all the details to the table rows
            LocationsHtml += `<option value=${LocationName.id} id="ItemName">${LocationName.Name}</option>`;
        }

        LocationsHtml += `</select>`;
        document.getElementById("Item").innerHTML = LocationsHtml;
    });

}

function Confirm(event) {


    document.getElementById("PurchaseId").value = Cookies.get("PurchaseId");

    const form = document.getElementById("DetailsForm");
    const formData = new FormData(form);



    fetch('/SalesOrderDetails/AddSaleOrderDetail', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        alert("Yes");
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }else{
            pageLoad();
        }

    });

    pageLoad();

}

function index(){window.location.href = "http://localhost:8081/client/index.html";}

function addnew() {



    const form = document.getElementById("Add");
    const formData = new FormData(form);



    fetch('/SalesOrder/AddSaleOrder', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }else{
            pageLoad();
        }


    });





}

function deleteButton(event) {




    const ok = confirm("Are you sure?"); //Alert to check the user has clicked on the button on purpose

    if (ok === true) {

        let id = event.target.getAttribute("data-id"); //Gets the id of the purchase order
        let formData = new FormData();
        formData.append("id", id); // Adds id to form

        fetch('/SalesOrder/DeleteSalesOrder', {method: 'post', body: formData} //Fetches the API to delete the purchase order
        ).then(response => response.json()
        ).then(responseData => {

                if (responseData.hasOwnProperty('error')) { //Checks for errors
                    alert(responseData.error);
                } else {
                    pageLoad(); //If no errors loads page again to remove the order from the page.
                }
            }
        );
    }}