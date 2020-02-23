public class Define implements Command {

    public Define(String [] args) {
        this.args = args;
    }

    @Override
    public void execute(DataStorage data) {
        data.getVariables().put(args[0], Double.valueOf(args[1]));
    }

    private String [] args;

}
