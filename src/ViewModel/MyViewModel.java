package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private Maze maze;
    private Solution solution;



    private int rowChar;
    private int colChar;
    private int rowGoal;
    private int colGoal;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof IModel) {
            if (maze == null) { //Generate maze
                this.maze = model.getMaze();
            } else {
                Maze maze = model.getMaze();
                if (maze == this.maze) { // Move Character
                    int rowChar = model.getRowChar();
                    int colChar = model.getColChar();
                    if (this.colChar == colChar && this.rowChar == rowChar){ // Solve Maze
                        getSolution();
                    }
                     else //Update location
                     {
                         this.rowChar = rowChar;
                         this.colChar = colChar;
                     }
                }
                else{
                    this.maze = maze;
                    rowChar = model.getRowChar();
                    colChar = model.getColChar();
                }
            }
            setChanged();
            notifyObservers();
        }
    }

    public void generateMaze(int row, int col) {
        this.model.generateMaze(row, col);
    }

    public void moveCharacter(KeyEvent keyEvent) {
        int direction = -1;
        switch (keyEvent.getCode()) {
            case UP:
                direction = 1;
                break;
            case DOWN:
                direction = 2;
                break;
            case LEFT:
                direction = 3;
                break;
            case RIGHT:
                direction = 4;
                break;
        }
        model.updateCharacterLocation(direction);
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    public void setRowChar(int rowChar) {
        this.rowChar = rowChar;
    }

    public void setColChar(int colChar) {
        this.colChar = colChar;
    }
    public int getRowGoal() {
        return rowGoal;
    }

    public int getColGoal() {
        return colGoal;
    }

    public Maze getMaze() {
        return maze;
    }

    public void solveMaze(Maze maze,int row_player,int col_player){
        model.solveMaze(maze,row_player,col_player);
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void loadGame(String absolutePath) {
    }

    public void saveMaze(String absolutePath) {
    }

}