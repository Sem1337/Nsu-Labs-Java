package main.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class AboutFrame extends JFrame implements main.ui.AboutFrame {

    public AboutFrame() {
        setTitle("about");
        System.out.println("here");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setBackground(Color.YELLOW);

        setupTextArea();
        this.setResizable(false);

        add(panel);

        setSize(500, 500);
        setVisible(true);

    }


    private void setupTextArea() {
        textArea = new JTextArea(25, 40);
        scroll = new JScrollPane(textArea);
        textArea.setEditable(false);
        List<String> contents = readRules();
        for (String line: contents) {
            textArea.append(line);
        }
        panel.add(scroll);
    }

    private List<String> readRules() {
        List<String> lines = new LinkedList<>();
        try (Scanner scanner = new Scanner(new File("resources/rules.txt"))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return lines;
    }

    @Override
    public synchronized boolean isActive() {
        return isShowing();
    }

    @Override
    public void update() {
        if (readyToDispose) dispose();
    }


    private boolean readyToDispose = false;
    private JTextArea textArea;
    private JScrollPane scroll;
    private JPanel panel;
}
