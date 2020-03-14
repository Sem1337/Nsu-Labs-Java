package main.commands;

import main.DataStorage;
import main.calcExceptions.WrongNumberOfArguments;

import java.util.regex.Pattern;

public class Print implements Command {

    public Print(String args) {
        int cnt = args == null? 0 : args.split(" ").length;
        if(cnt != 0)throw new WrongNumberOfArguments("wrong number of arguments in command PRINT.\nexpected 0. found " + cnt);
    }

    @Override
    public void execute(DataStorage data) {
        if(data.getOperands().empty()){
            System.out.println("stack is empty");
            return;
        }
        String v = data.getOperands().pop();
        //Calculator.LOGGER.log(Level.FINE, "print: " + v);
        if(correctPattern.matcher(v).matches()) {
            System.out.println(v);
        } else {
            System.out.println(v + " == " + data.getVariables().get(v));
        }
    }

    Pattern correctPattern = Pattern.compile("-?\\d+(\\.\\d+)?(E-?\\d+)?");

    @Override
    public String toString() {
        return "Print command";
    }

}
