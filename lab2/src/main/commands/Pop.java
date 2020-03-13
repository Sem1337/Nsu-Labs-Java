package main.commands;

import main.Calculator;
import main.DataStorage;
import main.calcExceptions.NotEnoughOperands;
import main.calcExceptions.WrongNumberOfArguments;

import java.util.logging.Level;

public class Pop implements Command {

    public Pop(String args) {
        int cnt = args == null? 0 : args.split(" ").length;
        if(cnt != 0)throw new WrongNumberOfArguments("wrong number of arguments in command POP.\nexpected 0. found " + cnt);
    }

    @Override
    public void execute(DataStorage data) {
        if(data.getOperands().size() < 1)throw new NotEnoughOperands("not enough operands in stack for POP command: expected 1. found " + data.getOperands().size());
        String result = data.getOperands().pop();
        Calculator.LOGGER.log(Level.FINE, "pop: " + result);
    }

}
