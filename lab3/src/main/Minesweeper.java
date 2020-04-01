package main;

import main.ui.DTO;
import main.ui.Frame;
import main.ui.gui.GameFrame;
import main.ui.gui.SetupFrame;

import java.util.Random;

public class Minesweeper {

    public Minesweeper(int width, int height) {
        setup();
        gameFrame = new GameFrame(width,height, rows, cols);
        run();
    }

    private void setup() {
        cellsState =  new Integer[rows][cols];
        field = new Integer[rows][cols];
        //placeMines();

        for(int i = 0; i<rows; i++) {
            for(int j=0;j < cols;j++){
                field[i][j] = 0;
                cellsState[i][j] = -2;
            }
        }

        openedCells = 0;
        alive = 1;
        endShown = 0;
    }


    // place mines after first opened cell at (row,col)
    private void placeMines(int row,int col) {
        int[] cells = new int[rows*cols-1];

        int cell = 0;
        for(int i = 0; i < rows * cols - 1; cell++){
            if(cell / cols == row && cell%cols == col)continue;
            cells[i++] = cell;
        }
        int index;
        Random random = new Random();
        for (int i = cells.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                cells[index] ^= cells[i];
                cells[i] ^= cells[index];
                cells[index] ^= cells[i];
            }
        }


        for(int i = 0; i < minesCount; i++) {
            field[cells[i]/cols][cells[i]%cols] = -1;
        }


        for(int i = 0; i<rows; i++) {
            for(int j=0;j < cols;j++){
                if(field[i][j] == -1)continue;
                int cnt =0;
                for(int l = -1; l <= 1; l++) {
                    for(int r = -1; r <= 1; r++) {
                        if(i+l < 0 || j+r < 0 || i+l>= rows || j+r >= cols)continue;
                        if(field[i+l][j+r] == -1)cnt++;
                    }
                }
                field[i][j] = cnt;
            }
        }


        for(int i =0; i< rows ;i ++ ){
            for(int j=0;j<cols;j++){
                System.out.print(cellsState[i][j] + " ");
            }
            System.out.println();
        }

    }

    private void run() {


        while(true) {
            while (openedCells < rows * cols - minesCount && alive == 1) {
                // request for the user action
                handleResponse(gameFrame.requestData());


                gameFrame.draw(getVisibleField());
                gameFrame.update();
            }

            if(endShown == 0) {
                endOfGame();
                endShown = 1;
            }
            handleResponse(gameFrame.requestData());

        }
    }

    private void endOfGame() {
        System.out.println("END!");

    }

    private void uncoverField() {
        for(int row =0; row < rows; row++) {
            for(int col =0; col < cols; col++) {
                if(cellsState[row][col] == -2 || cellsState[row][col] == -3) {
                    cellsState[row][col] = 0;
                }
            }
        }
    }


    private void handleResponse(DTO data) {
        if(!data.getDescription().equals("empty")) {
            if(data.getDescription().equals("openCell")) {
                String[] args = data.getArgs();
                int row = Integer.parseInt(args[0]);
                int col = Integer.parseInt(args[1]);
                if(cellsState[row][col] != 0) {
                    if(field[row][col] == -1)  {
                        cellsState[row][col] = -4;
                        alive = 0;
                        uncoverField();
                        return;
                    } else {
                        cellsState[row][col] = -2;
                    }
                    if(openedCells == 0)placeMines(row,col);
                    openCells(row,col);
                }

            } else if(data.getDescription().equals("setFlag")) {
                String[] args = data.getArgs();
                int row = Integer.parseInt(args[0]);
                int col = Integer.parseInt(args[1]);
                if(cellsState[row][col] == -3) {
                    cellsState[row][col] = -2;
                } else if(cellsState[row][col] == -2){
                    cellsState[row][col] = -3;
                }
            } else if(data.getDescription().equals("retry")) {
                System.out.println("retry!");
                setup();
            }
        }
    }

    private void openCells(int row,int col) {
        if(row < 0 || col < 0 || row >= rows || col >= cols)return;
        if(cellsState[row][col] != -2)return;
        if(field[row][col] == -1)return;
        cellsState[row][col] = 0;
        openedCells++;
        if(openedCells == rows*cols - minesCount)uncoverField();
        if(field[row][col] > 0)return;
        for(int nextRow = row-1; nextRow <= row+1; nextRow++) {
            for(int nextCol = col-1; nextCol <= col+1; nextCol++) {
                openCells(nextRow,nextCol);
            }
        }
    }

    private Integer[][] getVisibleField() {
        Integer[][] visibleField = new Integer[rows][cols];
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col  < cols; col++) {
                if(cellsState[row][col] == -2) {
                    visibleField[row][col] = -2;
                } else if(cellsState[row][col] == -3){
                    visibleField[row][col] = -3;
                } else if(cellsState[row][col] == -4) {
                    visibleField[row][col] = -4;
                } else {
                    visibleField[row][col] = field[row][col];
                }
            }
        }
        return visibleField;
    }


    private void settings() {

        Frame setupFrame = new SetupFrame();

    }


    private GameFrame gameFrame;
    private int openedCells = 0;
    private int minesCount = 7;
    private int rows = 13;
    private int cols = 7;
    private Integer[][] cellsState;                   // -4: opened mine  -3: flag    -2: unchecked   -1: mine  0: opened
    private Integer[][] field;                        // -1: mine    0-8: number
    private int alive = 1;
    private int endShown = 0;

}
