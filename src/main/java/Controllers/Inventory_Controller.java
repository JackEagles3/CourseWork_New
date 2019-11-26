package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Inventory/")
public class Inventory_Controller {

    @GET
    @Path("List")
    @Produces(MediaType.APPLICATION_JSON)
    public String ListAllInventory() {

        System.out.println("Inventory/List");
        JSONArray list  = new JSONArray();
        try {
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Inventory");


            //Outputs all the data from the database
            ResultSet results = ps.executeQuery();

            System.out.println("ItemID,Name,Price,Location,Quantity,RoleName");
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id",results.getInt(1));
                item.put("Name", results.getString(2));
                item.put("Price",results.getDouble(3));
                item.put("Location", results.getString(4));
                item.put("Quantity", results.getString(5));
                item.put("RoleName",results.getInt(6));
                list.add(item);



            }
            System.out.println("Success");
            return list.toString();

        } catch (Exception e) {

            System.out.println("Database error:" + e);
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";

        }
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getThing(@PathParam("id") Integer id) throws Exception {
        if (id == null) {
            throw new Exception("Thing's 'id' is missing in the HTTP request's URL.");
        }
        System.out.println("get/" + id);
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


    @POST
    @Path("AddItem")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static void AddItem(@FormDataParam("ItemName") String ItemName, @FormDataParam("Price") Double Price, @FormDataParam("Location") String Location, @FormDataParam("Quantity") int Quantity, @FormDataParam("RoleName") String RoleName) {

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

    @POST
    @Path("UpdateItem")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateItemDetails(@FormDataParam("ItemName") String ItemName, @FormDataParam("Price") double Price, @FormDataParam("Location") String Location, @FormDataParam("Quantity") int Quantity, @FormDataParam("RoleName") String RoleName, @FormDataParam("ItemId") int ItemId) {

        try {

            //Lets you insert into the [Sales Order Details] table
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Inventory SET  ItemName = ?, Price = ?, Location = ?, Quantity = ?, RoleName = ? WHERE ItemId=?");

            ps.setString(1,ItemName);
            ps.setDouble(2,Price);
            ps.setString(3,Location);
            ps.setInt(4, Quantity);
            ps.setString(5, RoleName);
            ps.setInt(6, ItemId);


            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception e) {
            System.out.println("Database Update error:" + e);
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";

        }

    }

    @POST
    @Path("DeleteItem")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeteleItem(@FormDataParam("id") int ItemID) {
        try {

            //Lets you delete from the [Sales Order Details] table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Inventory WHERE ItemID = ?");

            ps.setInt(1, ItemID);


            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

}

