function ToIndexPage(){
    window.location.href = "http://localhost:8081/client/index.html";
}

function editItem() {



}

function pageLoad(){

    let LocationHtml = '<table>' + //Creates table
        '<tr>' +
        '<th>Id</th>' +
        '<th>Name</th>' +
        '<th>Price</th>' +
        '<th class="last">Quantity</th>' +
        '</tr>';


    let id = window.location.search; //Finds the id of the location which is used to identify which API method is called
    id = id.substring(10, id.length);
    if (id != "LocationName"){  //Checks if their is an id, if there is then it will called the API which gets the items in the certain location
    fetch('/Location/get/' + id, {method: 'get'}
    ).then(response => response.json()
    ).then(location => {

        document.getElementById("ItemLocations").innerText += "Item Details in "+ location.name;

        fetch('/Inventory/getLocation/' + location.id, {method: 'get'}).then(response => response.json()).then(Item => {  //Calls the method to get items in location and stores the response

            for (let ItemName of Item) {
                //For loop goes through the response data and adds all the details into the table which is displayed to user.

                LocationHtml += `<tr>` +
                    `<td>${ItemName.id}</td>` +
                    `<td class="ItemNames">${ItemName.name}</td>` +
                    `<td>${ItemName.price}</td>` +
                    `<td>${ItemName.quantity}</td>` +
                    `<td class="last">` +
                    `<button class='editButton' data-id='${ItemName.id}'>Edit</button>  ` +
                    `<button class='deleteButton' data-id='${ItemName.id}'>Delete</button>  ` +
                    `</td>` +
                    `</tr>`;

            }

            LocationHtml += `</table>`;  //Adds the closing of the table to the html

            document.getElementById("listDiv").innerHTML = LocationHtml;  // Adds the Html variable to the html ItemDetails page

            //Adds the functions to the buttons

            let editButtons = document.getElementsByClassName("editButton");
            for (let button of editButtons) {
                button.addEventListener("click", editItem);
                button.style.visibility = "visible";
            }

            let deleteButtons = document.getElementsByClassName("deleteButton");
            for (let button of deleteButtons) {
                button.addEventListener("click", deleteItem);
                button.style.visibility = "visible";
            }

        });
    });

    }else {
        //The below code does the exact same as above apart from it will list all the items instead of only the ones in a location
        document.getElementById("ItemLocations").innerText = "Item Details in every locations";

        fetch('/Inventory/List', {method: 'get'}).then(response => response.json()).then(Item => {

            for (let ItemName of Item) {


                LocationHtml += `<tr class ="ItemRow">` +
                    `<td>${ItemName.id}</td>` +
                    `<td class="ItemName">${ItemName.Name}</td>` +
                    `<td>${ItemName.Price}</td>` +
                    `<td>${ItemName.Quantity}</td>` +
                    `<td class="last">` +
                    `<button class='editButton' data-id='${ItemName.id}'>Edit</button>` +
                    `<button class='deleteButton' data-id='${ItemName.id}'>Delete</button>` +
                    `</td>` +
                    `</tr>`;

            }

            LocationHtml += `</table>`;

            document.getElementById("listDiv").innerHTML = LocationHtml;

            let editButtons = document.getElementsByClassName("editButton");
            for (let button of editButtons) {
                button.addEventListener("click", editItem);
                button.style.visibility = "visible";
            }

            let deleteButtons = document.getElementsByClassName("deleteButton");
            for (let button of deleteButtons) {
                button.addEventListener("click", deleteItem);
                button.style.visibility = "visible";
            }

        });
    }


}


function addItem(){

    window.location.href = "http://localhost:8081/client/AddItem.html";
}

function editItem(){
    window.location.href = "http://localhost:8081/client/AddItem.html";

}

function index(){window.location.href = "http://localhost:8081/client/index.html";}

function deleteItem(event) {


    const ok = confirm("Are you sure?");

    if (ok === true) {

        let id = event.target.getAttribute("data-id");
        let formData = new FormData();
        formData.append("id", id);

        fetch('/Inventory/DeleteItem', {method: 'post', body: formData}
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


function SearchFunction(){

    let input = document.getElementById('searchbar').value;
    input=input.toLowerCase();
    let x = document.getElementsByClassName('ItemName');
    let y = document.getElementsByClassName('ItemRow');
    for (i = 0; i < x.length; i++) {
        if (!x[i].innerHTML.toLowerCase().includes(input)) {
            y[i].style.visibility="hidden";
        }
        else {
            y[i].style.visibility="visible";
        }
    }


}