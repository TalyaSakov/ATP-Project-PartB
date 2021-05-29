package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.util.Observer;

public interface IModel {
    public void generateMaze(int rows, int cols);
    public Maze getMaze();
    public void updateCharacterLocation(int direction);
    public void assignObserver(Observer observer);
    public int getRowChar();
    public int getColChar();
    public int getRowGoal();
    public int getColGoal();
    public void solveMaze(Maze maze,int row_player,int col_player);
    public Solution getSolution();

}
