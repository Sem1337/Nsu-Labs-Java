package main.ui.tui;

import main.ui.DTO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class HighscoresFrame implements main.ui.HighscoresFrame {

    public HighscoresFrame(String fileName) {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        this.fileName = fileName;
        readHighscoresFile();
        viewList.add(new MessageView("Commands: 'close',  'clear'\n:"));
    }



    private void readHighscoresFile() {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine() + System.lineSeparator());
            }
        } catch (IOException e) {
            File file = new File(fileName);
        }
    }

    @Override
    public void update() {
        for(int i = 0; i< 10;i++) System.out.println();

        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        for (String line: lines) {
            System.out.println(line);
        }

        for (var view: viewList) {
            view.draw();
        }

        System.out.println(currentInput);

    }

    @Override
    public DTO requestData() {

        Scanner scanner = new Scanner(System.in);
        command = scanner.nextLine();

        if(command.equals("close")) {
            readyToDispose = true;
        } else if(command.equals("clear")) {
            File file = new File(fileName);
            file.delete();
            lines.clear();
        }


        return new DTO("empty");
    }

    @Override
    public boolean willDisappear() {
        return readyToDispose;
    }

    private List<View> viewList = new LinkedList<>();
    private List<String> lines = new LinkedList<>();
    private String fileName;
    private boolean readyToDispose = false;
    private String currentInput = "";
    private String command = "";

}
