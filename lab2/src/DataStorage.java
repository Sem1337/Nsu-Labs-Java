import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class DataStorage {
    public Map<String, Double> getVariables() {
        return variables;
    }

    public Stack<String> getOperands() {
        return operands;
    }

    private Map<String, Double> variables = new TreeMap<>();
    private Stack<String>operands = new Stack<>();
}
