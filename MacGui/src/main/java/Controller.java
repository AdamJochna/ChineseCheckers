import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Controller {
    
    public AnchorPane pane;
    public TextField nickfield;
    public TextField infofield;
    public ImageView background;
    public Button JoinGame;

    GUI game=GUI.getInstance();
    int tempxGC;
    int tempyGC;

    public void AddCircles(ArrayList<Circle> list){
        Platform.runLater(() ->{
            pane.getChildren().clear();
            pane.getChildren().add(background);
            pane.getChildren().addAll(list);

        });
    }

    public void MousePress(MouseEvent mouseEvent){
        double xPC=mouseEvent.getX();
        double yPC=mouseEvent.getY();
        xPC=game.PCtoGC_X(xPC,yPC);
        yPC=game.PCtoGC_Y(xPC,yPC);
        tempxGC = (int) Math.round(xPC);
        tempyGC = (int) Math.round(yPC);
    }

    public void MouseRelease(MouseEvent mouseEvent){
        double xPC=mouseEvent.getX();
        double yPC=mouseEvent.getY();
        xPC=game.PCtoGC_X(xPC,yPC);
        yPC=game.PCtoGC_Y(xPC,yPC);
        int newxGC = (int) Math.round(xPC);
        int newyGC = (int) Math.round(yPC);

        game.client.sendMove(tempxGC,tempyGC,newxGC,newyGC);
    }

    public void clickJG(ActionEvent event){
        String text=nickfield.getText();
        if(text!=null && text.length()>0)
        {
            game.client.setNick(text);
            nickfield.setEditable(false);
            nickfield.setStyle("-fx-text-inner-color: grey");
        }
    }
}

