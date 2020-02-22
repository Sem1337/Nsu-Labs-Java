public class Pop implements Command {

    public Pop(String [] args) {
        this.args = args;
    }

    @Override
    public void execute(DataStorage data) {
    }

    private String[] args;
}
