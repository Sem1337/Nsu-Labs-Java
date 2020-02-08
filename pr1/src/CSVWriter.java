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

    void write(Map<String, Integer> data, Integer wordsCount) {
        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("Word;Frequency;%\n");
            Set<ComparablePair<Integer, String>> orderedData = new TreeSet<ComparablePair<Integer, String>>();
            for (Map.Entry<String, Integer> pair : data.entrySet()) {
                orderedData.add(new ComparablePair<>(pair.getValue(), pair.getKey()));
            }

            for (ComparablePair<Integer, String> pair : orderedData) {
                int cnt = (int) pair.getKey();
                double percent = (double) cnt / wordsCount * 100;
                double truncatedPercent = BigDecimal.valueOf(percent).setScale(3, RoundingMode.HALF_UP).doubleValue();
                writer.write(pair.getValue() + ";" + cnt + ";" + truncatedPercent + "%\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
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
