package main.factory;

import main.factory.details.Accessory;
import main.factory.details.Body;
import main.factory.details.Engine;

import java.util.UUID;

class Car {

    Car(Engine engine, Body body, Accessory accessory) {
        this.engine = engine;
        this.body = body;
        this.accessory = accessory;
        id = UUID.randomUUID();
    }

    String getDescription() {
        return "( car: " + id + " " + engine.getDescription() + " " + body.getDescription() + " " + accessory.getDescription() + " )";
    }

    private Engine engine;
    private Body body;
    private Accessory accessory;
    private UUID id;
}
