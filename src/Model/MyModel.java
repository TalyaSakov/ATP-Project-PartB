package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import IO.SimpleDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.MazeState;
import algorithms.search.SearchableMaze;
import Server.*;
import algorithms.search.Solution;
import javafx.geometry.Pos;


import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {

    private Maze maze;
    private SearchableMaze searchableMaze;
    private Solution solution;
//    private ExecutorService threadPool = Executors.newCachedThreadPool();
    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    private int rowChar;
    private int colChar;
    private int rowGoal;
    private int colGoal;
    public MyModel(){
        maze = null;
        searchableMaze = null;
        rowChar = 0;
        colChar = 0;
        this.mazeGeneratingServer = new Server(5400,1000, new ServerStrategyGenerateMaze());
        this.solveSearchProblemServer = new Server(5401,1000,new ServerStrategySolveSearchProblem());
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
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
        notifyObservers(direction);
     }

    @Override
    public void assignObserver(Observer observer) {
        this.addObserver(observer);
    }

//    public void generateMaze(int rows, int cols){
//        Maze maze = new MyMazeGenerator().generate(rows,cols);
//        this.maze = maze;
//        Position startPosition = maze.getStartPosition();
//        rowChar = startPosition.getRowIndex();
//        colChar = startPosition.getColumnIndex();
//        Position goalPosition = maze.getGoalPosition();
//        rowGoal = goalPosition.getRowIndex();
//        colGoal = goalPosition.getColumnIndex();
//        this.searchableMaze = new SearchableMaze(maze);
//        setChanged();
//        notifyObservers();
//    }
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

    public void setRowGoal(int rowGoal) {
        this.rowGoal = rowGoal;
    }

    public void setColGoal(int colGoal) {
        this.colGoal = colGoal;
    }


//    public void solveMaze(int row_player, int col_player) {
//        BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch();
//        maze.setStartPosition(row_player,col_player);
//        solution = breadthFirstSearch.solve(searchableMaze);
//        setChanged();
//        notifyObservers();
//    }

    @Override
    public Solution getSolution() {
        return solution;}

    public void solveMaze(int row_player, int col_player) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, (IClientStrategy) (inFromServer, outToServer) -> {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();
                    Position realStartPos = new Position(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());//save the real start position
                    maze.setStartPosition(rowChar,colChar);
                    maze.setMazeHashCode(maze.hashCode());
                    toServer.writeObject(maze);
                    toServer.flush();
                    maze.setStartPosition(realStartPos.getRowIndex(),realStartPos.getColumnIndex()); //return to the real start position
                    maze.setMazeHashCode(maze.hashCode());
                    Solution sol = (Solution)fromServer.readObject();
                    solution = sol;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            client.communicateWithServer();
            setChanged();
            notifyObservers();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void generateMaze(int row, int col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[row*col + 10000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        maze.setMazeHashCode(maze.hashCode());
                        rowChar = maze.getStartPosition().getRowIndex();
                        colChar = maze.getStartPosition().getColumnIndex();
                        rowGoal = maze.getGoalPosition().getRowIndex();
                        colGoal = maze.getGoalPosition().getColumnIndex();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            setChanged();
            notifyObservers();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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

    public void saveMaze(File saveFile){
        File endFile = new File(saveFile.getPath());
        try {
            /*game state params --> save to file*/
            endFile.createNewFile();
            StringBuilder  builder = new StringBuilder();
            builder.append(rowChar+"\n");
            builder.append(colChar+"\n");
            builder.append(rowGoal+"\n");
            builder.append(colGoal+"\n");
            builder.append(maze.getMaze().length+"\n");
            builder.append(maze.getMaze()[0].length+"\n");

            for(int i = 0; i < maze.getMaze().length; i++)
            {
                for(int j = 0; j < maze.getMaze()[0].length; j++)
                {
                    builder.append(maze.getMaze()[i][j]+"");
                    if(j < maze.getMaze()[0].length - 1)
                        builder.append(",");
                }
                builder.append("\n");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile.getPath()));
            writer.write(builder.toString());
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void exit() {
        this.mazeGeneratingServer.stop();
        this.solveSearchProblemServer.stop();
    }
    
    public void loadMaze(File file){
        int goalRowIdx = 0, goalColIdx = 0 , playerRowIdx = 0, playerColIdx= 0, mazeNumOfRows = 0, mazeNumOfCols = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            /*read 6 lines from file -- the saved parameters of a maze game */
            for( int i = 0 ; i < 6 ; i++){
                String line = br.readLine();
                if (line != null) {
                    if(i == 0)
                        playerRowIdx = Integer.parseInt(line);
                    if(i == 1)
                        playerColIdx = Integer.parseInt(line);
                    if(i == 2)
                        goalRowIdx = Integer.parseInt(line);
                    if(i == 3)
                        goalColIdx = Integer.parseInt(line);
                    if(i == 4)
                        mazeNumOfRows = Integer.parseInt(line);
                    if(i == 5)
                        mazeNumOfCols = Integer.parseInt(line);
                }
            }
            int[][] grid = new int[mazeNumOfRows][mazeNumOfCols];
            String line = "";
            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");
                int col = 0;
                for (String c : cols) {
                    grid[row][col] = Integer.parseInt(c);
                    col++;
                }
                row++;
            }
            br.close();
            this.maze = new Maze(mazeNumOfRows,mazeNumOfCols);
            this.maze.setMaze(grid);

            this.maze.setStartPosition(playerRowIdx,playerColIdx);
            this.maze.setEndPosition(goalRowIdx,goalColIdx);
            this.rowChar = playerRowIdx;
            this.colChar = playerColIdx;

            this.rowGoal = goalRowIdx;
            this.colGoal = goalColIdx;
            setChanged();
            notifyObservers("loaded");
        } catch (IOException e){
            e.printStackTrace();
        }


}
}
