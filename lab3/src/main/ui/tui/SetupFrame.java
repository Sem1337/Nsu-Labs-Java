package main.ui.tui;

import main.Minesweeper;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SetupFrame implements main.ui.SetupFrame {

    public SetupFrame(Minesweeper gameModel) {
        this.gameModel = gameModel;
        viewList.add(new MessageView("Commands: 'cancel',  'rows cols mines'\n( " + minDimension + " <= dimension <= " + maxDimension + " 0 <= mines <  rows * columns)\n:"));

    }

    @Override
    public void update() {

        System.out.println();

        for (var view: viewList) {
            view.draw();
        }

        requestData();

    }

    private void requestData() {


        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        if(command.equals("cancel")) {
            readyToDispose = true;
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
                gameModel.setParameters(rows,cols,mines);
                readyToDispose = true;

            }catch(Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    @Override
    public boolean isActive() {
        return !readyToDispose;
    }


    private Minesweeper gameModel;
    private int minDimension = 1;
    private int maxDimension = 50;
    private List<View> viewList = new LinkedList<>();
    private boolean readyToDispose = false;

}
