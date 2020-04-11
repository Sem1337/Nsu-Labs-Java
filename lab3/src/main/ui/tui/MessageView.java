package main.ui.tui;

public class MessageView implements View {

    MessageView(String message) {
        this.message = message;

    }

    @Override
    public void draw() {
        System.out.println(message);
    }


    private String message;
}
