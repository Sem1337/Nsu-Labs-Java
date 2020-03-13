package test;

import main.DataStorage;
import main.calcExceptions.DivisionByZero;
import main.calcExceptions.NotEnoughOperands;
import main.commands.Division;
import main.commands.Pop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PopTest {


    @Test
    public void shouldSuccessPop() {
        Pop instance = new Pop(null);
        DataStorage dataStorage = new DataStorage();
        dataStorage.getOperands().push("5.6");
        instance.execute(dataStorage);
        assertEquals(0,dataStorage.getOperands().size());
    }

    @Test
    public void shouldThrowNotEnoughOperands() {
        Pop instance = new Pop(null);
        DataStorage dataStorage = new DataStorage();
        assertThrows(NotEnoughOperands.class, () -> instance.execute(dataStorage));
    }

}