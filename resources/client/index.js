

function pageLoad() {

    let LocationHtml = '<table>' +
        '<tr>' +
        '<th>Id</th>' +
        '<th class="last">Name</th>' +

        '</tr>';

    fetch('/Location/ListLocations', {method: 'get'}).then(response => response.json()).then(Locations => {

        for (let LocationName of Locations){

            LocationHtml += `<tr>` +
                `<td>${LocationName.id}</td>` +
                `<td>${LocationName.Name}</td>`+
                `<td class="last">` +
                `<button class='editButton' date-id='${LocationName.id}'>Edit</button>`+
                `<button class='deleteButton' date-id='${LocationName.id}'>Delete</button>` +
                `</td>`+
                `</tr>`;

    }
        
        LocationHtml += `</table>`;

        document.getElementById("listDiv").innerHTML = LocationHtml;

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons){
            button.addEventListener("click", editLocation);
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons){
            button.addEventListener("click", deleteLocation);
        }
        
    });

    document.getElementById("saveButton").addEventListener("click", saveEditLocation);
    document.getElementById("saveButton").addEventListener("click", cancelEditLocation);
    
    
    
    
    }



    function editLocation(event){

        const id = event.target.getAttribute("data-id");

        if (id === null) {

            document.getElementById("editHeading").innerHTML = 'Add new location:';

            document.getElementById("fruitId").value = '';
            document.getElementById("fruitName").value = '';

            document.getElementById("listDiv").style.display = 'none';
            document.getElementById("editDiv").style.display = 'block';

        } else {

            fetch('/Location/AddLocation/' + id, {method: 'get'}
            ).then(response => response.json()
            ).then(fruit => {

                if (fruit.hasOwnProperty('error')) {
                    alert(fruit.error);
                } else {

                    document.getElementById("editHeading").innerHTML = 'Editing ' + fruit.name + ':';

                    document.getElementById("fruitId").value = id;
                    document.getElementById("fruitName").value = fruit.name;


                }
            });
        }
        }

    function deleteLocation() {

    }







