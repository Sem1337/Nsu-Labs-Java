package main.ui;

import main.Minesweeper;

public class FrameFactory {


    private static final boolean GUI = false;

    public static GameFrame getGameFrame(Minesweeper gameModel) {
        if(GUI) return new main.ui.gui.GameFrame(gameModel);
        else return new main.ui.tui.GameFrame(gameModel);
    }

    public static SetupFrame getSetupFrame(Minesweeper gameModel) {
        if(GUI) return new main.ui.gui.SetupFrame(gameModel);
        else return new main.ui.tui.SetupFrame(gameModel);
    }

    public static HighscoresFrame getHighScoresFrame(String filename) {
        if(GUI) return new main.ui.gui.HighscoresFrame(filename);
        else return new main.ui.tui.HighscoresFrame(filename);
    }

    public static AboutFrame getAboutFrame() {
        if(GUI) return new main.ui.gui.AboutFrame();
        else return new main.ui.tui.AboutFrame();
    }


}
