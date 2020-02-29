public class ArithmeticException extends RuntimeException {
    public ArithmeticException(String message) {
        this.message = message;
    }

    public ArithmeticException() {
        this.message = "Arithmetic exception";
    }

    @Override
    public String getLocalizedMessage() {
        return message;
    }

    private String message;
}
