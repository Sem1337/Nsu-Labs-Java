package main.ui.tui;

import main.Minesweeper;
import main.Timer;
import java.util.LinkedList;

import java.util.List;
import java.util.Scanner;
public class GameFrame implements main.ui.GameFrame {

    public GameFrame(Minesweeper gameModel) {
        this.gameModel = gameModel;
    }

    private void initViews(int rows,int cols) {
        viewList.clear();
        viewList.add(new MessageView("commands: 'exit' 'retry' 'settings' 'highscores'  'about' 'open x y'  'flag x y'\n (1<= row <= " + rows + "   1 <= col <= " + cols + ")\n:"));
    }

    private void initField(int rows, int cols) {
        Character[][] fieldCharacters = new Character[rows][cols];
        for(int row = 0; row < rows; row++) {
            for(int col =0; col < cols; col++) {
                fieldCharacters[row][col] = 'O';
            }
        }
        fieldView = new GridView(fieldCharacters);
    }

    @Override
    public void setNewField(Integer[][] field) {
            if(field.length == 0)return;
            Character[][] fieldCharacters = new Character[field.length][field[0].length];
            for(int row = 0; row < field.length; row++) {
                for(int col =0; col < field[0].length; col++) {
                    if(field[row][col] == -3) {
                        fieldCharacters[row][col] = 'f';

                    } else if(field[row][col] == -2) {
                        fieldCharacters[row][col] = 'O';
                    }
                    else if(field[row][col] == -1) {
                        fieldCharacters[row][col] = 'x';
                    } else if(field[row][col] == -4) {
                        fieldCharacters[row][col] = 'X';
                    } else {
                        if(field[row][col] == 0) fieldCharacters[row][col] = ' ';
                        else fieldCharacters[row][col] = (char)('0' + field[row][col]);
                    }
                }
            }
            fieldView = new GridView(fieldCharacters);
    }

    @Override
    public void restart(int rows, int cols) {
            timer.start();
            initViews(rows,cols);
            initField(rows,cols);
    }

    @Override
    public void pause() {
            timer.stop();
            readyToDispose = true;
    }

    @Override
    public void resume() {
            timer.resume();
            readyToDispose = false;
    }

    @Override
    public Long getCurrentGameTime() {
        return timer.getCurrentTime();
    }

    @Override
    public void showEndGameMessage() {
        String message;
        if(gameModel.isAlive()) {
            message = "You won in " + getCurrentGameTime() + " seconds!";
        } else {
            message = "You lost";
        }
        new MessageView(message).draw();
    }

    @Override
    public void update() {
        System.out.println();

        View timerView = new MessageView(timer.getCurrentTime().toString() + " seconds");
        timerView.draw();

        fieldView.draw();

        for (var view: viewList) {
            view.draw();
        }
        requestData();
    }


    private void requestData() {


        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        if(command.equals("exit")) {
            readyToDispose = true;
        } else if(command.equals("settings")) {
            gameModel.setState(1);
        } else if(command.equals("highscores")) {
            gameModel.setState(2);
        } else if(command.equals("about")) {
            gameModel.setState(3);
        } else if(command.equals("retry")) {
            gameModel.setup();
        } else {
            try {
                String [] values = command.split(" ");
                if(values.length != 3)throw  new Exception("incorrect input");
                String type = values[0];
                String rowText = values[1];
                String colText = values[2];
                int col = Integer.parseInt(colText) - 1;
                int row = Integer.parseInt(rowText) - 1;
                if(type.equals("flag")) {
                    gameModel.setFlag(row, col);
                } else {
                    gameModel.openCell(row,col);
                }

            }catch(Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    @Override
    public boolean isActive() {
        return !readyToDispose;
    }

    private Minesweeper gameModel;
    private View fieldView;
    private List<View> viewList = new LinkedList<>();
    private main.Timer timer = new Timer();
    private boolean readyToDispose = false;

}
