package main.ui.gui;

import main.ui.DTO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.min;

public class GameFrame extends JFrame implements main.ui.GameFrame {

    public GameFrame(int width, int height, int rows, int cols) {
        setSize(width, height);
        setMinimumSize(new Dimension(450,450));
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
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++){
                try {
                    bufferedImages[row][col] = ImageIO.read(new File("resources/unchecked45.png"));
                } catch (IOException e) {
                    System.out.println("setup");
                }
                cells[row][col] = new JButton();
                int sz = fieldPanel.getHeight() / rows;
                cells[row][col].setPreferredSize(new Dimension(sz,sz));
                cells[row][col].setVisible(true);

                Integer finalRow = row;
                Integer finalCol = col;
                cells[row][col].addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        //JButton btn = (JButton) e.getComponent();
                        Dimension size = cells[finalRow][finalCol].getSize();
                        updateCellImage(finalRow,finalCol,size);
                    }
                });

                cells[row][col].addMouseListener(new MouseAdapter() {
                    boolean pressed;
                    JButton button = cells[finalRow][finalCol];
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
                            if (SwingUtilities.isRightMouseButton(e)) {
                                dataBuffer = new DTO("setFlag", finalRow.toString(), finalCol.toString());
                            }
                            else {
                                dataBuffer = new DTO("openCell", finalRow.toString(), finalCol.toString());
                            }
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

                fieldPanel.add(cells[row][col]);
            }
        }
    }

    private void updateCellImage(int row, int col, Dimension sz) {
        Image scaled = bufferedImages[row][col].getScaledInstance(sz.width, sz.height, Image.SCALE_FAST);
        cells[row][col].setIcon(new ImageIcon(scaled));
    }

    private void resizeField(int width, int height) {
        fieldPanel.setPreferredSize(new Dimension(width,height));
    }


    @Override
    public void update() {

    }

    @Override
    public DTO requestData() {
        if(!dataBuffer.getDescription().equals("empty")){
            DTO sendCopy = new DTO(dataBuffer);
            dataBuffer = new DTO("empty");
            return sendCopy;
        } else {
            return dataBuffer;
        }
    }

    private DTO dataBuffer = new DTO("empty");
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
    public void draw(Integer[][] cellsState, int[][] field) {
        int sz = fieldPanel.getHeight() / cellsState.length;
        for(int i = 0; i< cellsState.length; i++) {
            for(int j=0; j< cellsState[i].length; j++){
                if(cachedCellsState[i][j] == cellsState[i][j])continue;
                if(cellsState[i][j] == 0) {
                    try {
                        bufferedImages[i][j] = ImageIO.read(new File("resources/unchecked45.png"));
                    } catch (IOException e){
                        System.out.println("draw");
                    }
                } else if(cellsState[i][j] == -1) {
                    try {
                        bufferedImages[i][j] = ImageIO.read(new File("resources/flag64.png"));
                    } catch (IOException e){
                        System.out.println("draw");
                    }

                } else if(cellsState[i][j] == 1) {
                    try {
                        bufferedImages[i][j] = ImageIO.read(new File("resources/mine80.jpg"));
                    } catch (IOException e){
                        System.out.println("draw");
                    }
                }
                if(cells[i][j].getWidth() != 0) updateCellImage(i,j, cells[i][j].getSize());
                cachedCellsState[i][j] = cellsState[i][j];
                
            }
        }
    }
}
