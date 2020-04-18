package main;


import lombok.Setter;
import main.ui.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Minesweeper {

    Minesweeper() {
        gameFrame = FrameFactory.getGameFrame(this);
        setup();
        run();
    }

    public synchronized void setup() {
        cellsState = new Integer[rows][cols];
        field = new Integer[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                field[i][j] = 0;
                cellsState[i][j] = -2;
            }
        }

        openedCells = 0;
        alive = 1;
        gameFrame.restart(rows, cols);
    }


    // place mines after first opened cell at (row,col)
    private void placeMines(int row, int col) {
        int[] cells = new int[rows * cols - 1];

        int cell = 0;
        for (int i = 0; i < rows * cols - 1; cell++) {
            if (cell / cols == row && cell % cols == col) continue;
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


        for (int i = 0; i < minesCount; i++) {
            field[cells[i] / cols][cells[i] % cols] = -1;
        }


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (field[i][j] == -1) continue;
                int cnt = 0;
                for (int l = -1; l <= 1; l++) {
                    for (int r = -1; r <= 1; r++) {
                        if (i + l < 0 || j + r < 0 || i + l >= rows || j + r >= cols) continue;
                        if (field[i + l][j + r] == -1) cnt++;
                    }
                }
                field[i][j] = cnt;
            }
        }
    }

    private void run() {

        while (gameFrame.isActive()) {

            while (!isGameOver() && gameFrame.isActive()) {
                endShown = 0;
                gameFrame.setNewField(getVisibleField());
                gameFrame.update();
                if (state != 0) showExtraFrame();
            }

            if (!gameFrame.isActive()) break;
            if (endShown == 0) {
                endOfGame();
                endShown = 1;
            }

            gameFrame.setNewField(getVisibleField());
            gameFrame.update();
            if (state != 0) showExtraFrame();

        }
    }

    private void saveResult(long time) {
        String str = "size: " + rows + " x " + cols + " mines: " + minesCount + " time: " + time + " seconds\n";
        try (BufferedWriter writer =  new BufferedWriter(new FileWriter(gameArchiveFileName, true))) {
            writer.write(str);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void endOfGame() {
        if (alive == 1) {
            long time = gameFrame.getCurrentGameTime();
            saveResult(time);
        }
        new Thread(() -> gameFrame.showEndGameMessage()).start();
    }


    private void uncoverField() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cellsState[row][col] == -2 || cellsState[row][col] == -3) {
                    cellsState[row][col] = 0;
                }
            }
        }
    }


    public void openCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return;
        if (cellsState[row][col] != 0) {
            if (field[row][col] == -1) {
                cellsState[row][col] = -4;
                alive = 0;
                uncoverField();
                return;
            } else {
                cellsState[row][col] = -2;
            }
            if (openedCells == 0) placeMines(row, col);
            openOtherCells(row, col);
        }
    }

    public void setFlag(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return;
        if (cellsState[row][col] == -3) {
            cellsState[row][col] = -2;
        } else if (cellsState[row][col] == -2) {
            cellsState[row][col] = -3;
        }
    }

    public void setParameters(int rows, int cols, int minesCount) {
        this.rows = rows;
        this.cols = cols;
        this.minesCount = minesCount;
        setup();
    }

    private void openOtherCells(int row, int col) {
        if (row < 0 || col < 0 || row >= rows || col >= cols) return;
        if (cellsState[row][col] != -2) return;
        if (field[row][col] == -1) return;
        cellsState[row][col] = 0;
        openedCells++;
        if (openedCells == rows * cols - minesCount) uncoverField();
        if (field[row][col] > 0) return;
        for (int nextRow = row - 1; nextRow <= row + 1; nextRow++) {
            for (int nextCol = col - 1; nextCol <= col + 1; nextCol++) {
                openOtherCells(nextRow, nextCol);
            }
        }
    }

    private synchronized Integer[][] getVisibleField() {
        Integer[][] visibleField = new Integer[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cellsState[row][col] == -2) {
                    visibleField[row][col] = -2;
                } else if (cellsState[row][col] == -3) {
                    visibleField[row][col] = -3;
                } else if (cellsState[row][col] == -4) {
                    visibleField[row][col] = -4;
                } else {
                    visibleField[row][col] = field[row][col];
                }
            }
        }

        return visibleField;
    }


    private void showExtraFrame() {
        gameFrame.pause();
        Frame frame;
        if (state == 1) {
            frame = FrameFactory.getSetupFrame(this);
        } else if (state == 2) {
            frame = FrameFactory.getHighScoresFrame(gameArchiveFileName);
        } else {
            frame = FrameFactory.getAboutFrame();
        }

        while (frame.isActive()) {
            frame.update();
        }
        gameFrame.resume();
        state = 0;
    }


    public boolean isGameOver() {
        return (alive == 0) || (openedCells == rows * cols - minesCount);
    }

    public boolean isAlive() {
        return alive == 1;
    }

    @Setter
    private int state = 0;
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
