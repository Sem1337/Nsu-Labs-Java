package test;

import main.DataStorage;
import main.calcExceptions.IncorrectArgument;
import main.calcExceptions.IncorrectResult;
import main.commands.Sqrt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqrtTest {

    @Test
    public void shouldSuccessSqrt() {
        Sqrt instance = new Sqrt(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("64");
        instance.execute(dataStorage);
        assertEquals("8.0",dataStorage.getOperands().peek());
    }

    @Test
    public void shouldThrowIncorrectArgumentSqrt() {
        Sqrt instance = new Sqrt(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("-45");
        assertThrows(IncorrectArgument.class, () -> instance.execute(dataStorage));
    }
    
}