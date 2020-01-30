package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
@Path("PurchaseOrder/")
public class PurchaseOrder_Controller {


    @GET
    @Path("Read")
    @Produces(MediaType.APPLICATION_JSON)
    public String ReadPurchaseOrder(){

        JSONArray list = new JSONArray();

        try{
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM [Purchase Order]");


            //Outputs all the data from the database
            ResultSet results = ps.executeQuery();

            while (results.next()){

                JSONObject item = new JSONObject();
                item.put("id",results.getInt(1));
                item.put("Date", results.getString(2));
                item.put("UserID",results.getInt(3));
                item.put("SupplierId", results.getString(4));

                list.add(item);

            }

            return list.toString();

        }catch (Exception e){

            System.out.println("Database error:" + e);
            return "error";
        }

    }

    @POST
    @Path("AddPurchaseOrder")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String AddPurchaseOrder(@CookieParam("UserId") int UserId, @FormDataParam("Supplier") int SupplierId) {

        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO [Purchase Order] (Date,UserID,SupplierID) VALUES (?,?,?)");

            //Sets the values of the columns
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime now = LocalDateTime.now();

            ps.setString(1, dtf.format(now));
            ps.setInt(2, UserId);
            ps.setInt(3, SupplierId);

            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";
        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }

    @POST
    @Path("UpdatePurchaseOrder")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdatePurchaseOrder(@FormDataParam("PurchaseId") int id, @FormDataParam("Date") String Date, @FormDataParam("UserId") int UserId) {

        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO [Purchase Order] (PurchaseID,Date,UserID,SupplierID) VALUES (?,?,?,?)");

            //Sets the values of the columns

            ps.setInt(1, id);
            ps.setString(2, Date);
            ps.setInt(3, UserId);


            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";
        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }




    @POST
    @Path("DeletePurchaseOrder")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DetelePurchaseOrder(@FormDataParam("id") int PurchaseID, @CookieParam("token") String token){
        try{

            //Lets you delete from the [Purchase Order] table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM [Purchase Order] WHERE PurchaseID = ?");

            ps.setInt(1,PurchaseID);

            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        }catch (Exception  e){
            System.out.println("Database error:" + e);
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

}
