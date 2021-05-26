package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;

import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private SearchableMaze searchableMaze;
    private Solution solution;
    private int rowChar;
    private int colChar;
    private int rowGoal;
    private int colGoal;

    public MyModel(){
        maze = null;
        searchableMaze = null;
        rowChar = 0;
        colChar = 0;
    }

    public void updateCharacterLocation(int direction){
        switch (direction){
            case 1: //UP
                if ((rowChar !=0) && (maze.getMaze()[rowChar-1][colChar] != 1))
                    rowChar --;
                break;
            case 2: //DOWN
                if ((rowChar != maze.getMaze().length -1) && (maze.getMaze()[rowChar+1][colChar] != 1))
                    rowChar ++;
                break;

            case 3: //LEFT
                if ((colChar !=0) && (maze.getMaze()[rowChar][colChar -1] != 1))
                    colChar --;
                break;

            case 4://RIGHT
                if ((colChar != maze.getMaze()[0].length -1)&& (maze.getMaze()[rowChar][colChar+1] != 1))
                    colChar ++;
                break;
        }
        setChanged();
        notifyObservers();
     }

    @Override
    public void assignObserver(Observer observer) {
        this.addObserver(observer);
    }

    public void generateMaze(int rows, int cols){
        Maze maze = new MyMazeGenerator().generate(rows,cols);
        this.maze = maze;
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        setChanged();
        notifyObservers();
    }
    @Override
    public Maze getMaze() {
        return maze;
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    public int getRowGoal() {
        return rowGoal;
    }

    public int getColGoal() {
        return colGoal;
    }

    @Override
    public void solveMaze(Maze maze) {
        BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch();
        solution = breadthFirstSearch.solve(searchableMaze);
        setChanged();
        notifyObservers();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

}
