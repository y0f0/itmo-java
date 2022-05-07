package info.kgeorgiy.ja.pologov.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {
    private final List<Thread> workers;
    private final BlockingQueue queue;

    public ParallelMapperImpl(int threadsCount) {
        workers = new ArrayList<>();
        queue = new BlockingQueue();

        for (int unused = 0; unused < threadsCount; unused++) {

            Thread worker = new Thread(() -> {
                while (true) {
                    Runnable func;
                    try {
                        func = queue.take();
                    } catch (InterruptedException e) {
                        return;
                    }
                    func.run();
                }
            });
            worker.start();
            workers.add(worker);
        }
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        BlockingList<R> result = new BlockingList<>(Collections.nCopies(args.size(), null));
        for (int i = 0; i < args.size(); i++) {
            int finalI = i;
            queue.add(() -> result.set(finalI, f.apply(args.get(finalI))));
        }
        return result.getValues();
    }

    @Override
    public void close() {
        for (Thread worker : workers) {
            worker.interrupt();
            try {
                worker.join();
            } catch (InterruptedException ignored) {

            }
        }
    }
}

