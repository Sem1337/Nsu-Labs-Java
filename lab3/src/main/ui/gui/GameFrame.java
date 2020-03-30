package main.ui.gui;

import main.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static java.lang.Math.min;

public class GameFrame extends JFrame implements main.ui.GameFrame {

    public GameFrame(int width, int height, int rows, int cols) {
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("main.Minesweeper");

        topPanel = new JPanel();
        topPanel.setBackground(Color.RED);

        centerPanel = new JPanel();
        centerPanel.setBackground(Color.YELLOW);

        fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(rows,cols));
        fieldPanel.setBackground(Color.GRAY);
        setupField(rows,cols);

        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.GREEN);

        centerPanel.setVisible(true);
        bottomPanel.setVisible(true);
        topPanel.setVisible(true);
        fieldPanel.setVisible(true);
        centerPanel.add(fieldPanel);

        centerPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int cellSize = min(e.getComponent().getWidth() / cols, e.getComponent().getHeight() / rows);
                resizeField(cellSize * cols, cellSize * rows);
                centerPanel.updateUI();
                //SwingUtilities.updateComponentTreeUI(GameFrame.this);
            }
        });

        add(topPanel);
        add(bottomPanel);
        add(centerPanel);
        configureConstraints();
        setVisible(true);
        //fieldPanel.setSize(centerPanel.getWidth(), centerPanel.getHeight());
    }

    private void setupButtons(int rows, int cols) {

    }

    private void configureConstraints() {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        SpringLayout centerLayout = new SpringLayout();
        centerPanel.setLayout(centerLayout);

        centerLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, fieldPanel, 0,SpringLayout.HORIZONTAL_CENTER, centerPanel);
        centerLayout.putConstraint(SpringLayout.VERTICAL_CENTER, fieldPanel, 0, SpringLayout.VERTICAL_CENTER, centerPanel);
        //centerLayout.putConstraint(SpringLayout.WIDTH, fieldPanel, 0,SpringLayout.HEIGHT, fieldPanel);

        layout.putConstraint(SpringLayout.NORTH, centerPanel, 0,SpringLayout.SOUTH,topPanel);
        layout.putConstraint(SpringLayout.SOUTH, bottomPanel,0,SpringLayout.SOUTH, this.getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, centerPanel, 0,SpringLayout.NORTH, bottomPanel);
        layout.putConstraint(SpringLayout.NORTH, topPanel,0,SpringLayout.NORTH, this.getContentPane());
        layout.putConstraint(SpringLayout.WIDTH, topPanel,0,SpringLayout.WIDTH,this.getContentPane());
        layout.putConstraint(SpringLayout.WIDTH, bottomPanel,0,SpringLayout.WIDTH,topPanel);
        layout.putConstraint(SpringLayout.WIDTH, centerPanel,0,SpringLayout.WIDTH,topPanel);

    }

    private void setupField(int rows,int cols){
        cells = new JButton[rows][cols];
        for(int row = 1; row <= rows; row++) {
            for(int col = 1; col <= cols; col++){

                cells[row-1][col-1] = new JButton(new ImageIcon("resources/unchecked.png"));
                cells[row-1][col-1].setVisible(true);
                fieldPanel.add(cells[row-1][col-1]);
            }
        }

    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    private void resizeField(int width, int height) {
        fieldPanel.setPreferredSize(new Dimension(width,height));
    }


    @Override
    public void update() {

    }


    private double verticalPartOfTopPanel = 0.1;
    private double verticalPartOfBottomPanel = 0.1;
    private double verticalPartOfCenterPanel = 0.8;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel fieldPanel;
    private JPanel bottomPanel;
    private JButton[][] cells;

    @Override
    public void draw(int[][] cellsState, int[][] field) {
        int sz = fieldPanel.getHeight() / cellsState.length;
        for(int i = 0; i< cellsState.length; i++) {
            for(int j=0; j< cellsState[i].length; j++){
                if(cellsState[i][j] == 0)continue;
                if(cellsState[i][j] == -1) {
                    //System.out.println("here");
                    //cells[i][j].setBackground(Color.BLUE);
                    cells[i][j].setIcon(resizeIcon(new ImageIcon("resources/flag.png"),sz,sz));

                } else if(cellsState[i][j] == 1){

                }
            }
        }
        fieldPanel.updateUI();
    }

}
