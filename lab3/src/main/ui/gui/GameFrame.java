package main.ui.gui;

import main.Application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.min;

public class GameFrame extends JFrame implements main.ui.GameFrame {

    public GameFrame(int width, int height, int rows, int cols) {
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("main.Minesweeper");
        this.setPreferredSize(new Dimension(width,height));

        topPanel = new JPanel();
        topPanel.setBackground(Color.RED);
        topPanel.setPreferredSize(new Dimension(width, (int) (height*verticalPartOfTopPanel)));

        centerPanel = new JPanel();
        centerPanel.setBackground(Color.YELLOW);
        centerPanel.setPreferredSize(new Dimension(width, (int) (height*verticalPartOfCenterPanel)));

        fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(rows,cols));
        fieldPanel.setBackground(Color.GRAY);
        int sz = min(centerPanel.getWidth() / cols, centerPanel.getHeight() / rows);
        fieldPanel.setPreferredSize(new Dimension(sz * cols, sz* rows));

        bufferedImages = new BufferedImage[rows][cols];
        setupField(rows,cols);

        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.GREEN);
        bottomPanel.setPreferredSize(new Dimension(width, (int) (height*verticalPartOfBottomPanel)));

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
        cachedCellsState = new int[rows][cols];
        for(int i =0 ;i<rows;i++){
            for(int j=0;j<cols;j++){
                cachedCellsState[i][j] = -2;
            }
        }

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
                try {
                    bufferedImages[row - 1][col - 1] = ImageIO.read(new File("resources/unchecked45.png"));
                } catch (IOException e) {
                    System.out.println("setup");
                }
                cells[row-1][col-1] = new JButton();
                int sz = fieldPanel.getHeight() / rows;
                cells[row-1][col-1].setPreferredSize(new Dimension(sz,sz));
                cells[row-1][col-1].setVisible(true);

                int finalRow = row;
                int finalCol = col;
                cells[row-1][col-1].addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);

                        JButton btn = (JButton) e.getComponent();
                        Dimension size = btn.getSize();
                        //Insets insets = btn.getInsets();
                        //size.width -= insets.left + insets.right;
                        //size.height -= insets.top + insets.bottom;
                        //System.out.println(size.width);
                        //System.out.println(size.height);
                        /*if (size.width > size.height) {
                            size.width = -1;
                        } else {
                            size.height = -1;
                        }*/
                        Image scaled = bufferedImages[finalRow- 1][finalCol - 1].getScaledInstance(size.width, size.height, Image.SCALE_FAST);
                        btn.setIcon(new ImageIcon(scaled));

                        /*var button = cells[finalRow-1][finalCol-1];
                        int sz = e.getComponent().getHeight();
                        button.setIcon(resizeIcon((ImageIcon) button.getIcon(),sz,sz));*/
                    }
                });

                fieldPanel.add(cells[row-1][col-1]);
            }
        }

    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        return icon;
        //Image img = icon.getImage();
        //Image newImg = img.getScaledInstance(width, height,  Image.SCALE_DEFAULT);
        //return new ImageIcon(newImg);
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
    private int[][] cachedCellsState;
    private BufferedImage[][] bufferedImages;

    @Override
    public void draw(int[][] cellsState, int[][] field) {
        int sz = fieldPanel.getHeight() / cellsState.length;
        for(int i = 0; i< cellsState.length; i++) {
            for(int j=0; j< cellsState[i].length; j++){
                if(cellsState[i][j] == 0 || cachedCellsState[i][j] == cellsState[i][j])continue;
                if(cellsState[i][j] == -1) {
                    System.out.println("here");
                    //cells[i][j].setBackground(Color.BLUE);
                    try {
                        bufferedImages[i][j] = ImageIO.read(new File("resources/flag30.png"));
                    } catch (IOException e){
                        System.out.println("draw");
                    }
                    //cells[i][j].setIcon(resizeIcon(new ImageIcon("resources/flag.png"),sz,sz));

                } else if(cellsState[i][j] == 1){

                }
            }
        }
        cachedCellsState = cellsState;
    }

}
