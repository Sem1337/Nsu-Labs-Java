import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class CSVWriter {

    public CSVWriter(String fileName) {
        this.fileName = fileName;
    }

    public void write(Map<String,Integer> data, Integer wordsCount) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("Word;Frequency;%\n");
        for (String word: data.keySet()) {
            int cnt = data.get(word);
            double percent = (double)cnt / wordsCount * 100;

            double truncatedPercent = BigDecimal.valueOf(percent).setScale(3, RoundingMode.HALF_UP).doubleValue();
            writer.write(word + ";" + cnt + ";" + truncatedPercent + "%\n");
        }
        writer.close();
    }

    private String fileName;
}
