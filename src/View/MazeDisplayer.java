package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class MazeDisplayer extends Canvas {
    private Maze maze;
    private int row_player = 0;
    private int column_player = 0;
    private int row_goal;
    private int col_goal;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageGoalIcon = new SimpleStringProperty();

    public String getImageGoalIcon() {
        return imageGoalIcon.get();
    }
    public void setImageGoalIcon(String imageGoalIcon) {
        this.imageGoalIcon.set(imageGoalIcon);
    }
    public int getRow_goal() {
        return row_goal;
    }
    public int getCol_goal() {
        return col_goal;
    }
    public void set_goal_position(){
        this.row_goal = maze.getGoalPosition().getRowIndex();
        this.col_goal = maze.getGoalPosition().getColumnIndex();
    }
    public void set_player_position(int row,int col) throws FileNotFoundException {
        this.row_player = row;
        this.column_player = col;
        drawMaze();
    }

    public Maze getMaze() {
        return maze;
    }
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }
    public void setImageFileNamePlayer(String imageFileNamePlayer) { this.imageFileNamePlayer.set(imageFileNamePlayer); }

    public int getRow_player() {return row_player; }
    public int getColumn_player() {
        return column_player;
    }

    public void drawMaze(Maze maze) throws FileNotFoundException {
        this.maze = maze;
        set_goal_position();
        drawMaze();
    }

    private void drawMaze() throws FileNotFoundException {
        if(maze != null){
            int [][] matrix = maze.getMaze();
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = matrix.length;
            int cols = matrix[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.RED);
            Image wallImage = null;
            try{
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            } catch(FileNotFoundException e){
                System.out.println("File not found");
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if(matrix[i][j] == 1){
                        //if it is a wall:
                        double w = j * cellWidth;
                        double h = i * cellHeight;
                        if (wallImage ==null){
                        graphicsContext.fillRect(w,h, cellWidth, cellHeight);
                        }
                        else{
                        graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);}
                    }
                }
            }
            double h_player = getRow_player() * cellHeight;
            double w_player = getColumn_player() *cellWidth;
            Image playerImage = null;
            try{
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            }catch(FileNotFoundException e){
                System.out.println("File of player not found");
            }
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

            double h_goal = getRow_goal() * cellHeight;
            double w_goal = getCol_goal() *cellWidth;
            Image goalImage = null;
            try{
                goalImage = new Image(new FileInputStream(getImageGoalIcon()));
            }catch(FileNotFoundException e){
                System.out.println("File of player not found");
            }
            graphicsContext.drawImage(goalImage,w_goal,h_goal,cellWidth,cellHeight);
        }
    }

}
