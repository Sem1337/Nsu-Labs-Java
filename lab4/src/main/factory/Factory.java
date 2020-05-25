package main.factory;

import lombok.Getter;
import lombok.Setter;
import main.factory.details.Accessory;
import main.factory.details.Body;
import main.factory.details.Engine;
import main.threadpool.ThreadPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.lang.String.*;

class Factory {

    Factory() {

        try {
            FileHandler handler = new FileHandler("factory.log");
            handler.setFormatter(new SimpleFormatter());
            logger.setUseParentHandlers(false);
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        Properties properties = new Properties();
        try(InputStream input = new FileInputStream("src/main/resources/application.properties")){
            properties.load(input);
        }catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage());
        }
        loggingEnabled = Boolean.parseBoolean(properties.getProperty("LogSale"));
        threadPool = new ThreadPool(Integer.parseInt(properties.getProperty("threadsCount")));
        engineStorage = new Storage<>(Integer.parseInt(properties.getProperty("engineStorageSize")));
        bodyStorage = new Storage<>(Integer.parseInt(properties.getProperty("bodyStorageSize")));
        accessoryStorage = new Storage<>(Integer.parseInt(properties.getProperty("accessoryStorageSize")));
        carStorage = new Storage<>(Integer.parseInt(properties.getProperty("carStorageSize")));
        workers = new ArrayBlockingQueue<>(Integer.parseInt(properties.getProperty("workersCount")));
        new Thread(new StorageController<>(carStorage, this)).start();
        initSuppliers(Integer.parseInt(properties.getProperty("accessorySuppliersCount")));
        initDealers(Integer.parseInt(properties.getProperty("dealersCount")));
        initWorkers(Integer.parseInt(properties.getProperty("workersCount")));

    }



    private void initSuppliers(int accessorySuppliersCount) {
        Supplier<Engine> engineSupplier = () -> {
            try {
                Thread.sleep(supplierDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Engine engine = new Engine();
            if(loggingEnabled) {
                logger.log(Level.FINE, "new " + engine.getDescription());
            }
            return engine;
        };

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    engineStorage.putItem(engineSupplier.get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();


        Supplier<Body> bodySupplier = () -> {
            try {
                Thread.sleep(supplierDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Body body = new Body();
            if(loggingEnabled) {
                logger.log(Level.FINE, "new " + body.getDescription());
            }
            return body;
        };

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    bodyStorage.putItem(bodySupplier.get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        for(int i = 0; i < accessorySuppliersCount; i++) {
            accessorySuppliers.add(() -> {
                try {
                    Thread.sleep(supplierDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Accessory accessory = new Accessory();
                if(loggingEnabled) {
                    logger.log(Level.FINE, "new " + accessory.getDescription());
                }
                return accessory;
            });
        }

        for (Supplier<Accessory> supplier : accessorySuppliers) {
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        accessoryStorage.putItem(supplier.get());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }

    }

    private void initDealers(int dealersCount) {
        for(int i =0 ;i < dealersCount;i++) {
            int finalI = i;
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Car car;
                        synchronized (carStorage) {
                            car = carStorage.getItem();
                            carStorage.notifyAll();
                        }
                        if(loggingEnabled) {
                            logger.log(Level.FINE, "dealer " + finalI + " took " + car.getDescription());
                        }
                        Thread.sleep(dealerDelay);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }
    }

    private void initWorkers(int workersCount) {
        try {
            for (int i = 0; i < workersCount; i++) {
                workers.put(new Worker(i));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    void makeProducts(int count) {
        try {
            for (int i = 0; i < count; i++) {
                threadPool.execute(workers.take());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private LinkedList<Supplier<Accessory>>accessorySuppliers = new LinkedList<>();
    private BlockingQueue<Runnable> workers;
    @Getter
    private Storage<Engine>engineStorage;
    @Getter
    private Storage<Body>bodyStorage;
    @Getter
    private Storage<Accessory>accessoryStorage;
    @Getter
    private final Storage<Car>carStorage;
    @Setter
    @Getter
    private int supplierDelay = 500;
    @Setter
    @Getter
    private int dealerDelay = 2000;
    @Getter
    private Integer totalCarsProduced = 0;
    private final Logger logger = Logger.getLogger(Factory.class.getName());
    private ThreadPool threadPool;
    private boolean loggingEnabled;

    public class Worker implements Runnable {
        Worker(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                Body body = bodyStorage.getItem();
                Engine engine = engineStorage.getItem();
                Accessory accessory = accessoryStorage.getItem();
                Car car = new Car(engine, body, accessory);
                if(loggingEnabled) {
                    logger.log(Level.FINE, format("worker %d created new Car: %s", id, car.getDescription()));
                }
                carStorage.putItem(car);
                incTotalCarsCounter();
                workers.put(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private synchronized void incTotalCarsCounter() {
            totalCarsProduced++;
        }

        private int id;
    }

}
