import java.util.regex.Pattern;

public class Print implements Command {

    public Print(String [] args) {
    }

    @Override
    public void execute(DataStorage data) {
        String v = data.getOperands().pop();
        if(correctPattern.matcher(v).matches()) {
            System.out.println(v);
        } else{
            System.out.println(v + " == " + data.getVariables().get(v));
        }
    }

    Pattern correctPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
}