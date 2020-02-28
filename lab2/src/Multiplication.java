import java.util.regex.Pattern;

public class Multiplication implements Command {

    public Multiplication(String args) {
    }

    @Override
    public void execute(DataStorage data) {
        String v1 = data.getOperands().pop();
        String v2 = data.getOperands().pop();
        double operand1 = correctPattern.matcher(v1).matches()? Double.valueOf(v1) : data.getVariables().get(v1);
        double operand2 = correctPattern.matcher(v2).matches()? Double.valueOf(v2): data.getVariables().get(v2);
        double result = operand1 * operand2;
        data.getOperands().push(Double.toString(result));
    }

    Pattern correctPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
}
