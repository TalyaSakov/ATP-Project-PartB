
package View;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class HelpController extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception { this.stage = primaryStage; }

    public Pane pane;

    public void showHelp() throws Exception {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Image help =  new Image(new FileInputStream("resources/Images/help.jpg"));
        ImageView imageView = new ImageView(help);
        alert.setGraphic(imageView);
        alert.show();
    }

    public void close(){
        Button close = new Button("close");
        close.setOnAction(e -> stage.close()); //TODO: closes only in the secound time
    }



}