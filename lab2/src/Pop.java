public class Pop implements Command {

    public Pop(String args) {
        int cnt = args.split(" ").length;
        if(!args.isEmpty())throw new WrongNumberOfArguments("wrong number of arguments in command POP.\nexpected 0. found " + cnt);
    }

    @Override
    public void execute(DataStorage data) {
        if(data.getOperands().size() < 1)throw new NotEnoughOperands("not enough operands in stack for POP command: expected 1. found " + data.getOperands().size());
        data.getOperands().pop();
    }

}
