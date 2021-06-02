
package View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.Element;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AboutController {
    public Stage stage;
    public Stage getStage() { return stage; }
    public void setStage(Stage stage) { this.stage = stage; }

    public void aboutUs() throws FileNotFoundException {
        Stage AboutUs = new Stage();
        AboutUs.initModality(Modality.APPLICATION_MODAL);
        AboutUs.setTitle("About Us");
        AboutUs.setMinHeight(400);
        AboutUs.setMinWidth(300);
        javafx.scene.control.Label label = new javafx.scene.control.Label();
        javafx.scene.control.Button closeButton = new javafx.scene.control.Button("Close");
        closeButton.setOnAction(e -> AboutUs.close());

        Stage aboutYoni = new Stage();
        aboutYoni.initModality(Modality.APPLICATION_MODAL);
        aboutYoni.setTitle("Jonathan Pelah");
        aboutYoni.setMinHeight(250);
        aboutYoni.setMinWidth(250);
        String yoniDec= "This is yoni";

        FileInputStream input = new FileInputStream("src/Resources/Images/yoni.jpg");
        Image yoni = new Image(input);
        ImageView yoniPic = new ImageView(yoni);
        yoniPic.setFitWidth(120);
        yoniPic.setFitHeight(120);
        Button yoniB = new Button("", yoniPic);
        yoniB.setOnAction(e->popWindow(aboutYoni,yoniDec));


        Stage aboutTalya = new Stage();
        aboutTalya.initModality(Modality.APPLICATION_MODAL);
        aboutTalya.setTitle("Talya Sakov");
        aboutTalya.setMinHeight(250);
        aboutTalya.setMinWidth(250);
        String talyaDec= "Talya Sakov, 25 years old.\n"+"Leaves in Zichron Yaacov";

        FileInputStream input1 = new FileInputStream("src/Resources/Images/talya.jpg");
        Image talya = new Image(input1);
        ImageView talyaPic = new ImageView(talya);
        talyaPic.setFitWidth(120);
        talyaPic.setFitHeight(120);
        Button talyaB = new Button("", talyaPic);

        talyaB.setOnAction(e->popWindow(aboutTalya,talyaDec));
        VBox layout = new VBox(20);
        layout.getChildren().addAll(yoniB,talyaB,closeButton);
        layout.setAlignment(Pos.CENTER);



        Scene scene = new Scene(layout);
        AboutUs.setScene(scene);
        AboutUs.showAndWait();

    };
    public void aboutProject(){

        Stage aboutProj = new Stage();
        aboutProj.initModality(Modality.APPLICATION_MODAL);
        aboutProj.setTitle("About The Project");
        aboutProj.setMinHeight(300);
        aboutProj.setMinWidth(300);

        String aboutProjStr = "This game was created as part of the course\n" +
                "Advanced Topic In Programing\n" +
                "At the PartA, We built a maze based on a Prime algorithm.\n" +
                "Then,we solved it using algorithms: Breadth-first search, \n"+
                "Best-first search and depth-first search (here we use BFS to get an optimal solution).\n" +

                "At PartB , using Threads, we managed a server and client interface\n"+
                "and allowed multiple clients to access the maze.\n"+
                "We compress the maze in a decimal method\n" +
                "The transfer of information between the server and the client was performed by\n"+
                "compressing the maze in a decimal method.\n"+
                "©Jonathan Pelah & Talya Sakov" ;

        Label label = new Label();
        label.setText(aboutProjStr);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> aboutProj.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        aboutProj.setScene(scene);
        aboutProj.showAndWait();

    };

    public void popWindow(Stage stage1, String str){


        Label label = new Label();
        label.setText(str);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> stage1.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        stage1.setScene(scene);
        stage1.showAndWait();


    };

}