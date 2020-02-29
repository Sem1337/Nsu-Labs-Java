import java.util.regex.Pattern;

public class Sqrt implements Command {

    public Sqrt(String args) {
        int cnt = args.split(" ").length;
        if(!args.isEmpty())throw new WrongNumberOfArguments("wrong number of arguments in command SQRT.\nexpected 0. found " + cnt);
    }

    @Override
    public void execute(DataStorage data) {
        if(data.getOperands().size() < 1)throw new NotEnoughOperands("not enough operands in stack for SQRT command: expected 1. found " + data.getOperands().size());
        String v = data.getOperands().pop();
        Double operand = correctPattern.matcher(v).matches()? Double.valueOf(v) : data.getVariables().get(v);
        if(operand == null) throw new IncorrectArgument("Wrong argument for SQRT command");
        double result = Math.sqrt(operand);
        data.getOperands().push(Double.toString(result));
    }

    Pattern correctPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
}
