package info.kgeorgiy.ja.pologov.mapper;

import java.util.ArrayDeque;
import java.util.Queue;

public class BlockingQueue {
    private final Queue<Runnable> tasks;

    public BlockingQueue() {
        tasks = new ArrayDeque<>();
    }

    public synchronized void add(Runnable task) {
        tasks.add(task);
        notify();
    }

    public synchronized Runnable take() throws InterruptedException {
        while (tasks.isEmpty()) {
            wait();
        }
        Runnable task = tasks.peek();
        tasks.poll();
        return task;
    }
}