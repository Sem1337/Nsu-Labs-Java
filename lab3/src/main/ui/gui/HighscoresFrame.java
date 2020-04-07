package main.ui.gui;

import main.ui.DTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class HighscoresFrame extends JFrame implements main.ui.HighscoresFrame {

    public HighscoresFrame(String fileName) {
        this.fileName = fileName;
        readHighscoresFile();

        setTitle("highscores");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        listPanel = new JPanel();
        bottomPanel = new JPanel();
        listPanel.setBackground(Color.YELLOW);
        bottomPanel.setBackground(Color.ORANGE);

        setupTextArea();
        setupButtons();
        this.setResizable(false);


        bottomPanel.add(clearButton);
        add(listPanel);
        add(bottomPanel);

        configureConstraints();

        setSize(500, 500);
        setVisible(true);
    }

    private void setupTextArea() {
        textArea = new JTextArea(25,20);
        scroll = new JScrollPane(textArea);
        textArea.setEditable(false);
        for (String line: lines) {
            textArea.append(line);
        }

        listPanel.add(scroll);

    }

    private void setupButtons() {
        clearButton = new JButton("clear");
        clearButton.addMouseListener(new MouseAdapter() {
            boolean pressed;
            JButton button = clearButton;
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                button.getModel().setArmed(true);
                button.getModel().setPressed(true);
                pressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                button.getModel().setArmed(false);
                button.getModel().setPressed(false);
                if (pressed) {
                    File file = new File(fileName);
                    file.delete();
                    lines.clear();
                    textArea.setText("");
                }
                pressed = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pressed = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                pressed = false;
            }
        });
    }


    private void readHighscoresFile() {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void configureConstraints() {
        SpringLayout mainLayout = new SpringLayout();
        setLayout(mainLayout);

        listPanel.setLayout(new FlowLayout());
        bottomPanel.setLayout(new FlowLayout());
        mainLayout.putConstraint(SpringLayout.NORTH, listPanel, 0 , SpringLayout.NORTH, this.getContentPane());
        mainLayout.putConstraint(SpringLayout.WIDTH, listPanel, 0 , SpringLayout.WIDTH, this.getContentPane());
        mainLayout.putConstraint(SpringLayout.SOUTH, bottomPanel, 0 , SpringLayout.SOUTH, this.getContentPane());
        mainLayout.putConstraint(SpringLayout.WIDTH, bottomPanel,0,SpringLayout.WIDTH, this.getContentPane());
        mainLayout.putConstraint(SpringLayout.NORTH, bottomPanel, 0, SpringLayout.SOUTH, listPanel);


    }

    @Override
    public void update() {

    }

    @Override
    public DTO requestData() {
        return new DTO("empty");
    }

    @Override
    public boolean willDisappear() {
        return !isShowing();
    }

    private JTextArea textArea;
    private JScrollPane scroll;
    private List<String> lines = new LinkedList<>();
    private String fileName;
    private JPanel listPanel;
    private JPanel bottomPanel;
    private JButton clearButton;

}
