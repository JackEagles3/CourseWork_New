package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("SalesOrder/")
public class SalesOrder_Controller {

    @GET
    @Path("ReadSaleOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public String ReadSaleOrder(){

        JSONArray list = new JSONArray();

        try{
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM [Sales Order]");


            //Outputs all the data from the database
            ResultSet results = ps.executeQuery();

            System.out.println("UserID,UserName,Password,RoleName");

            while (results.next()){

                JSONObject item = new JSONObject();
                item.put("SaleID",results.getInt(1));
                item.put("Date", results.getString(2));
                item.put("UserId",results.getInt(3));


                list.add(item);

            }

            return list.toString();

        }catch (Exception e){

            System.out.println("Database error:" + e);
            return "error";
        }

    }

    @POST
    @Path("AddSaleOrder")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String AddSaleOrder(@FormDataParam("SaleId") int id, @FormDataParam("Date") String Date, @FormDataParam("Userid") int UserId,@CookieParam("token") String token) {
        if (!LogIn_Controller.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO [Sales Order] (SaleID,Date,UserID) VALUES (?,?,?)");

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
    @Path("UpdateSalesOrder")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateSalesOrder(@FormDataParam("SaleId") int id, @FormDataParam("Date") String Date, @FormDataParam("Userid") int UserId,@CookieParam("token") String token) {
        if (!LogIn_Controller.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("UPDATE [Sales Order] SET  Date = ?, UserID = ? WHERE SaleID=?");

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




    public static void DeteleSalesOrder(int SaleID){


        try{

            //Lets you delete from the [Sales Order] table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM [Sales Order] WHERE SaleID = ?");

            ps.setInt(1,SaleID);

            ps.executeUpdate();


        }catch (Exception  e){
            System.out.println("Database error:" + e);
        }
    }

}
