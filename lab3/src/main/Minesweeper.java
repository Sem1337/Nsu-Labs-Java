package main;

import main.ui.DTO;
import main.ui.HighscoresFrame;
import main.ui.GameFrame;
import main.ui.SetupFrame;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class Minesweeper {
    private static final boolean gui = true;
    Minesweeper() {

        if(gui) {
            gameFrame = new main.ui.gui.GameFrame();
        } else {
            gameFrame = new main.ui.tui.GameFrame();
        }

        setup();
        run();
    }

    private void setup() {
        cellsState =  new Integer[rows][cols];
        field = new Integer[rows][cols];

        for(int i = 0; i<rows; i++) {
            for(int j=0;j < cols;j++){
                field[i][j] = 0;
                cellsState[i][j] = -2;
            }
        }

        openedCells = 0;
        alive = 1;
        endShown = 0;
        gameFrame.restart(rows,cols);
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
    }

    private void run() {

        startUpdatingGameFrame();
        while(!gameFrame.willDisappear()) {

            while (openedCells < rows * cols - minesCount && alive == 1 && !gameFrame.willDisappear()) {
                handleResponse(gameFrame.requestData());
                gameFrame.draw( getVisibleField());
            }

            if(gameFrame.willDisappear())break;
            if(endShown == 0) {
                endOfGame();
                endShown = 1;
            }

            handleResponse(gameFrame.requestData());

        }
    }

    private void startUpdatingGameFrame() {
        isUpdatingStopped = false;

        gameFrameUpdatingThread = new Thread(() -> {
            while (openedCells < rows * cols - minesCount && alive == 1 && !gameFrame.willDisappear()) {
                gameFrame.update();
            }
            isUpdatingStopped = true;
        });

        gameFrameUpdatingThread.start();
    }

    private void saveResult(long time) {
        String str = "size: " + rows + " x " + cols + " mines: "+ minesCount + " time: " + time + " seconds\n";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(gameArchiveFileName, true));
            writer.write(str);
        }catch (IOException e){
            System.out.println(e.getLocalizedMessage());
        }finally {
            if(writer != null) {
                try {
                    writer.close();
                }catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
    }

    private void endOfGame() {
        String message;
        if(alive == 1) {
            long time = gameFrame.getCurrentGameTime();
          message = "You won in " + time + " seconds!";
          saveResult(time);
        } else {
          message = "You lost";
        }
        gameFrame.showEndGameMessage(message);
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
                if(row<0 || row >= rows || col < 0 || col >= cols)return;
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
                if(row<0 || row >= rows || col < 0 || col >= cols)return;
                if(cellsState[row][col] == -3) {
                    cellsState[row][col] = -2;
                } else if(cellsState[row][col] == -2){
                    cellsState[row][col] = -3;
                }
            } else if(data.getDescription().equals("retry")) {
                setup();
                if(isUpdatingStopped) {
                    startUpdatingGameFrame();
                }
            } else if(data.getDescription().equals("settings")) {
                settings();
            } else if(data.getDescription().equals("confirmSettings")) {
                String[] args = data.getArgs();
                rows = Integer.parseInt(args[0]);
                cols = Integer.parseInt(args[1]);
                minesCount = Integer.parseInt(args[2]);
                setup();
            } else if(data.getDescription().equals("highscores")) {
                showHighscores();
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


    private void showHighscores() {
        gameFrame.pause();

        try {
            gameFrameUpdatingThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        HighscoresFrame highscoresFrame;
        if(gui){
            highscoresFrame = new main.ui.gui.HighscoresFrame(gameArchiveFileName);
        } else {
            highscoresFrame = new main.ui.tui.HighscoresFrame(gameArchiveFileName);
        }

        while(!highscoresFrame.willDisappear()){
            highscoresFrame.update();
            handleResponse(highscoresFrame.requestData());
        }

        gameFrame.resume();
        startUpdatingGameFrame();
    }

    private void settings() {

        gameFrame.pause();

        try {
            gameFrameUpdatingThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        SetupFrame setupFrame;
        if(gui) {
            setupFrame = new  main.ui.gui.SetupFrame();
        } else {
            setupFrame = new  main.ui.tui.SetupFrame();
        }

        while(!setupFrame.willDisappear()) {
            setupFrame.update();
            handleResponse(setupFrame.requestData());
        }
        setupFrame.update();
        gameFrame.resume();
        startUpdatingGameFrame();
    }

    private Thread gameFrameUpdatingThread;
    private boolean isUpdatingStopped = true;
    private String gameArchiveFileName = "resources/archive.txt";
    private GameFrame gameFrame;
    private int openedCells = 0;
    private int minesCount = 7;
    private int rows = 15;
    private int cols = 15;
    private Integer[][] cellsState;                   // -4: opened mine  -3: flag    -2: unchecked   -1: mine  0: opened
    private Integer[][] field;                        // -1: mine    0-8: number
    private int alive = 1;
    private int endShown = 0;

}
