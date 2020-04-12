package main.ui.gui;

import main.ui.DTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SetupFrame extends JFrame implements main.ui.SetupFrame {

    public SetupFrame() {
        setTitle("settings");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        this.setResizable(false);
        setBackground(Color.ORANGE);
        setupTextFields();
        setupButtons();
        setupLabels();
        contents = new JPanel();
        contents.add(colsCountText);
        contents.add(rowsCountText);
        contents.add(minesCountText);
        contents.add(colsLabel);
        contents.add(rowsLabel);
        contents.add(minesLabel);
        contents.add(cancelButton);
        contents.add(confirmButton);

        configureConstraints();
        setContentPane(contents);
        contents.setBackground(Color.YELLOW);
        setSize(400, 400);
        setVisible(true);
    }

    private void setupTextFields() {
        colsCountText = new JTextField(5);
        rowsCountText = new JTextField(5);
        minesCountText = new JTextField(5);


        rowsCountText.setFont(new Font("Dialog", Font.PLAIN, 30));
        colsCountText.setFont(new Font("Dialog", Font.PLAIN, 30));
        minesCountText.setFont(new Font("Dialog", Font.PLAIN, 30));
    }

    private void setupButtons() {
        cancelButton = new JButton();
        cancelButton.setText("cancel");

        confirmButton = new JButton();
        confirmButton.setText("confirm");

        cancelButton.addMouseListener(new MouseAdapter() {
            boolean pressed;
            JButton button = cancelButton;
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
                    dataBuffer = new DTO("cancelSettings");
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


        confirmButton.addMouseListener(new MouseAdapter() {
            boolean pressed;
            JButton button = confirmButton;
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

                    try {
                        if(colsCountText.getText().isEmpty() || rowsCountText.getText().isEmpty() || minesCountText.getText().isEmpty()) throw new Exception("fill all fields");
                        int cols = Integer.parseInt(colsCountText.getText());
                        int rows =Integer.parseInt(rowsCountText.getText());
                        int mines = Integer.parseInt(minesCountText.getText());
                        if(cols > maxDimension || cols < minDimension || rows > maxDimension || rows < minDimension || mines < 0 || mines >= cols*rows) throw new Exception("incorrect input: " + minDimension + " <= dimension <= " + maxDimension + "\n 0 <= mines < rows * columns");
                    }catch(Exception ex) {
                        JOptionPane.showMessageDialog(SetupFrame.this,  ex.getMessage());
                        return;
                    }
                    dataBuffer = new DTO("confirmSettings" , rowsCountText.getText(), colsCountText.getText(), minesCountText.getText());
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

    private void configureConstraints() {
        SpringLayout layout = new SpringLayout();
        contents.setLayout(layout);

        layout.putConstraint(SpringLayout.EAST, rowsLabel, -100 , SpringLayout.HORIZONTAL_CENTER, contents);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, rowsLabel, 0 , SpringLayout.VERTICAL_CENTER, rowsCountText);

        layout.putConstraint(SpringLayout.WEST, colsLabel, 0, SpringLayout.WEST, rowsLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, colsLabel, 0 , SpringLayout.VERTICAL_CENTER, colsCountText);

        layout.putConstraint(SpringLayout.WEST,  minesLabel, 0, SpringLayout.WEST, rowsLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, minesLabel, 0 , SpringLayout.VERTICAL_CENTER, minesCountText);

        layout.putConstraint(SpringLayout.WEST, rowsCountText, 0,SpringLayout.EAST, rowsLabel);
        layout.putConstraint(SpringLayout.NORTH, rowsCountText, 0 , SpringLayout.NORTH,  contents);

        layout.putConstraint(SpringLayout.WEST, colsCountText, 0,SpringLayout.EAST, colsLabel);
        layout.putConstraint(SpringLayout.NORTH, colsCountText, 5, SpringLayout.SOUTH, rowsCountText);

        layout.putConstraint(SpringLayout.WEST, minesCountText, 0,SpringLayout.EAST, minesLabel);
        layout.putConstraint(SpringLayout.NORTH, minesCountText, 5 , SpringLayout.SOUTH, colsCountText);

        layout.putConstraint(SpringLayout.WEST, cancelButton, 40,SpringLayout.WEST, rowsLabel);
        layout.putConstraint(SpringLayout.NORTH, cancelButton, 30 , SpringLayout.SOUTH, minesCountText);

        layout.putConstraint(SpringLayout.WEST, confirmButton, 10,SpringLayout.EAST, cancelButton);
        layout.putConstraint(SpringLayout.NORTH, confirmButton, 0 , SpringLayout.NORTH, cancelButton);

    }

    private void setupLabels() {
        colsLabel = new JLabel("columns: ");
        rowsLabel = new JLabel("rows: ");
        minesLabel = new JLabel("mines count: ");
    }

    @Override
    public boolean willDisappear() {
        return readyToDispose || !isShowing();
    }

    @Override
    public void update() {

        if(readyToDispose) {
            dispose();
        }

    }

    @Override
    public DTO requestData() {
        if(!dataBuffer.getDescription().equals("empty")){
            DTO sendCopy = new DTO(dataBuffer);
            dataBuffer = new DTO("empty");
            readyToDispose = true;
            return sendCopy;
        } else {
            return dataBuffer;
        }
    }


    private JPanel contents;
    private DTO dataBuffer = new DTO("empty");
    private JTextField colsCountText;
    private JTextField rowsCountText;
    private JTextField minesCountText;
    private JButton cancelButton;
    private JButton confirmButton;
    private JLabel colsLabel;
    private JLabel rowsLabel;
    private JLabel minesLabel;
    private int minDimension = 1;
    private int maxDimension = 50;

    private boolean readyToDispose = false;

}
