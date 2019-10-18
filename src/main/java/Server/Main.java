package Server;

import Controller.PurchaseOrder_Controller;
import Controller.SalesOrderDetails_Controller;
import Controller.SalesOrder_Controller;
import Controller.Supplier_Controller;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.sqlite.SQLiteConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.function.Supplier;


public class Main {

    public static Connection db = null;

    public static void main(String[] args) {
        // Opens Database
        openDatabase("Inventory Database.db");
        //Code for manipulating the data in the database

        /*ResourceConfig config = new ResourceConfig();
        config.packages("Controllers");
        config.register(MultiPartFeature.class);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            System.out.println("Server successfully started.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        PurchaseOrder_Controller.ReadPurchaseOrder();
        PurchaseOrder_Controller.AddPurchaseOrder(1,"11/12/19",2,1);
        Supplier_Controller.DeteleSalesOrderDetails(1);
        SalesOrderDetails_Controller.UpdateSalesOrderDetails(1,2,12,10.00);

        closeDatabase();
    }



 private static void openDatabase(String dbFile){
        //Creates a connection to the database
        try{
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Successful Connection");
        //Catches any errors from connecting to the database
        }catch (Exception e) {
            System.out.println("Connection error: " + e);
        }
    }
    private static void closeDatabase(){
        //Disconnects the database
        try{
            db.close();
            System.out.println("Successful Disconnection");
        //Catches any errors from disconnecting the database
        }catch (Exception e){
            System.out.println("Disconnection error: " + e);
        }
    }
}