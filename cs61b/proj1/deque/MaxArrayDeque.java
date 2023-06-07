package deque;

import java.util.Comparator;

/**
 * @author yang
 */
public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public T max() {
        return max(this.comparator);
    }

    public T max(Comparator<T> comparator) {
        if (isEmpty()) {
            return null;
        }

        T tmpMax = get(0);
        for (int i = 1; i < size(); i++) {
            T curr = get(i);
            if (comparator.compare(curr, tmpMax) > 0) {
                tmpMax = curr;
            }
        }
        return tmpMax;
    }

}
