package Server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.sqlite.SQLiteConfig;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    public static Connection db = null;

    public static void main(String[] args) {

        openDatabase("Inventory Database.db");

        ResourceConfig config = new ResourceConfig();
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
        }
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