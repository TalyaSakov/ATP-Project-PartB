
package View;

import Server.Configurations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    public Stage stage;
    public TextField numOfThreads;
    public String poolSize;
    public ChoiceBox<String> searchingAlgorithm;
    public ChoiceBox<String> mazeGenerator;
    public boolean hasChanged=false;

    private String algorithmString;
    private String generatorString;
    private int threadNum;

    public PropertiesController() { }

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> generatorsViewrs=FXCollections.observableArrayList( "myMazeGenerator","simpleMazeGenerator");
        mazeGenerator.setItems(generatorsViewrs);
        String currGenerator = Configurations.getInstance().mazeGeneratingAlgorithm().getClass().getName();
        this.mazeGenerator.setValue(currGenerator);

        ObservableList<String> algoViewrs=FXCollections.observableArrayList( "BreadthFirstSearch","DepthFirstSearch", "BestFirstSearch");
        searchingAlgorithm.setItems(algoViewrs);
        String currSearchAlgo = Configurations.getInstance().mazeSearchingAlgorithm().getName();
        this.searchingAlgorithm.setValue(currSearchAlgo);


        int currPoolSize = Configurations.getInstance().threadPoolSize();
        this.poolSize = String.valueOf(currPoolSize);
        this.threadNum=Integer.valueOf(this.poolSize);


    }
        public void UpdateClicked () throws IOException {

            System.out.println("Properties: saveChanges");
            algorithmString = searchingAlgorithm.getValue();
            generatorString = mazeGenerator.getValue();
            this.poolSize=this.numOfThreads.getText();
            threadNum =  Integer.valueOf(poolSize);
            hasChanged=true;

            Properties prop = new Properties();
            InputStream inputStream = Configurations.class.getClassLoader().getResourceAsStream("src/Resources/config.properties");
            
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

           prop.setProperty("mazeGenerator",generatorString);
           prop.setProperty("searchingAlgorithm",algorithmString);
           prop.setProperty("threadPoolSize",String.valueOf(poolSize));



           FileOutputStream fos = new FileOutputStream("src/Resources/config.properties");
           prop.store(fos,null);

           
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("success");
            window.setMinWidth(250);
            window.setMinHeight(350);

            Label label = new Label();
            label.setText("Changing settings completed successfully.\n"+"Please restart the program in order to take effect. \n" + "You can see the current state of settings \n"+ "in the \"show properties\" button. ");
            Button closeButton = new Button("Close this window");
            closeButton.setOnAction(e -> window.close());

            VBox layout = new VBox(20);
            layout.getChildren().addAll(label, closeButton);
            layout.setAlignment(Pos.CENTER);

            //Display window and wait for it to be closed before returning
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();


        }

    public void showProp () {
        String str="";
        Properties prop = new Properties();
        InputStream input = Configurations.class.getClassLoader().getResourceAsStream("src/Resources/config.properties");
        // load a properties file
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (hasChanged)
        {

             str = "Number of threads for each server = ";
            algorithmString = searchingAlgorithm.getValue();
            generatorString = mazeGenerator.getValue();
            this.poolSize = this.numOfThreads.getText();
            threadNum = Integer.valueOf(poolSize);
            str += threadNum;
            str += "\n";
            str += "Generating algorithm = ";
            str += generatorString;
            str += "\n";
            str += "Searching algorithm = ";
            str += algorithmString;
        }
        else
        {
            str = "Number of threads for each server = ";
        str += prop.getProperty("threadPoolSize");
        str += "\n";
        str += "Generating algorithm = ";
        str += prop.getProperty("mazeGenerator");
        str += "\n";
        str += "Searching algorithm = ";
        str +=prop.getProperty("searchingAlgorithm");
        }

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("maze properties");
        window.setMinWidth(300);
        window.setMinHeight(300);

        Label label = new Label();
        label.setText(str);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();


}}
