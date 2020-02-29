public class NotEnoughOperands extends RuntimeException {
    public NotEnoughOperands(String message) {
            this.message = message;
    }


    @Override
    public String getLocalizedMessage() {
        return message;
    }

    private String message;
}
