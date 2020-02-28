public class Application {
    public static void main(String[] args) {
        CommandFactory commandFactory = new CommandFactory(args[0]);
        Calculator calculator = new Calculator(args[1]);
        calculator.run(commandFactory);
    }
}
