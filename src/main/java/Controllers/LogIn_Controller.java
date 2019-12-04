package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;


@Path("LogIn/")
public class LogIn_Controller {

    @GET
    @Path("ShowUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public String ShowUsers(){

        JSONArray list = new JSONArray();

        try{
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM LogIn");


            //Outputs all the data from the database
            ResultSet results = ps.executeQuery();

            System.out.println("UserID,UserName,Password,RoleName");

            while (results.next()){

                JSONObject item = new JSONObject();
                item.put("UserID",results.getInt(1));
                item.put("UserName", results.getString(2));
                item.put("Password",results.getString(3));
                item.put("RoleName", results.getString(4));

                list.add(item);

            }

            return list.toString();

        }catch (Exception e){

            System.out.println("Database error:" + e);
            return "error";
        }

    }

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public  String  login(@FormDataParam("UserName") String username, @FormDataParam("Password") String password){

        try{
            //Selects all data from the database
            PreparedStatement ps = Main.db.prepareStatement("SELECT Password FROM LogIn WHERE UserName = ?");

            ps.setString(1,username);

            ResultSet result = ps.executeQuery();
            //Outputs all the data from the database
            if (result.next()){

                String correctPass = result.getString(1);

                if (password.equals(correctPass)){

                    String token = UUID.randomUUID().toString();

                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE LogIn SET Token = ? where UserName = ?");
                    ps2.setString(1,token);
                    ps2.setString(2,username);
                    ps2.executeUpdate();

                    JSONObject user = new JSONObject();
                    user.put("username", username);
                    user.put("token", token);

                    return user.toString();


                }else{
                    return "{\"error\": \"Incorect password!\"}";
                }


            }else{
                return "{\"error\": \"Inccorect User!\"}";
            }





        }catch (Exception e){
            System.out.println("Database error:" + e);
            return "{\"status\": \"Error\"}";


        }

    }

    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutUser(@CookieParam("token") String token) {

        try {

            System.out.println("user/logout");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT UserName FROM LogIn WHERE Token = ?");
            ps1.setString(1, token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {

                int id = logoutResults.getInt(1);

                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Login SET Token = NULL WHERE UserID = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                return "{\"status\": \"OK\"}";
            } else {

                return "{\"error\": \"Invalid token!\"}";

            }

        } catch (Exception exception){
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }

    }

    public static boolean validToken(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM LogIn WHERE Token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch (Exception exception) {
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return false;
        }
    }



    @POST
    @Path("AddUser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String AddUser(@FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password, @FormDataParam("RoleName") String RoleName) {

        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO LogIn (UserName, Password, RoleName) VALUES (?,?,?)");

            //Sets the values of the columns

            ps.setString(1, UserName);
            ps.setString(2, Password);
            ps.setString(3, RoleName);
            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";
        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }



    @POST
    @Path("UpdateUser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateUser(@FormDataParam("id") int UserId, @FormDataParam("Password") String Password) {

        try {

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("UPDATE LogIn SET  Password = ? WHERE UserID=?");

            //Sets the values of the columns

            ps.setInt(2, UserId);
            ps.setString(1, Password);

            ps.executeUpdate();
            return "{\"error\": \"Ok\"}";
        } catch (Exception e) {
            System.out.println("Database error:" + e);
            return "{\"error\": \"Error\"}";
        }
    }


    /** @noinspection unused*/
    public static void UpdateUser(String Password, int UserId ){

        try{

            //Lets you insert into the Login table
            PreparedStatement ps = Main.db.prepareStatement("UPDATE LogIn SET  Password = ? WHERE UserID=?");


            ps.setString(1, Password);
            ps.setInt(2, UserId);

            ps.executeUpdate();


        }catch (Exception  e){
            System.out.println("Database Update error:" + e);
        }

    }

    public static void DeleteUser(int UserID){
        try{

            //Lets you delete from the Login table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM LogIn WHERE UserID = ?");


            ps.setInt(1,UserID);
            ps.executeUpdate();


        }catch (Exception  e){
            System.out.println("Database error:" + e);
        }
    }







}
