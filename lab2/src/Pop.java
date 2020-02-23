public class Pop implements Command {

    public Pop(String [] args) {
    }

    @Override
    public void execute(DataStorage data) {
        data.getOperands().pop();
    }

}
