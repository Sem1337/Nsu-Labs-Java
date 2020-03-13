package test;

import main.DataStorage;
import main.calcExceptions.IncorrectArgument;
import main.calcExceptions.WrongNumberOfArguments;
import main.commands.Define;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefineTest {

    @Test
    void shouldSuccessDefine() {
        Define instance = new Define("var 55.3");
        DataStorage dataStorage = new DataStorage();
        instance.execute(dataStorage);
        assertEquals(55.3,dataStorage.getVariables().get("var"));
    }

    @Test
    void shouldThrowWrongNumberOfArguments() {
        assertThrows(WrongNumberOfArguments.class, () -> new Define("var    "));
    }

    @Test
    void shouldThrowIncorrectArgument() {
        Define instance = new Define("54var 55.3");
        DataStorage dataStorage = new DataStorage();
        assertThrows(IncorrectArgument.class, () -> instance.execute(dataStorage));
    }

}