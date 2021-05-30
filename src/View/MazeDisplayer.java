package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
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
    private int row_player =0;
    private int column_player =0;
    private int row_goal;
    private int col_goal;
    private boolean firstRun = true;
    private Solution solution = null;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageGoalIcon = new SimpleStringProperty();
    StringProperty imageSolution= new SimpleStringProperty();
    StringProperty imageTree= new SimpleStringProperty();

    public String getImageTree() {
        return imageTree.get();
    }

    public void setImageTree(String imageTree) {
        this.imageTree.set(imageTree);
    }



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
    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
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
        if (firstRun){
            Position startPosition = maze.getStartPosition();
            set_player_position(startPosition.getRowIndex(),startPosition.getColumnIndex());
            firstRun = false;
        }
        set_goal_position();
        drawMaze();
    }

    public void drawSolution(Solution solution) throws FileNotFoundException {
    this.solution = solution;
    drawMaze();
    }

//    public void drawSolution(Solution solution) {
//        try {
//            /*get Maze Canvas dimensions */
//            double width = getWidth();
//            double height = getHeight();
//            /*get single cell dimesions */
//            double cellHeight = height / maze.getMaze().length;
//            double cellWidth = width / maze.getMaze()[0].length;
//            /* create Image instance of the Solution-step Image */
//            Image solutionPathImage = null;
//            solutionPathImage = new Image(new FileInputStream(getImageSolution()));
//            /* create Image instance of the Wall-Brick Image */
//            Image wallImage = null;
//            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
//            int[][] matrix = maze.getMaze();
//            GraphicsContext graphicsContext = getGraphicsContext2D();
//            /* reset the Maze canvas */
//            graphicsContext.clearRect(0, 0, getWidth(), getHeight());
//            /*Draw walls and goal point*/
//            ArrayList<AState> path = solution.getSolutionPath();
//            for (int i = 0; i < matrix.length; i++) {
//                for (int j = 0; j < matrix[i].length; j++) {
//                    if (matrix[i][j] == 1) {
//                        graphicsContext.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
//                    }
//                    /*if this cell is part of the path draw the solution path image */
//                    AState p = new MazeState(i,j);//using generic AState makes sense design-wise
//                    if (path.contains(p)) {
//                        graphicsContext.drawImage(solutionPathImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
//                    }
//                }
//            }
//            drawPlayerAndGoal(cellHeight,cellWidth,graphicsContext);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

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
            Image grass = null;
            Image treeImage = null;
            Image solutionPathImage = null;
            try{
                treeImage = new Image(new FileInputStream(getImageTree()));
                grass = new Image(new FileInputStream(getImageFileNameWall()));
                solutionPathImage = new Image(new FileInputStream(getImageSolution()));
            } catch(FileNotFoundException e){
                System.out.println("File not found");
            }
            ArrayList<AState> path = null;
            if (solution != null)
                path = solution.getSolutionPath();
            graphicsContext.drawImage(grass,0,0,canvasHeight,canvasWidth);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if(matrix[i][j] == 1){
                        //if it is a wall:
                        double w = j * cellWidth;
                        double h = i * cellHeight;
                        if (treeImage ==null){
                        graphicsContext.fillRect(w,h, cellWidth, cellHeight);
                        }
                        else{
                        graphicsContext.drawImage(treeImage,w,h,cellWidth,cellHeight);}

                    }
                    if (solution != null){
                        AState p = new MazeState(i,j);
                        if (path.contains(p)) {
                            graphicsContext.drawImage(solutionPathImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                    }
                }
            }
            if (solution != null)
                solution = null;
            drawPlayerAndGoal(cellHeight, cellWidth, graphicsContext);
        }
    }

    private void drawPlayerAndGoal(double cellHeight, double cellWidth, GraphicsContext graphicsContext) {
        double h_player = getRow_player() * cellHeight;
        double w_player = getColumn_player() * cellWidth;
        Image playerImage = null;
        try{
        playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        }catch(FileNotFoundException e){
            System.out.println("File of player not found");
        }
        graphicsContext.drawImage(playerImage,w_player,h_player, cellWidth, cellHeight);

        double h_goal = getRow_goal() * cellHeight;
        double w_goal = getCol_goal() * cellWidth;
        Image goalImage = null;
        try{
            goalImage = new Image(new FileInputStream(getImageGoalIcon()));
        }catch(FileNotFoundException e){
            System.out.println("File of player not found");
        }
        graphicsContext.drawImage(goalImage,w_goal,h_goal, cellWidth, cellHeight);
    }




}
