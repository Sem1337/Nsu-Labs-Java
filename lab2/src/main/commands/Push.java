package main.commands;

import main.Calculator;
import main.DataStorage;
import main.calcExceptions.WrongNumberOfArguments;

import java.util.logging.Level;

public class Push implements Command {
    public Push(String args) {
        this.args = args == null ? null: args.split(" ");
        int cnt = this.args == null ? 0 : this.args.length;
        if(cnt != 1)throw new WrongNumberOfArguments("wrong number of arguments in command PUSH.\nexpected 1. found " + cnt);
    }

    @Override
    public void execute(DataStorage data) {
        for (String arg: args) {
            data.getOperands().push(arg);
            Calculator.LOGGER.log(Level.FINE, "push: " + arg);
        }
    }

    private String[] args;
}
