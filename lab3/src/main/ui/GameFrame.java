package main.ui;

public interface GameFrame extends Frame {

    void draw(Integer[][] field);

    void restart(int rows,int cols);


}
