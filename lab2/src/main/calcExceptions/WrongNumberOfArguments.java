package main.calcExceptions;

public class WrongNumberOfArguments extends ArgumentException {
    public WrongNumberOfArguments(String message) {
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
