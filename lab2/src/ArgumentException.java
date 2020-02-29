public class ArgumentException extends RuntimeException {
    public ArgumentException(String message) {
        this.message = message;
    }

    public ArgumentException() {
        this.message = "Argument exception";
    }

    @Override
    public String getLocalizedMessage() {
        return message;
    }

    private String message;
}
