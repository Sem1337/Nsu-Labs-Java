public class Push implements Command {
    public Push(String args) {
        this.args = args.split(" ");
        int cnt = this.args.length;
        if(cnt != 1)throw new WrongNumberOfArguments("wrong number of arguments in command PUSH.\nexpected 1. found " + cnt);
    }

    @Override
    public void execute(DataStorage data) {
        for (String arg: args) {
            data.getOperands().push(arg);
        }
    }

    private String[] args;
}
