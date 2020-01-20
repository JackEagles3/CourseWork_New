package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Path("Supplier/")
public class Supplier_Controller {



    @GET
    @Path("ReadSupplier")
    @Produces(MediaType.APPLICATION_JSON)
    public String ReadSupplier(){

        JSONArray list = new JSONArray();

        try{
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM [Sales Order Details]");


            //Outputs all the data from the database
            ResultSet results = ps.executeQuery();

            System.out.println("UserID,UserName,Password,RoleName");

            while (results.next()){

                JSONObject item = new JSONObject();
                item.put("SupplierId",results.getInt(1));
                item.put("SupplierName", results.getString(2));
                item.put("ItemID",results.getInt(3));
                item.put("Price",results.getDouble(4));

                list.add(item);

            }

            return list.toString();

        }catch (Exception e){

            System.out.println("Database error:" + e);
            return "error";
        }

    }

    @POST
    @Path("AddSupplier")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String AddSupplier(@FormDataParam("SupplierId") int SupplierId, @FormDataParam("SupplierName") String SupplierName, @FormDataParam("ItemID") int ItemID, @FormDataParam("price") double price, @CookieParam("token") String token) {

        if (!LogIn_Controller.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO [Supplier] (SupplierID,SupplierName,ItemID,price) VALUES (?,?,?,?)");

            //Sets the values of the columns

            ps.setInt(1, SupplierId);
            ps.setString(2, SupplierName);
            ps.setInt(3, ItemID);
            ps.setDouble(4, price);


            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";

        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }

    @POST
    @Path("DeleteSupplier")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteSupplier(@FormDataParam("id") int SupplierID, @CookieParam("token") String token){

        try{

            //Lets you delete from the [Supplier] table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM [Supplier] WHERE SupplierID = ?");


            ps.setInt(1,SupplierID);
            ps.executeUpdate();

            return "{\"error\": \"Ok\"}";
        }catch (Exception  e){
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }

}
