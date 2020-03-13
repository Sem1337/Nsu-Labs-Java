package test;

import main.DataStorage;
import main.calcExceptions.IncorrectArgument;
import main.calcExceptions.IncorrectResult;
import main.commands.Subtraction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtractionTest {


    @Test
    public void shouldSuccessSubtraction() {
        Subtraction instance = new Subtraction(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("3");
        dataStorage.getOperands().push("4");
        instance.execute(dataStorage);
        assertEquals("-1.0",dataStorage.getOperands().peek());
    }

    @Test
    public void shouldThrowIncorrectResultSubtraction() {
        Subtraction instance = new Subtraction(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("1.0E250");
        dataStorage.getOperands().push("-1.0E350");
        assertThrows(IncorrectResult.class, () -> instance.execute(dataStorage));
    }

    @Test
    public void shouldThrowIncorrectArgumentSubtraction() {
        Subtraction instance = new Subtraction(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("1.0E250");
        dataStorage.getOperands().push("fdscsEERR");
        assertThrows(IncorrectArgument.class, () -> instance.execute(dataStorage));
    }


}