
package View;

import Server.Configurations;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PropertiesController {
    public Stage stage;
    public TextField numOfThreads;
    public String poolSize;
    public ChoiceBox<String> searchingAlgorithm;
    public ChoiceBox<String> generator;

    public PropertiesController() {
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("resources/config.properties"));
            Properties prop = new Properties();
            InputStream input = Configurations.class.getClassLoader().getResourceAsStream("src/Resources/config.properties");

            try {
                prop.load(input);
            } catch (IOException var8) {
                var8.printStackTrace();
            }

            this.poolSize = this.numOfThreads.getText();
            String a1 = properties.getProperty("searchingAlgorithm");
            String a2 = properties.getProperty("generator");
            if (a1.equals("BestFirstSearch")) {
                this.searchingAlgorithm.setValue("BestFirstSearch");
            } else if (a1.equals("DepthFirstSearch")) {
                this.searchingAlgorithm.setValue("DepthFirstSearch");
            } else if (a1.equals("BreadthFirstSearch")) {
                this.searchingAlgorithm.setValue("BreadthFirstSearch");
            }

            if (a2.equals("MyMazeGenerator")) {
                this.generator.setValue("MyMazeGenerator");
            } else if (a2.equals("SimpleMazeGenerator")) {
                this.generator.setValue("SimpleMazeGenerator");
            } else if (a2.equals("EmptyMazeGenerator")) {
                this.generator.setValue("EmptyMazeGenerator");
            }

            if (properties.getProperty("searchingAlgorithm").equals("BestFirstSearch")) {
                this.searchingAlgorithm.setValue("BestFirstSearch");
            } else if (properties.getProperty("searchingAlgorithm").equals("DepthFirstSearch")) {
                this.searchingAlgorithm.setValue("DepthFirstSearch");
            } else if (properties.getProperty("searchingAlgorithm").equals("BreadthFirstSearch")) {
                this.searchingAlgorithm.setValue("BreadthFirstSearch");
            }

            if (properties.getProperty("generator").equals("MyMazeGenerator")) {
                this.generator.setValue("MyMazeGenerator");
            }

            if (properties.getProperty("generator").equals("SimpleMazeGenerator")) {
                this.generator.setValue("SimpleMazeGenerator");
            }

            prop.setProperty("mazeGenerator", (String)this.generator.getValue());
            System.out.println("maze genertorrrrr" + Configurations.getInstance().mazeGeneratingAlgorithm().getClass().getName());
            prop.setProperty("searchingAlgorithm", (String)this.searchingAlgorithm.getValue());
            System.out.println("serchinggggg" + Configurations.getInstance().mazeSearchingAlgorithm().getName());
            prop.setProperty("threadPoolSize", this.poolSize);
        } catch (FileNotFoundException var9) {
            var9.printStackTrace();
        } catch (IOException var10) {
            var10.printStackTrace();
        }

    }

    public void UpdateClicked() {
        Configurations.getInstance().setMazeGeneratingAlgorithm((String)this.generator.getValue());
        Configurations.getInstance().setMazeSearchingAlgorithm((String)this.searchingAlgorithm.getValue());
    }

    public void submit() throws FileNotFoundException {
    }
}