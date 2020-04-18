package main.ui.tui;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class HighscoresFrame implements main.ui.HighscoresFrame {

    public HighscoresFrame(String fileName) {

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
            new File(fileName);
        }
    }

    @Override
    public void update() {
        System.out.println();

        for (String line: lines) {
            System.out.println(line);
        }

        for (var view: viewList) {
            view.draw();
        }

        requestData();

    }

    private void requestData() {

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        if(command.equals("close")) {
            readyToDispose = true;
        } else if(command.equals("clear")) {
            File file = new File(fileName);
            file.delete();
            lines.clear();
        }

    }

    @Override
    public boolean isActive() {
        return !readyToDispose;
    }

    private List<View> viewList = new LinkedList<>();
    private List<String> lines = new LinkedList<>();
    private String fileName;
    private boolean readyToDispose = false;

}
