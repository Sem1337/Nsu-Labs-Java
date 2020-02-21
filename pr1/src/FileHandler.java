import java.io.*;
import java.util.Map;
import java.util.TreeMap;

class FileHandler {

    FileHandler(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                for (String word : line.toLowerCase().split("[\\W_]")) {
                    if (word.isEmpty()) continue;
                    wordsCount++;
                    frequency.merge(word, 1, Integer::sum);
                }
            }
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    private Map<String, Integer> frequency = new TreeMap<>();

    Map<String, Integer> getFrequency() {
        return frequency;
    }

    private int wordsCount;

    int getWordsCount() {
        return wordsCount;
    }

}
