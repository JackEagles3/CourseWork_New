

function pageLoad() {

    let LocationHtml = '<table>' +
        '<tr>' +
        '<th>Id</th>' +
        '<th class="last">Name</th>' +

        '</tr>';

    fetch('/Location/ListLocations', {method: 'get'}).then(response => response.json()).then(Locations => {

        for (let LocationName of Locations) {

            LocationHtml += `<tr>` +
                `<td>${LocationName.id}</td>` +
                `<td>${LocationName.Name}</td>` +
                `<td class="last">` +
                `<button class='editButton' data-id='${LocationName.id}'>Edit</button>` +
                `<button class='deleteButton' data-id='${LocationName.id}'>Delete</button>` +
                `</td>` +
                `</tr>`;

        }

        LocationHtml += `</table>`;

        document.getElementById("listDiv").innerHTML = LocationHtml;
        document.getElementById("LocationName").innerText = '';

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.addEventListener("click", editLocation);
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.addEventListener("click", deleteLocation);
        }

    });

    document.getElementById("saveButton").addEventListener("click", saveEditLocation);


    document.getElementById("cancelButton").addEventListener("click", cancelEditLocation);

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

        console.log("I am about to SAVE!");

        event.preventDefault();

        if (document.getElementById("LocationName").value.trim() === '') {
            alert("Please provide a Location name.");
            return;
        }


        const id = document.getElementById("LocationId").value;
        const form = document.getElementById("LocationForm");
        const formData = new FormData(form);

        let apiPath = '';
        if (id === '') {
            apiPath = '/Location/AddLocation';
        } else {
            apiPath = '/Location/UpdateLocation';
        }

        fetch(apiPath, {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            }

            pageLoad();
        });



}


    function cancelEditLocation(event) {

        event.preventDefault();

        document.getElementById("editHeading").innerHTML = 'Add New Location:';

        document.getElementById("LocationId").value = '';
        document.getElementById("LocationName").value = '';


    }




