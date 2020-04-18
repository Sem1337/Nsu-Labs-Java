package main;

public class Timer {

    public Timer() {
        currentTime = 0;
        firstTime = 0;
        lastTime = 0;
    }

    public void start() {
        currentTime = 0;
        resume();
    }

    public void resume() {
        firstTime = System.currentTimeMillis() / 1000;
    }

    public void stop() {
        update();
        getCurrentTime();
    }

    private void update() {
        lastTime = System.currentTimeMillis() / 1000;
    }


    public Long getCurrentTime() {
        update();
        currentTime += lastTime - firstTime;
        firstTime = lastTime;
        return currentTime;
    }

    private long currentTime;
    private long firstTime;
    private long lastTime;
}
