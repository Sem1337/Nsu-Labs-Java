package main;

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
        cellsState =  new int[rows][cols];
        field = new int[rows][cols];
        placeMines();
        alive = 1;
    }

    private void placeMines() {
        int[] cells = new int[rows*cols];
        for(int i = 0; i < rows * cols ; i++ ){
            cells[i] = i;
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
                cellsState[i][j] = -1;
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
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }

    }

    private void run() {

        int openedCells = 0;
        while(openedCells < rows*cols - minesCount && alive == 1) {

            // request for the user action


            gameFrame.draw(cellsState, field);
            gameFrame.update();

        }

    }

    private void settings() {

        Frame setupFrame = new SetupFrame();



    }


    private GameFrame gameFrame;
    private int minesCount = 10;
    private int rows = 13;
    private int cols = 7;
    private int[][] cellsState;
    private int[][] field;
    private int alive = 1;


}
