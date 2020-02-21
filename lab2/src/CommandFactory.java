import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class CommandFactory {

    CommandFactory(String name) {
        InputStream inputStream = null;
        try {
            inputStream = CommandFactory.class.getResourceAsStream(name);
            byte[] bytes = inputStream.readAllBytes();
            String[] commandsDescription = (new String(bytes)).split("[:\\n]");
            System.out.println(commandsDescription.length );
            for (int i = 0; i < commandsDescription.length; i+=2) {
                commandsNames.put(commandsDescription[i],commandsDescription[i+1]);
            }
        } catch (IOException ex) {
            System.err.println("Error reading config: " + ex.getLocalizedMessage());
        } catch (Exception ex) {
            System.err.println("Error parsing config: " + ex.getLocalizedMessage());
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }
    }


    Command getCommand(String name) {
        return null;
    }

    private Map<String,String> commandsNames = new TreeMap<>();



}
