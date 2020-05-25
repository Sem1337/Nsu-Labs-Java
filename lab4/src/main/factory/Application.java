package main.factory;

public class Application {
    public static void main(String[] args)  {
        Factory factory = new Factory();
        FactoryGUI factoryGUI = new FactoryGUI(factory);

        while(!Thread.currentThread().isInterrupted()) {
            factoryGUI.updateData();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
