
function pageLoad() {

    let now = new Date();

    let Html = '<div style=  "text-align:center;"> '
                + '<h1>Welcome to my Inventory System! </h1>'
                + 'This was loaded at' + now.toLocaleDateString()
                + '</div>'
                + '</div>'

    document.getElementById("Test").innerHTML = Html;

}