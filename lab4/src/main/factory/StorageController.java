package main.factory;


import lombok.Getter;

public class StorageController<T> implements  Runnable{

    StorageController(Storage<T> storage, Factory factory) {
        this.storage = storage;
        this.factory = factory;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int newWorkersCount = storage.getStorage().remainingCapacity();
                factory.makeProducts(newWorkersCount);
                if(newWorkersCount == 0) {
                    synchronized (storage) {
                        storage.wait();
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Getter
    private int waitingNotify = 0;
    private final Factory factory;
    private final Storage<T> storage;

}
