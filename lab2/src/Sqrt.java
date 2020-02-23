import java.util.regex.Pattern;

public class Sqrt implements Command {

    public Sqrt(String [] args){
    }

    @Override
    public void execute(DataStorage data) {
        String v = data.getOperands().pop();
        double operand = correctPattern.matcher(v).matches()? Double.valueOf(v) : data.getVariables().get(v);
        double result = Math.sqrt(operand);
        data.getOperands().push(Double.toString(result));
    }

    Pattern correctPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
}
