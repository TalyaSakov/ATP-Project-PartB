package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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



    StringProperty imageSolution= new SimpleStringProperty();

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
    public String getImageSolution() { return imageSolution.get(); }
    public void setImageSolution(String imageSolution) { this.imageSolution.set(imageSolution); }

    public int getRow_player() {return row_player; }
    public int getColumn_player() {
        return column_player;
    }

    public void drawMaze(Maze maze) throws FileNotFoundException {
        this.maze = maze;
        set_goal_position();
        drawMaze();
    }

    public void drawSolution() {
        try {
            /*get Maze Canvas dimensions */
            double width = getWidth();
            double height = getHeight();
            /*get single cell dimesions */
            double cellHeight = height / maze.getMaze().length;
            double cellWidth = width / maze.getMaze()[0].length;
            /* create Image instance of the Solution-step Image */
            Image solutionPathImage = null;
            solutionPathImage = new Image(new FileInputStream(getImageSolution()));
            /* create Image instance of the Wall-Brick Image */
            Image wallImage = null;
            wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
            int[][] grid = maze.getMazeGrid();
            GraphicsContext graphicsContext = getGraphicsContext2D();
            /* reset the Maze canvas */
            graphicsContext.clearRect(0, 0, getWidth(), getHeight());
            /*Draw walls and goal point*/
            ArrayList<AState> path = solutionObj.getSolutionPath();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == 1) {
                        graphicsContext.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                    }
                    /*if this cell is part of the path draw the solution path image */
                    AState p = new MazeState(new Position(i, j), null, 0);//using generic AState makes sense design-wise
                    if (path.contains(p)) {
                        graphicsContext.drawImage(solutionPathImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                    }
                }
            }
            graphicsContext.drawImage(playerImage, playerPosCol * cellWidth, playerPosRow * cellHeight, cellWidth, cellHeight);
            endPointImage = new Image(new FileInputStream(ImageFileNameFlag.get()));
            /*draw the goal Image in the Maze's Goal Position */
            Position goalPosition = maze.getGoalPosition();
            int goalPosRow = goalPosition.getRowIndex();
            int goalPosCol = goalPosition.getColumnIndex();
            graphicsContext.drawImage(endPointImage, goalPosCol * cellWidth, goalPosRow * cellHeight, cellWidth, cellHeight);

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*reset the mazeSolution */
        solutionObj = null;
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
