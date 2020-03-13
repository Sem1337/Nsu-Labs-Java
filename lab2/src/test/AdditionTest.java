package test;

import main.DataStorage;
import main.calcExceptions.IncorrectArgument;
import main.calcExceptions.IncorrectResult;
import main.commands.Addition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdditionTest {

    @Test
    public void shouldSuccessAddition() {
        Addition instance = new Addition(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("3");
        dataStorage.getOperands().push("4");
        instance.execute(dataStorage);
       assertEquals("7.0",dataStorage.getOperands().peek());
    }

    @Test
    public void shouldThrowIncorrectResultAddition() {
        Addition instance = new Addition(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("1.0E250");
        dataStorage.getOperands().push("1.0E350");
        assertThrows(IncorrectResult.class, () -> instance.execute(dataStorage));
    }

    @Test
    public void shouldThrowIncorrectArgumentAddition() {
        Addition instance = new Addition(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("1.0E250");
        dataStorage.getOperands().push("1.0EERR");
        assertThrows(IncorrectArgument.class, () -> instance.execute(dataStorage));
    }


}