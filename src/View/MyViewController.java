package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;

public class MyViewController implements Initializable, Observer {

    private MyViewModel myViewModel;
    private Maze maze;


    @FXML
    public Label lbl_player_row;
    @FXML
    public Label lbl_player_column;
    @FXML
    public MazeGenerator generator;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.Node GridPane_newMaze;

    private Stage primaryStage;


    public void setMyViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
    }

    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }

    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }

    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);
    }

    public void generateMaze(ActionEvent actionEvent) throws FileNotFoundException {
        if(generator == null)
            generator = new MazeGenerator();

        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        myViewModel.generateMaze(rows,cols);
//        Maze maze = generator.generateMaze(rows,cols);
        mazeDisplayer.drawMaze(myViewModel.getMaze());
    }

    public void solveMaze() {
        myViewModel.solveMaze(maze);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void keyPressed(KeyEvent keyEvent) throws FileNotFoundException {
        myViewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public  void  newGame(){
        GridPane_newMaze.setVisible(true);
    }
    public  void  propertiesGame(){
        Properties prop = new Properties();

        InputStream input = Configurations.class.getClassLoader().getResourceAsStream("config.properties");
        // load a properties file
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get value by key
        String str = "Tread = ";
        str += prop.getProperty("Tread");
        str += "\n";
        str += "Solution = ";
        str += prop.getProperty("sol");
        str += "\n";
        str += "Maze = ";
        str += prop.getProperty("maze");
        Stage window = new Stage();

        popAlert("maze properties", str);
    }

    public  void  exitGame(){
        String strExit = "Are you sure you want to exit?";
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("exit window");
        window.setMinHeight(300);
        window.setMinWidth(300);


        Label label = new Label();
        label.setText(strExit);
        Button yesButton = new Button("Yes!");
        Button noButton = new Button("No!");
        noButton.setOnAction(e -> window.close());
        yesButton.setOnAction(e -> System.exit(0));//Platform.exit();
        VBox layout = new VBox(20);//Platform.exit();
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

    public void helpGame () {
        String help = "Game Instructions:\n"+
                " You must bring the man to his destination. \n"+
                "If you have difficulty, you can always click \n"+
                " the Maze Solve button to get the optimal route.\n"+"" +
                "TODo: Add detail about more things we will add";
        popAlert("Help window", help);
    }


    public void aboutGame () {
        String strAbout = "This game was created as part of the course\n" +
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

        popAlert("About the game", strAbout);
    }

    public  void  saveGame(){

        //Receiving the path and than creating actual file there
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.maze)", "*.maze"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file == null)
            return;
        this.myViewModel.saveMaze(file.getAbsolutePath());
    }
    public  void  loadGame()
    {
        //Receiving the path and than creating actual file there
        FileChooser fileChooser = new FileChooser();
        //Defining the .maze to be the end of the file
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.maze)", "*.maze"));
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file == null)
            return;

        if (file.getAbsolutePath().length() <= 5 || !file.getAbsolutePath().substring(file.getAbsolutePath().length() - 4).equals("maze")) {
            Alert unvalidFile = new Alert(Alert.AlertType.ERROR);
            unvalidFile.setContentText("Unvalid file");
            unvalidFile.showAndWait();
            return;
        }
        this.myViewModel.loadGame(file.getAbsolutePath());
    }
    public void popAlert (String title, String message ){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(300);

        Label label = new Label();
        label.setText(message);
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

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = myViewModel.getMaze();
                try {
                    mazeDisplayer.drawMaze(maze);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else {
                Maze maze = myViewModel.getMaze();

                if (maze == this.maze)//Not generateMaze
                {
                    int rowChar = mazeDisplayer.getRow_player();
                    int colChar = mazeDisplayer.getColumn_player();
                    int rowFromViewModel = myViewModel.getRowChar();
                    int colFromViewModel = myViewModel.getColChar();

                    if(rowFromViewModel == rowChar && colFromViewModel == colChar)//Solve Maze
                    {
                        myViewModel.getSolution();
                    }
                    else//Update location
                    {
                        set_update_player_position_row(rowFromViewModel + "");
                        set_update_player_position_col(colFromViewModel + "");
                        try {
                            this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else//GenerateMaze
                {
                    this.maze = maze;
                    try {
                        mazeDisplayer.drawMaze(maze);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
