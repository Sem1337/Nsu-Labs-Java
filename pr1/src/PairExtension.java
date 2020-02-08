import javafx.util.Pair;

class ComparablePair<T extends Comparable, K extends  Comparable> extends Pair<Comparable,Comparable> implements Comparable<ComparablePair> {

    ComparablePair(Comparable<Integer> key, Comparable<String> value) {
        super(key, value);
    }

    @Override
    public int compareTo(ComparablePair comparablePair) {
        if(getKey().compareTo(comparablePair.getKey()) == 0) return -1*getValue().compareTo(comparablePair.getValue());
        return -1*getKey().compareTo(comparablePair.getKey());
    }
}

