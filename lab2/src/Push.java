public class Push implements Command {
    public Push(String[] args) {
        this.args = args;
    }

    @Override
    public void execute(DataStorage data) {
        for (String arg: args) {
            data.getOperands().push(arg);
        }
    }

    private String[] args;
}
