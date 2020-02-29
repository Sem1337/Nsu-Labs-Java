import java.util.regex.Pattern;

public class Define implements Command {

    public Define(String args) {
        this.args = args.split(" ");
        if(this.args.length != 2)throw new WrongNumberOfArguments("wrong number of arguments in command Define.\nexpected 2. found " + this.args.length);
    }

    @Override
    public void execute(DataStorage data) {
        Double value = correctPattern.matcher(args[1]).matches()? Double.valueOf(args[1]) : null;
        if(value == null || args[0].startsWith("[0-9]"))throw new IncorrectArgument("incorrect argument for Define command");
        data.getVariables().put(args[0], Double.valueOf(args[1]));
    }

    private String [] args;
    Pattern correctPattern = Pattern.compile("-?\\d+(\\.\\d+)?");

}
