function pageLoad() {


    let OrderHtml = '<table class = "tables" border="1" >' + //Starts off the html which will be added to the div, and creates the start of the table
        '<tr>' +
        '<th>ID</th>' +
        '<th>UserID</th>' +
        '<th class="last">Date</th>' +
        '</tr>';

    fetch('/PurchaseOrder/Read', {method: 'get'}).then(response => response.json()).then(Orders => { //Calls the API method for listing locations via the server

        for (let Order of Orders) {//Goes through all of the locations and adds all the details to the table rows

            OrderHtml += `<tr>` +
                `<td>${Order.id}</td>` +
                `<td>${Order.UserID}</td>` +
                `<td>${Order.Date}</td>` +
                `<td class="last">` +
                `<button class='view' data-id='${Order.id}'>View Sale Details</button> ` + //Adds buttons to the tables
                `<button class='delete' data-id='${Order.id}'>Delete</button>` +
                `</td>` +
                `</tr>`;

        }

        OrderHtml += `</table>`; //Closes Table

        document.getElementById("listDiv").innerHTML = OrderHtml; //Adds the table html to the div to be displayed

        //All of the code belows adds the functions to the buttons which are within the tables
        let viewButtons = document.getElementsByClassName("viewButton");
        for (let button of viewButtons) {
            button.addEventListener("click", viewDetails);
        }

    });


}

function viewDetails(){

}