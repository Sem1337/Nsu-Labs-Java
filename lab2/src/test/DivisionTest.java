package test;

import main.DataStorage;
import main.calcExceptions.DivisionByZero;
import main.calcExceptions.IncorrectArgument;
import main.calcExceptions.IncorrectResult;
import main.commands.Addition;
import main.commands.Division;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DivisionTest {

    @Test
    public void shouldSuccessDivision() {
        Division instance = new Division(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("3");
        dataStorage.getOperands().push("4");
        instance.execute(dataStorage);
        assertEquals("0.75",dataStorage.getOperands().peek());
    }

    @Test
    public void shouldThrowIncorrectResultAddition() {
        Division instance = new Division(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("1.0E250");
        dataStorage.getOperands().push("1.0E-150");
        assertThrows(IncorrectResult.class, () -> instance.execute(dataStorage));
    }

    @Test
    public void shouldThrowIncorrectArgumentAddition() {
        Division instance = new Division(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("1.0E250");
        dataStorage.getOperands().push("1.0EERR");
        assertThrows(IncorrectArgument.class, () -> instance.execute(dataStorage));
    }

    @Test
    public void shouldThrowDivisionByZero() {
        Division instance = new Division(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("1.0E250");
        dataStorage.getOperands().push("0");
        assertThrows(DivisionByZero.class, () -> instance.execute(dataStorage));
    }

}