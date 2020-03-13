package main.calcExceptions;

public class DivisionByZero extends ArithmeticException {
    public DivisionByZero() {
        super();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage() + ": Division by zero";
    }
}
