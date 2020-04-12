package main.ui.tui;

import main.ui.DTO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SetupFrame implements main.ui.SetupFrame {

    public SetupFrame() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        viewList.add(new MessageView("Commands: 'cancel',  'rows cols mines'\n( " + minDimension + " <= dimension <= " + maxDimension + " 0 <= mines <  rows * columns)\n:"));

    }

    @Override
    public void update() {
        for(int i = 0; i< 10;i++) System.out.println();

        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        for (var view: viewList) {
            view.draw();
        }

    }

    @Override
    public DTO requestData() {


        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        if(command.isEmpty()) return new DTO("empty");
        if(command.equals("cancel")) {
            readyToDispose = true;
            return new DTO("cancelSettings");
        } else {
            try {
                String [] values = command.split(" ");
                if(values.length != 3)throw  new Exception("incorrect input");
                String rowsCount = values[0];
                String colsCount = values[1];
                String minesCount = values[2];

                int cols = Integer.parseInt(colsCount);
                int rows =Integer.parseInt(rowsCount);
                int mines = Integer.parseInt(minesCount);
                if(cols > maxDimension || cols < minDimension || rows > maxDimension || rows < minDimension || mines < 0 || mines >= cols*rows) throw new Exception("incorrect input");
                readyToDispose = true;
                return new DTO("confirmSettings", rowsCount, colsCount, minesCount);

            }catch(Exception ex) {
                return new DTO("empty");
            }
        }
    }

    @Override
    public boolean willDisappear() {
        return readyToDispose;
    }



    private int minDimension = 1;
    private int maxDimension = 50;
    private List<View> viewList = new LinkedList<>();
    private boolean readyToDispose = false;

}
