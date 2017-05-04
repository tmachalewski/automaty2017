package zadanie13;

import java.util.List;

/**
 * Created by tomek on 02.05.17.
 */
public class MyEdge {
    String label;
    List<Integer> source;
    List<Integer> target;

    public MyEdge(String label) {
        this.label = label;
    }

    public MyEdge(String label, List<Integer> source, List<Integer> target) {
        this.label = label;
        this.source = source;
        this.target = target;
    }

    @Override
    public String toString() {
        return label.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != MyEdge.class) {
            return false;
        }
        MyEdge tempEdge = (MyEdge) obj;
        if (!this.label.equals(tempEdge.label)) {
            return false;
        }
        if (!this.source.equals(tempEdge.source)) {
            return false;
        }
        return this.target.equals(tempEdge.target);
    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }

}