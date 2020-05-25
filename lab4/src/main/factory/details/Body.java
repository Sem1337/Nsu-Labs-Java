package main.factory.details;

import java.util.UUID;

public class Body implements Detail {
    public Body() {
        id = UUID.randomUUID();
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return type + ": " + id;
    }

    private UUID id;
    private String type = "Body";

}
