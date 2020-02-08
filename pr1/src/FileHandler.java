import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

class FileHandler {

    FileHandler(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            while(true) {
                String line = reader.readLine();
                if(line == null) break;
                for ( String word: line.toLowerCase().split("[\\W_]")) {
                    if(word.isEmpty())continue;
                    wordsCount++;
                    if (!frequency.containsKey(word)) {
                        frequency.put(word,1);
                    } else {
                        Integer incVal = frequency.get(word) + 1;
                        frequency.remove(word);
                        frequency.put(word,incVal);
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        }
        finally {
            if (null != reader) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    private Map<String, Integer>frequency = new TreeMap<>();
    Map<String, Integer> getFrequency() {
        return frequency;
    }


    private int wordsCount;
    int getWordsCount() {
        return wordsCount;
    }

}
