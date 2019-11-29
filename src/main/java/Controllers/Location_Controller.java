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
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLocation(@PathParam("id") Integer id) throws Exception {
        if (id == null) {
            throw new Exception("Thing's 'id' is missing in the HTTP request's URL.");
        }
        System.out.println("get/" + id);
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT LocationName FROM Location WHERE LocationId = ?");
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("id", id);
                item.put("name", results.getString(1));

            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }




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
    public String AddItem(@FormDataParam("Name") String name)
    {
        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Location (LocationName) VALUES (?)");


            //Sets the values of the columns

            ps.setString(1, name);



            ps.executeUpdate();

            return "{\"status\": \"OK\"}";


        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"OK\"}";
        }

    }

    @POST
    @Path("UpdateLocation")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateLocation(@FormDataParam("Name") String name, @FormDataParam("id") int id) {

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
    @Path("DeleteLocation")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteLocation(@FormDataParam("id") int Location) {
        try {

            //Lets you delete from the [Sales Order Details] table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Location WHERE Locationid = ?");

            ps.setInt(1, Location);


            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }



}
