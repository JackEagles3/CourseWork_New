package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Path("PurchaseOrderDetails/")

public class PurchaseOrderDetails_Controller {


    @GET
    @Path("ReadPurchaseOrderDetails/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String ReadPurchaseOrderDetails(@PathParam("id") Integer id){

        JSONArray list = new JSONArray();

        try{
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM [Purchase Orders detail] Where PurchaseID = ?");
            ps.setInt(1, id);


            //Outputs all the data from the database
            ResultSet results = ps.executeQuery();

            System.out.println("UserID,UserName,Password,RoleName");

            while (results.next()){
                PreparedStatement ps2 = Main.db.prepareStatement("SELECT Name FROM Inventory WHERE ItemID = ?");
                ps2.setInt(1,results.getInt(2));
                ResultSet Name = ps2.executeQuery();

                JSONObject item = new JSONObject();
                item.put("PurchaseId",results.getInt(1));
                item.put("ItemID", Name.getInt(1));
                item.put("Quantity",results.getInt(3));
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
    @Path("AddPurchaseOrderDetail")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String AddPurchaseOrder(@FormDataParam("PurchaseId") int id, @FormDataParam("ItemId") int item, @FormDataParam("Quantity") int quantity, @FormDataParam("Price") int price,@CookieParam("token") String token) {
        if (!LogIn_Controller.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO [Purchase Orders detail] (PurchaseID,ItemID, Quantity, [Unit Price]) VALUES (?,?,?,?)");

            //Sets the values of the columns

            ps.setInt(1, id);
            ps.setInt(2, item);
            ps.setInt(3, quantity);
            ps.setInt(4, price);

            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";

        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }

    @POST
    @Path("UpdatePurchasesOrderDetails")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdatePurchasesOrderDetails(@CookieParam("token") String token,@FormDataParam("PurchaseId") int id, @FormDataParam("ItemId") int item, @FormDataParam("Quantity") int quantity, @FormDataParam("Price") int price) {
        if (!LogIn_Controller.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("UPDATE [Purchases Order Details] SET  ItemID=?, Quantity=?, Unit Price=? WHERE PurchaseID=?");

            //Sets the values of the columns

            ps.setInt(4, id);
            ps.setInt(2, item);
            ps.setInt(3, quantity);
            ps.setInt(1, price);

            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";

        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }

    @POST
    @Path("DeletePurchasesOrderDetails")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeletePurchasesOrderDetails(@FormDataParam("id") int PurchaseID, @FormDataParam("Itemid") int ItemID,@CookieParam("token") String token){
        if (!LogIn_Controller.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try{

            //Lets you delete from the [Purchases Order Details] table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM [Purchases Order Details] WHERE PurchaseID = ? and ItemID = ? ");

            ps.setInt(1,PurchaseID);
            ps.setInt(1,ItemID);

            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";

        }catch (Exception  e){
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }


}

