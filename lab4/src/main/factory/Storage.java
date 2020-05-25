package main.factory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Storage <T> {

    Storage(int size) {
        details = new ArrayBlockingQueue<>(size);
    }

    T getItem() throws InterruptedException {
        return details.take();
    }
    void putItem(T detail) throws InterruptedException {
        details.put(detail);
    }

    BlockingQueue<T> getStorage() {
        return details;
    }

    private BlockingQueue<T> details;

}
