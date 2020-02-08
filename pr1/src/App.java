import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        String inputFileName = args.length > 0 ? args[0] : "input.txt";
        FileHandler handler = new FileHandler(inputFileName);
        CSVWriter writer = new CSVWriter("out.csv");
        writer.write(handler.getFrequency(), handler.getWordsCount());
    }
}
