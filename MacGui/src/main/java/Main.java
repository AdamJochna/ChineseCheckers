import javafx.stage.Stage;
import javafx.application.Application;
import java.net.DatagramSocket;
import java.io.IOException;
import java.net.InetAddress;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage){

        String ip="";

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
            System.out.println(ip);
        }
        catch (Exception e){}


        try{
            System.out.println("Connection ok");
            GUI.getInstance().createGUI(primaryStage,ip);
        }
        catch(IOException e){
            System.out.println("Connection error");
        }

    }

    public static void main(String[] args){
        launch(args);
    }
}
