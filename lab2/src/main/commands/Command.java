package main.commands;

import main.DataStorage;

public interface Command {
    public void execute(DataStorage data);
}
