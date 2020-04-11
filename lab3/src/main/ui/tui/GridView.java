package main.ui.tui;

public class GridView implements View {

    GridView(Character[][] grid) {
        this.grid = grid;
    }


    @Override
    public void draw() {
        System.out.print("  ");
        for(int col = 0; col < grid[0].length; col++) {
            System.out.printf(" %2d", col+1);
        }
        System.out.println();
        for(int row = 0; row < grid.length; row++) {
            System.out.print("  ");
            for(int col = 0; col < grid[0].length*3 + 1; col++) {
                System.out.print('_');
            }
            System.out.println();
            System.out.printf("%2d", row+1);
            for(int col = 0; col < grid[0].length; col++) {
                System.out.print("| ");
                System.out.print(grid[row][col]);
            }
            System.out.println('|');
        }
        System.out.print("  ");
        for(int col = 0; col < grid[0].length* 3 + 1; col++) {
            System.out.print('_');
        }
        System.out.println();
    }

    private Character[][] grid;

}
