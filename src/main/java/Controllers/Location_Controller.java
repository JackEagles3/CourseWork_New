package Controllers;


import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Location/")
public class Location_Controller {

    @GET
    @Path("ListLocations")
    @Produces(MediaType.APPLICATION_JSON)
    public String ListLocation() {

        System.out.println("Inventory/List");
        JSONArray list  = new JSONArray();
        try {
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Location");


            //Outputs all the data from the database
            ResultSet results = ps.executeQuery();

            System.out.println("Locations sent");
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id",results.getInt(1));
                item.put("Name",results.getString(2));

                list.add(item);



            }
            System.out.println("Success");
            return list.toString();

        } catch (Exception e) {

            System.out.println("Database error:" + e);
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";

        }
    }



    @POST
    @Path("AddLocation")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static void AddItem(@FormDataParam("Name") String name)
    {
        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Location (LocationName) VALUES (?)");


            //Sets the values of the columns

            ps.setString(1, name);



            ps.executeUpdate();


        } catch (Exception e) {
            System.out.println("Database error:" + e);
        }

    }

    @POST
    @Path("UpdateItem")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateItemDetails(@FormDataParam("Name") String name, @FormDataParam("id") int id) {

        try {

            //Lets you insert into the [Sales Order Details] table
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Location SET  LocationName = ? WHERE LocationId=?");

            ps.setString(1,name);
            ps.setInt(2, id);



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
