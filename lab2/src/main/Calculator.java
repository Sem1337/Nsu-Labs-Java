package main;

import main.commands.Command;
import main.commands.CommandFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.*;

public class Calculator {
    public Calculator(String fileName) {
        try {
            scanner = new Scanner(new File(fileName));

            var handler = new FileHandler("calc.log");
            handler.setFormatter(new SimpleFormatter());
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(handler);
            LOGGER.setLevel(Level.ALL);
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
            scanner = new Scanner(System.in);
        } catch (IOException ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public Calculator() {
        scanner = new Scanner(System.in);
    }

    void run(CommandFactory factory) {
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.charAt(0) == '#')continue;
            String[] splittedLine = line.split("[ \n\0]", 2);
            Command command = factory.getCommand(splittedLine[0], splittedLine.length == 1? "" : splittedLine[1]);
            if(command == null)continue;
            try {
                command.execute(dataStorage);
            } catch (RuntimeException ex){
                LOGGER.log(Level.SEVERE, "Exception occur", ex);
            }
        }
    }
    public static final Logger LOGGER = Logger.getLogger(Calculator.class.getName());
    private DataStorage dataStorage = new DataStorage();
    private Scanner scanner;

}
