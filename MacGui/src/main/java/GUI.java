import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class GUI {
    static final GUI game = new GUI();
    Stage stage;
    String ip;
    ChatClient client;
    Controller contr;

    private GUI(){}

    public static GUI getInstance(){
        return game;
    }

    public void createGUI(Stage primaryStage,String ip) throws IOException {
        this.stage=primaryStage;
        this.ip=ip;
        client = new ChatClient(ip);
        client.start();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = loader.load();
        contr = loader.getController();

        primaryStage.setTitle("Chinese checkers");
        primaryStage.setScene(new Scene(root,590,615));
        primaryStage.setResizable(false);
        primaryStage.show();
        System.out.println("showed");

        primaryStage.setOnCloseRequest(e ->{
            Platform.exit();
            System.exit(0);
        });
    }

    public Circle makeCircle(int x, int y, int k){
        Color[] colors = {Color.YELLOW,Color.BROWN,Color.RED,Color.GREEN,Color.BLUE,Color.WHITE,Color.BLACK};
        Color c;

        if(k>=0 || k<=5)
            c = colors[k];
        else
            c = colors[6];

        double yPix=300 - y*24.75;
        double xPix=300 + x*28.5833333 + y*(28.5833333/2);
        return (new Circle(xPix,yPix,8.5,c));
    }

    public ArrayList<Circle> decodeStrOfCircles(String s){
        String[] splited = s.split("\\s+");
        ArrayList<Circle> decodedCircles= new ArrayList<>();

        for(int i=1;i+2<splited.length;i=i+3)
        {
            int x=Integer.parseInt(splited[i]);
            int y=Integer.parseInt(splited[i+1]);
            int k=Integer.parseInt(splited[i+2]);
            decodedCircles.add(makeCircle(x,y,k));
        }
        return decodedCircles;
    }

    public void updateView(String s){
        contr.AddCircles(decodeStrOfCircles(s));
    }

    public void updateTurn(String s){
        String[] splited = s.split("\\s+");
        contr.infofield.setText(splited[1]);
    }

    public double PCtoGC_Y(double xPC,double yPC){
        return (300-yPC)/24.75;
    }

    public double PCtoGC_X(double xPC,double yPC){
        return (xPC-300-PCtoGC_Y(xPC,yPC)*(28.5833333/2))/28.5833333;
    }
}