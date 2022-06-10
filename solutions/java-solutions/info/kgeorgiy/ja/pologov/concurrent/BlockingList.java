package info.kgeorgiy.ja.pologov.concurrent;

import java.util.ArrayList;
import java.util.List;

public class BlockingList<R> {
    private final List<R> values;
    private boolean isFull = false;
    private int count = 0;

    public BlockingList(List<R> list) {
        this.values = new ArrayList<>(list);
    }

    public synchronized List<R> getValues() throws InterruptedException {
        while (!isFull) {
            wait();
        }
        return values;
    }

    public synchronized void set(int index, R value) {
        values.set(index, value);
        count++;
        if (values.size() == count) {
            isFull = true;
            notify();
        }
    }
}
