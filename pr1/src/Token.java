import javafx.util.Pair;

class Token implements Comparable<Token> {

    Token(String word, Integer frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    String getWord() {
        return word;
    }

    Integer getFrequency() {
        return frequency;
    }

    private String word;
    private Integer frequency;

    @Override
    public int compareTo(Token token) {
        if(frequency.equals(token.frequency))return word.compareTo(token.word);
        return -1*frequency.compareTo(token.frequency);
    }
}


