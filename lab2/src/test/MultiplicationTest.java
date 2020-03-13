package test;

import main.DataStorage;
import main.calcExceptions.IncorrectArgument;
import main.calcExceptions.IncorrectResult;
import main.commands.Division;
import main.commands.Multiplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultiplicationTest {

    @Test
    public void shouldSuccessMultiplication() {
        Multiplication instance = new Multiplication(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("3");
        dataStorage.getOperands().push("4");
        instance.execute(dataStorage);
        assertEquals("12.0",dataStorage.getOperands().peek());
    }

    @Test
    public void shouldThrowIncorrectResultAddition() {
        Multiplication instance = new Multiplication(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("1.0E250");
        dataStorage.getOperands().push("2.0E250");
        assertThrows(IncorrectResult.class, () -> instance.execute(dataStorage));
    }

    @Test
    public void shouldThrowIncorrectArgumentAddition() {
        Multiplication instance = new Multiplication(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("1.0E250");
        dataStorage.getOperands().push("1.0EERR");
        assertThrows(IncorrectArgument.class, () -> instance.execute(dataStorage));
    }

}