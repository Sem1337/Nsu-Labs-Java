import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

class CSVWriter {

    CSVWriter(String fileName) {
        this.fileName = fileName;
    }

    void write(FileHandler handledData) {
        Map<String, Integer> data = handledData.getFrequency();
        Integer wordsCount = handledData.getWordsCount();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("Word;Frequency;%\n");
            Set<Token> orderedData = new TreeSet<Token>();
            data.forEach((k,v) -> orderedData.add(new Token(k,v)));
            for (Token token : orderedData) {
                int cnt = (int) token.getFrequency();
                double percent = (double) cnt / wordsCount * 100;
                double truncatedPercent = BigDecimal.valueOf(percent).setScale(3, RoundingMode.HALF_UP).doubleValue();
                writer.write(token.getWord() + ";" + cnt + ";" + truncatedPercent + "%\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String fileName;
}
