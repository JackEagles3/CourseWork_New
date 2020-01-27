function pageLoad(){


    if (Cookies.get("Item") === "-1") {
        fetch('/Location/ListLocations', {method: 'get'}).then(response => response.json()).then(Locations => { //Calls the API method for listing locations via the server

            let LocationsHtml = `<label for="Location" form="ItemDetailsForm">Price: </label>` +
                `<select name="Location" id="Locations" form="ItemDetailsForm">`;


            for (let LocationName of Locations) {//Goes through all of the locations and adds all the details to the table rows
                LocationsHtml += `<option value=${LocationName.id} id="Location">${LocationName.Name}</option>`;
            }

            LocationsHtml += `</select>`;
            document.getElementById("Locations").innerHTML = LocationsHtml;
        });


        document.getElementById("AddItemButton").addEventListener("click", AddItem);

    }
}

function AddItem(event) {

    event.preventDefault();
    const form = document.getElementById("ItemDetailsForm");
    const formData = new FormData(form);



    fetch('/Inventory/AddItem', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        alert("Yes");
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }


    });

}

function index(){window.location.href = "http://localhost:8081/client/index.html";}