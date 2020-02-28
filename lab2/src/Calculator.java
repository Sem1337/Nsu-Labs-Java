import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Calculator {
    public Calculator(String fileName) {
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
            scanner = new Scanner(System.in);
        }
    }

    public Calculator() {
        scanner = new Scanner(System.in);
    }

    void run(CommandFactory factory) {
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] splittedLine = line.split("[ \n\0]", 2);
            Command command = factory.getCommand(splittedLine[0], splittedLine.length == 1? "" : splittedLine[1]);
            command.execute(dataStorage);

        }
    }

    private DataStorage dataStorage = new DataStorage();
    private Scanner scanner;

}
