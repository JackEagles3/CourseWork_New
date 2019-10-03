package Controller;

import Server.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Inventory/")
public class Inventory_Controller {

    @GET
    @Path("List")
    @Produces(MediaType.APPLICATION_JSON)
    public static void ListAllInventory() {

        System.out.println("Inventory/List");
        JSONArray list  = new JSONArray();
        try {
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Inventory");


            JSONObject item  = new JSONObject();
            //Outputs all the data from the database
            ResultSet results = ps.executeQuery();

            System.out.println("ItemID,Name,Price,Location,Quantity,RoleName");
            while (results.next()) {
                item.put("id",results.getInt(1));
                item.put("Name", results.getString(2));
                item.put("Price",results.getDouble(3));
                item.put("Location", results.getString(4));
                item.put("Quantity", results.getString(5));
                item.put("RoleName",results.getInt(6));



            }


        } catch (Exception e) {

            System.out.println("Database error:" + e);

        }
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getThing(@PathParam("id") Integer id) throws Exception {
        if (id == null) {
            throw new Exception("Thing's 'id' is missing in the HTTP request's URL.");
        }
        System.out.println("thing/get/" + id);
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Name, Price, Location, Quantity FROM Inventory WHERE Id = ?");
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("id", id);
                item.put("name", results.getString(1));
                item.put("price", results.getString(2));
                item.put("location", results.getString(3));
                item.put("quantity" , results.getString(4));
            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }


    public static void AddItem(String ItemName, Double Price, String Location, int Quantity, String RoleName) {

        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Inventory (Name,Price,Location,Quantity,RoleName) VALUES (?,?,?,?,?)");


            //Sets the values of the columns

            ps.setString(1, ItemName);
            ps.setDouble(2, Price);
            ps.setString(3, Location);
            ps.setInt(4, Quantity);
            ps.setString(5, RoleName);


            ps.executeUpdate();


        } catch (Exception e) {
            System.out.println("Database error:" + e);
        }

    }


    public static void UpdateItemDetails(String UserName, int UserID, String Password, String RoleName) {

        try {

            //Lets you insert into the [Sales Order Details] table
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Inventory SET  Username = ?, Password = ?, RoleName = ? WHERE UserID=?");


            ps.executeUpdate();


        } catch (Exception e) {
            System.out.println("Database Update error:" + e);
        }

    }

    public static void DeteleItem(int ItemID) {
        try {

            //Lets you delete from the [Sales Order Details] table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Inventory WHERE ItemID = ?");

            ps.setInt(1, ItemID);


            ps.executeUpdate();


        } catch (Exception e) {
            System.out.println("Database error:" + e);
        }
    }

}

