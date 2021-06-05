
package View;

import Server.Configurations;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PropertiesController implements Initializable {
    public Stage stage;
    public TextField numOfThreads;
    public String poolSize;
    public ChoiceBox<String> searchingAlgorithm;
    public ChoiceBox<String> mazeGenerator;

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
        String currGenerator ="myMazeGenerator";// Configurations.getInstance().mazeGeneratingAlgorithm().getClass().getName();
        this.mazeGenerator.setValue(currGenerator);

        ObservableList<String> algoViewrs=FXCollections.observableArrayList( "BreadthFirstSearch","DepthFirstSearch", "BestFirstSearch");
        searchingAlgorithm.setItems(algoViewrs);
        String currSearchAlgo = Configurations.getInstance().mazeSearchingAlgorithm().getName();
        this.searchingAlgorithm.setValue(currSearchAlgo);

        int currPoolSize = Configurations.getInstance().threadPoolSize();
        this.poolSize = String.valueOf(currPoolSize);



    }
        public void UpdateClicked () {
            System.out.println("Properties: saveChanges");
            algorithmString = searchingAlgorithm.getValue();
            generatorString = mazeGenerator.getValue();
            this.poolSize=this.numOfThreads.getText();
            threadNum =  Integer.valueOf(poolSize);


            Configurations.getInstance().setMazeGeneratingAlgorithm(generatorString);
            Configurations.getInstance().setMazeSearchingAlgorithm(algorithmString);
            Configurations.getInstance().setThreadPoolSize(threadNum);
            Configurations.start();

            System.out.println(generatorString);
            System.out.println(algorithmString);
            System.out.println(threadNum);
            stage.close();
        }


}
