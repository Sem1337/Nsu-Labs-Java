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

        bufferedCellImages = new BufferedImage[rows][cols];
        setupField(rows,cols);

        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.GREEN);
        bottomPanel.setPreferredSize(new Dimension(width, (int) (height*verticalPartOfBottomPanel)));

        centerPanel.setVisible(true);
        bottomPanel.setVisible(true);
        topPanel.setVisible(true);
        fieldPanel.setVisible(true);
        centerPanel.add(fieldPanel);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int width = e.getComponent().getWidth();
                int height = e.getComponent().getHeight();
                bottomPanel.setPreferredSize(new Dimension(width, (int) (height*verticalPartOfBottomPanel)));
                centerPanel.setPreferredSize(new Dimension(width, (int) (height*verticalPartOfCenterPanel)));
                topPanel.setPreferredSize(new Dimension(width, (int) (height*verticalPartOfTopPanel)));
            }
        });

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

        setupButtons();
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

    private void setupButtons() {
        try {
            bufferedRetryImage = ImageIO.read(new File("resources/retry256.png"));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        retryButton = new JButton();
        retryButton.setVisible(true);

        retryButton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Dimension size = e.getComponent().getSize();
                updateButtonImage(retryButton, bufferedRetryImage, size);
            }
        });

        retryButton.addMouseListener(new MouseAdapter() {
            boolean pressed;
            JButton button = retryButton;
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
                    dataBuffer = new DTO("retry");
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

        topPanel.add(retryButton);


    }


    private void updateButtonImage(JButton button, Image img, Dimension size) {
        Image scaled = img.getScaledInstance(size.width, size.height, Image.SCALE_FAST);
        button.setIcon(new ImageIcon(scaled));
    }

    private void configureConstraints() {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        SpringLayout centerLayout = new SpringLayout();
        centerPanel.setLayout(centerLayout);
        SpringLayout topLayout = new SpringLayout();
        topPanel.setLayout(topLayout);

        topLayout.putConstraint(SpringLayout.WEST, retryButton, 3, SpringLayout.WEST, topPanel);
        topLayout.putConstraint(SpringLayout.NORTH, retryButton, 3, SpringLayout.NORTH, topPanel);
        topLayout.putConstraint(SpringLayout.WIDTH, retryButton, 0, SpringLayout.HEIGHT, retryButton);
        topLayout.putConstraint(SpringLayout.HEIGHT, retryButton, -6, SpringLayout.HEIGHT, topPanel);

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
                setupCell(row,col);
                fieldPanel.add(cells[row][col]);
            }
        }
    }

    private void setupCell(Integer row, Integer col) {
        try {
            bufferedCellImages[row][col] = ImageIO.read(new File("resources/unchecked45.png"));
        } catch (IOException e) {
            System.out.println("setup");
        }
        cells[row][col] = new JButton();
        cells[row][col].setVisible(true);

        cells[row][col].addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Dimension size = cells[row][col].getSize();
                updateButtonImage(cells[row][col], bufferedCellImages[row][col], size);
            }
        });

        cells[row][col].addMouseListener(new MouseAdapter() {
            boolean pressed;
            JButton button = cells[row][col];
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
                        dataBuffer = new DTO("setFlag", row.toString(), col.toString());
                    }
                    else {
                        dataBuffer = new DTO("openCell", row.toString(), col.toString());
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
    }



    private void updateCellImage(int row, int col, Dimension sz) {
        Image scaled = bufferedCellImages[row][col].getScaledInstance(sz.width, sz.height, Image.SCALE_FAST);
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
    private double verticalPartOfTopPanel = 0.07;
    private double verticalPartOfBottomPanel = 0.07;
    private double verticalPartOfCenterPanel = 0.86;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel fieldPanel;
    private JPanel bottomPanel;
    private JButton[][] cells;
    private int[][] cachedCellsState;
    private BufferedImage[][] bufferedCellImages;
    private BufferedImage bufferedRetryImage;
    private JButton retryButton;


    @Override
    public void draw(Integer[][] field) {

        for(int i = 0; i< field.length; i++) {
            for(int j=0; j< field[i].length; j++){
                if(cachedCellsState[i][j] == field[i][j])continue;
                if(field[i][j] == -3) {
                    try {
                        bufferedCellImages[i][j] = ImageIO.read(new File("resources/flag64.png"));
                    } catch (IOException e){
                        System.out.println("draw");
                    }

                } else if(field[i][j] == -2) {
                    try {
                        bufferedCellImages[i][j] = ImageIO.read(new File("resources/unchecked45.png"));
                    } catch (IOException e){
                        System.out.println("draw");
                    }
                }
                else if(field[i][j] == -1) {
                    try {
                        bufferedCellImages[i][j] = ImageIO.read(new File("resources/mine80.jpg"));
                        cells[i][j].setBackground(Color.RED);
                    } catch (IOException e){
                        System.out.println("draw");
                    }
                } else if(field[i][j] == -4) {
                    try {
                        bufferedCellImages[i][j] = ImageIO.read(new File("resources/red_mine80.jpg"));
                        cells[i][j].setBackground(Color.RED);
                    } catch (IOException e){
                        System.out.println("draw");
                    }
                } else {
                    try {
                        bufferedCellImages[i][j] = ImageIO.read(new File("resources/" + field[i][j] + ".png"));
                    } catch (IOException e){
                        System.out.println("draw");
                    }
                }
                if(cells[i][j].getWidth() != 0) updateButtonImage(cells[i][j], bufferedCellImages[i][j], cells[i][j].getSize());
                cachedCellsState[i][j] = field[i][j];
            }
        }
    }
}
