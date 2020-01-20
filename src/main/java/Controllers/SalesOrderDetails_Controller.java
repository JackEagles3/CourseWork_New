package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SalesOrderDetails_Controller {



    @GET
    @Path("ReadSalesOrderDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public String ReadSalesOrderDetails(){

        JSONArray list = new JSONArray();

        try{
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM [Sales Order Details]");


            //Outputs all the data from the database
            ResultSet results = ps.executeQuery();

            System.out.println("UserID,UserName,Password,RoleName");

            while (results.next()){

                JSONObject item = new JSONObject();
                item.put("SaleID",results.getInt(1));
                item.put("ItemID", results.getString(2));
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
    @Path("AddSaleOrderDetail")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String AddSaleOrder(@FormDataParam("SaleId") int id, @FormDataParam("Itemid") int Item, @FormDataParam("Quantity") int quantity, @FormDataParam("Price") double price,@CookieParam("token") String token) {
        if (!LogIn_Controller.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO [Sales Order] (SaleID,Date,UserID) VALUES (?,?,?)");

            //Sets the values of the columns

            ps.setInt(1, id);
            ps.setInt(2, Item);
            ps.setInt(3, quantity);
            ps.setDouble(4,price);

            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";

        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }



    @POST
    @Path("UpdateSalesOrderDetails")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateSalesOrderDetails(@FormDataParam("SaleId") int id, @FormDataParam("Itemid") int Item, @FormDataParam("Quantity") int quantity, @FormDataParam("Price") double price,@CookieParam("token") String token) {
        if (!LogIn_Controller.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("UPDATE [Sales Order Details] SET  ItemID=?, Quantity=?, Unit Price=? WHERE SaleID=?");

            //Sets the values of the columns

            ps.setInt(4, id);
            ps.setInt(2, Item);
            ps.setInt(3, quantity);
            ps.setDouble(1,price);

            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";

        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }




    @POST
    @Path("DeleteSalesOrderDetails")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteSalesOrderDetails(@FormDataParam("id") int SaleID,@FormDataParam("Itemid") int ItemID, @CookieParam("token") String token){

        try{

            //Lets you delete from the [Sales Order Details] table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM [Sales Order Details] WHERE SaleID = ? and ItemID = ?");

            ps.setInt(1,SaleID);
            ps.setInt(2,ItemID);

            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        }catch (Exception  e){
            System.out.println("Database error:" + e);
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }


}
