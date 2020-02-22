public class Push implements Command {
    public Push(String[] args) {
        this.args = args;
    }

    @Override
    public void execute(DataStorage data) {
    }

    private String[] args;
}
