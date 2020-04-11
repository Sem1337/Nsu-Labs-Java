package main.ui.tui;

import main.Timer;
import main.ui.DTO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.LinkedList;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameFrame implements main.ui.GameFrame, KeyListener {

    public GameFrame() {

    }

    private void initViews(int rows,int cols) {
        viewList.clear();
        viewList.add(new MessageView("'flag/open row col' 'exit' 'settings' 'highscores' 'retry'\n (1<= row <= " + rows + "   1 <= col <= " + cols + ")\n:"));
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
    public void draw(Integer[][] field) {
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
    public void showEndGameMessage(String message) {
            new MessageView(message).draw();
    }

    @Override
    public void update() {
        for(int i = 0; i< 10;i++) System.out.println();

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        timerView = new MessageView(timer.getCurrentTime().toString() + " seconds");
        timerView.draw();

        fieldView.draw();


        for (var view: viewList) {
            view.draw();
        }

        System.out.println(currentInput);
    }

    @Override
    public DTO requestData() {


        Scanner scanner = new Scanner(System.in);
        command = scanner.nextLine();


        if(command.isEmpty()) return new DTO("empty");
        if(command.equals("exit")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
            readyToDispose = true;
            return new DTO("empty");
        } else if(command.equals("settings") || command.equals("highscores") || command.equals("retry")) {
            return new DTO(command);
        } else {
            try {
                String [] values = command.split(" ");
                if(values.length != 3)throw  new Exception("incorrect input");
                String type = values[0];
                String rowText = values[1];
                String colText = values[2];
                String commandName = type.equals("open")? "openCell" : "setFlag";
                int col = Integer.parseInt(colText) - 1;
                int row = Integer.parseInt(rowText) - 1;
                return new DTO( commandName, Integer.toString(row), Integer.toString(col));

            }catch(Exception ex) {
                return new DTO("empty");
            }
        }
    }

    @Override
    public boolean willDisappear() {
        return readyToDispose;
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            command = currentInput;
            currentInput = "";
        } else {
            currentInput += keyEvent.getKeyChar();
            try {
                new ProcessBuilder("pause", "/c", "pause").inheritIO().start().waitFor();
            } catch(IOException | InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            command = currentInput;
            currentInput = "";
        } else {
            currentInput += keyEvent.getKeyChar();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    private View fieldView;
    private View timerView;
    private List<View> viewList = new LinkedList<>();
    private main.Timer timer = new Timer();
    private boolean readyToDispose = false;
    private String currentInput = "";
    private String command = "";

}
