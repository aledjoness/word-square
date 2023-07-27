package wordsquare.domain;

import java.util.List;
import java.util.Objects;

public class NodeValue {

    private Pair<String, Boolean> valueToHasBeenExamined;

    public NodeValue(String value) {
        this.valueToHasBeenExamined = Pair.of(value, false);
    }

    public static List<NodeValue> toNodeValues(List<String> input) {
        return input.stream().map(NodeValue::new).toList();
    }

    public String getValue() {
        return valueToHasBeenExamined.left();
    }

    public boolean hasBeenExamined() {
        return valueToHasBeenExamined.right();
    }

    public void setAsExamined() {
        valueToHasBeenExamined = Pair.of(valueToHasBeenExamined.left(), true);
    }

    public void setAsUnexamined() {
        valueToHasBeenExamined = Pair.of(valueToHasBeenExamined.left(), false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NodeValue nodeValue = (NodeValue) o;
        return Objects.equals(valueToHasBeenExamined.left(), nodeValue.valueToHasBeenExamined.left())
                && Objects.equals(valueToHasBeenExamined.right(), nodeValue.valueToHasBeenExamined.right());
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueToHasBeenExamined);
    }

    @Override
    public String toString() {
        return "NodeValue{" + valueToHasBeenExamined.left() + "}";
    }
}
