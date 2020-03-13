package main.commands;

import main.*;
import main.calcExceptions.*;

import java.util.logging.Level;
import java.util.regex.Pattern;

public class Division implements Command {

    public Division(String args){
        int cnt = args == null? 0 : args.split(" ").length;
        if(cnt != 0)throw new WrongNumberOfArguments("wrong number of arguments in command Division.\nexpected 0. found " + cnt);
    }

    @Override
    public void execute(DataStorage data) {
        if(data.getOperands().size() < 2)throw new NotEnoughOperands("not enough operands in stack for Division command: expected 2. found " + data.getOperands().size());
        String v1 = data.getOperands().pop();
        String v2 = data.getOperands().pop();
        Double operand1 = correctPattern.matcher(v1).matches()? Double.valueOf(v1) : data.getVariables().get(v1);
        Double operand2 = correctPattern.matcher(v2).matches()? Double.valueOf(v2): data.getVariables().get(v2);
        if(operand1 == null || operand2 == null) throw new IncorrectArgument("incorrect argument for Division command");
        double result = operand2 / operand1;
        if(operand1 == 0.0)throw new DivisionByZero();
        if(!Double.isFinite(result)) throw new IncorrectResult("incorrect result if execute Division command with args" + operand1 + " " + operand2);
        Calculator.LOGGER.log(Level.FINE, "result = " + String.valueOf(result));
        data.getOperands().push(Double.toString(result));
    }

    Pattern correctPattern = Pattern.compile("-?\\d+(\\.\\d+)?(E-?\\d+)?");
}
