package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.MazeState;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;

import java.util.Observable;
import java.util.Observer;

public class
MyModel extends Observable implements IModel {
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

            case 5: //UP-Right
                if (Diagonal_Verification(5)){
                    rowChar --;
                    colChar ++;
                break;
                }
            case 6: //UP-LEFT
                if (Diagonal_Verification(6)){
                    rowChar --;
                    colChar --;
                break;
                }

            case 7: //DOWN-RIGHT
                if (Diagonal_Verification(7)){
                    rowChar ++;
                    colChar ++;
                break;
                }

            case 8://Down-left
                if (Diagonal_Verification(8)){
                    rowChar ++;
                    colChar --;
                    break;

                }

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
        Position startPosition = maze.getStartPosition();
        rowChar = startPosition.getRowIndex();
        colChar = startPosition.getColumnIndex();
        this.searchableMaze = new SearchableMaze(maze);
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
    public void solveMaze(Maze maze,int row_player, int col_player) {
        BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch();
        maze.setStartPosition(row_player,col_player);
        solution = breadthFirstSearch.solve(searchableMaze);
        setChanged();
        notifyObservers();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    public boolean Diagonal_Verification(int direction) {
        return switch (direction) {
            case 5 -> //UP-Right
                    ((rowChar != 0) && (maze.getMaze()[rowChar - 1][colChar+1] != 1) && (colChar != maze.getMaze()[0].length - 1) && (((maze.getMaze()[rowChar][colChar + 1] != 1)) || (maze.getMaze()[rowChar -1][colChar ]!= 1)));
            case 6 -> //UP-LEFT
                    (rowChar != 0) && (maze.getMaze()[0].length != 0) && (maze.getMaze()[rowChar - 1][colChar-1] != 1) && ((maze.getMaze()[rowChar-1][colChar] != 1) || (maze.getMaze()[rowChar][colChar-1] != 1));
            case 7 -> //DOWN-RIGHT
                    ((rowChar != maze.getMaze().length - 1)  && (colChar != maze.getMaze()[0].length - 1) && ((maze.getMaze()[rowChar + 1][colChar] != 1) || (maze.getMaze()[rowChar][colChar + 1] != 1)));
            case 8 ->//Down-left
                    ((rowChar != maze.getMaze().length - 1)  && (colChar != 0) && ((maze.getMaze()[rowChar][colChar - 1] != 1) || (maze.getMaze()[rowChar + 1][colChar] != 1)));
            default -> false;
        };
    }
}
