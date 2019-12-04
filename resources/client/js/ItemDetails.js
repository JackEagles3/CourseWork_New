function ToIndexPage(){
    window.location.href = "http://localhost:8081/client/index.html";
}

function editItem() {


}

function pageLoad(){
    debugger;
    let LocationHtml = '<table>' +
        '<tr>' +
        '<th>Id</th>' +
        '<th class="last">Name</th>' +

        '</tr>';


    let id = window.location.search;
    id = id.substring(10, id.length);
    if (id != "LocationName"){
    fetch('/Location/get/' + id, {method: 'get'}
    ).then(response => response.json()
    ).then(location => {

        document.getElementById("ItemLocations").innerText += " "+ location.name;

        fetch('/Inventory/getLocation/' + location.id, {method: 'get'}).then(response => response.json()).then(Item => {

            for (let ItemName of Item) {


                LocationHtml += `<tr>` +
                    `<td>${ItemName.id}</td>` +
                    `<td>${ItemName.name}</td>` +
                    `<td class="last">` +
                    `<button class='editButton' data-id='${ItemName.id}'>Edit</button>  ` +
                    `<button class='deleteButton' data-id='${ItemName.id}'>Delete</button>  ` +
                    `</td>` +
                    `</tr>`;

            }

            LocationHtml += `</table>`;

            document.getElementById("listDiv").innerHTML = LocationHtml;

            let editButtons = document.getElementsByClassName("editButton");
            for (let button of editButtons) {
                button.addEventListener("click", editItem);
            }

            let deleteButtons = document.getElementsByClassName("deleteButton");
            for (let button of deleteButtons) {
                button.addEventListener("click", deleteItem);
            }

        });
    });

    }else {

        document.getElementById("ItemLocations").innerText += " all locations";

        fetch('/Inventory/List', {method: 'get'}).then(response => response.json()).then(Item => {

            for (let ItemName of Item) {


                LocationHtml += `<tr>` +
                    `<td>${ItemName.id}</td>` +
                    `<td>${ItemName.Name}</td>` +
                    `<td class="last">` +
                    `<button class='editButton' data-id='${ItemName.id}'>Edit</button>  ` +
                    `<button class='deleteButton' data-id='${ItemName.id}'>Delete</button>  ` +
                    `</td>` +
                    `</tr>`;

            }

            LocationHtml += `</table>`;

            document.getElementById("listDiv").innerHTML = LocationHtml;

            let editButtons = document.getElementsByClassName("editButton");
            for (let button of editButtons) {
                button.addEventListener("click", editItem);
            }

            let deleteButtons = document.getElementsByClassName("deleteButton");
            for (let button of deleteButtons) {
                button.addEventListener("click", deleteItem);
            }

        });
    }


}

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