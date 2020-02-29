import java.util.regex.Pattern;

public class Multiplication implements Command {

    public Multiplication(String args) {
        int cnt = args.split(" ").length;
        if(!args.isEmpty())throw new WrongNumberOfArguments("wrong number of arguments in command Multiplication.\nexpected 0. found " + cnt);
    }

    @Override
    public void execute(DataStorage data) {
        if(data.getOperands().size() < 2)throw new NotEnoughOperands("not enough operands in stack for Multiplication command: expected 2. found " + data.getOperands().size());
        String v1 = data.getOperands().pop();
        String v2 = data.getOperands().pop();
        Double operand1 = correctPattern.matcher(v1).matches()? Double.valueOf(v1) : data.getVariables().get(v1);
        Double operand2 = correctPattern.matcher(v2).matches()? Double.valueOf(v2): data.getVariables().get(v2);
        if(operand1 == null || operand2 == null) throw new IncorrectArgument("incorrect argument for Multiplication command");
        double result = operand1 * operand2;
        if(!Double.isFinite(result)) throw new IncorrectResult("incorrect result if execute Multiplication command with args" + operand1 + " " + operand2);
        data.getOperands().push(Double.toString(result));
    }

    Pattern correctPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
}
