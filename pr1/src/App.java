import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        FileHandler handler = new FileHandler(args[0]);
        CSVWriter writer = new CSVWriter("out.csv");
        writer.write(handler.getFrequency(), handler.getWordsCount());
    }
}
