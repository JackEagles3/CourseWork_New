function pageLoad(){

    fetch('/Location/ListLocations', {method: 'get'}).then(response => response.json()).then(Locations => { //Calls the API method for listing locations via the server

        let LocationsHtml = `<label >Price: </label>` +
            `<select name="Locations" id="Locations">`;


        for (let LocationName of Locations) {//Goes through all of the locations and adds all the details to the table rows
            LocationsHtml += `<option value="Location">${LocationName.Name}</option>`;
        }

        LocationsHtml += `</select>`;
        document.getElementById("Locations").innerHTML = LocationsHtml;
    });


    document.getElementById("AddItemButton").addEventListener("click", AddItem);


}

function AddItem(event) {
    debugger;
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
