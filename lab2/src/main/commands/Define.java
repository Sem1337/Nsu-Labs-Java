package main.commands;

import main.Calculator;
import main.DataStorage;
import main.calcExceptions.IncorrectArgument;
import main.calcExceptions.WrongNumberOfArguments;

import java.util.logging.Level;
import java.util.regex.Pattern;

public class Define implements Command {

    public Define(String args) {
        this.args = args == null ? null: args.split(" ");
        int cnt = this.args == null ? 0 : this.args.length;
        if(cnt != 2)throw new WrongNumberOfArguments("wrong number of arguments in command Define.\nexpected 2. found " + cnt);
    }

    @Override
    public void execute(DataStorage data) {
        Double value = correctPattern.matcher(args[1]).matches()? Double.valueOf(args[1]) : null;
        if(value == null || Character.isDigit(args[0].charAt(0)))throw new IncorrectArgument("incorrect argument for Define command");
        Calculator.LOGGER.log(Level.FINE, "define " + args[0]  + " = " + args[1]);
        data.getVariables().put(args[0], Double.valueOf(args[1]));
    }

    private String [] args;
    Pattern correctPattern = Pattern.compile("-?\\d+(\\.\\d+)?(E-?\\d+)?");

}
