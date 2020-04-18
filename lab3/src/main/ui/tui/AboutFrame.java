package main.ui.tui;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class AboutFrame implements main.ui.AboutFrame {

    public AboutFrame() {
        viewList.add(new MessageView("Commands: 'close'\n:"));
        readRules();
    }


    private void readRules() {
        try (Scanner scanner = new Scanner(new File("resources/rules.txt"))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void requestData() {

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        if(command.equals("close")) {
            readyToDispose = true;
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

    @Override
    public boolean isActive() {
        return !readyToDispose;
    }



    private List<View> viewList = new LinkedList<>();
    private List<String> lines = new LinkedList<>();
    private boolean readyToDispose = false;


}
