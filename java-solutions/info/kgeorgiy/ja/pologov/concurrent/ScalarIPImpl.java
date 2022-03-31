package info.kgeorgiy.ja.pologov.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Nikita Pologov (nikita.pologov1@gmail.com)
 * @see ScalarIP
 */
public class ScalarIPImpl implements ScalarIP {
    /**
     * Returns maximum value.
     *
     * @param threads    number or concurrent threads.
     * @param values     values to get maximum of.
     * @param comparator value comparator.
     * @param <T>        value type.
     * @return maximum of given values
     * @throws InterruptedException             if executing thread was interrupted.
     * @throws java.util.NoSuchElementException if no values are given.
     */
    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return applyParalleling(threads, values, stream -> stream.max(comparator).orElseThrow(), stream -> stream.max(comparator).orElseThrow());
    }

    /**
     * Returns minimum value.
     *
     * @param threads    number or concurrent threads.
     * @param values     values to get minimum of.
     * @param comparator value comparator.
     * @param <T>        value type.
     * @return minimum of given values
     * @throws InterruptedException             if executing thread was interrupted.
     * @throws java.util.NoSuchElementException if no values are given.
     */
    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return applyParalleling(threads, values, stream -> stream.min(comparator).orElseThrow(), stream -> stream.min(comparator).orElseThrow());
    }

    /**
     * Returns whether all values satisfies predicate.
     *
     * @param threads   number or concurrent threads.
     * @param values    values to test.
     * @param predicate test predicate.
     * @param <T>       value type.
     * @return whether all values satisfies predicate or {@code true}, if no values are given
     * @throws InterruptedException if executing thread was interrupted.
     */
    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return applyParalleling(threads, values, stream -> stream.allMatch(predicate), stream -> stream.allMatch(b -> b == Boolean.TRUE));
    }

    /**
     * Returns whether any of values satisfies predicate.
     *
     * @param threads   number or concurrent threads.
     * @param values    values to test.
     * @param predicate test predicate.
     * @param <T>       value type.
     * @return whether any value satisfies predicate or {@code false}, if no values are given
     * @throws InterruptedException if executing thread was interrupted.
     */
    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return applyParalleling(threads, values, stream -> stream.anyMatch(predicate), stream -> stream.anyMatch(b -> b == Boolean.TRUE));
    }

    /**
     * Function Parallel Processing.
     *
     * @param threads           count of threads
     * @param values            list of processed data
     * @param function          list processing function
     * @param collectPartitions function to collect partitions
     * @param <T>               input type
     * @param <U>               output type
     * @return result of function for values
     * @throws InterruptedException if at least one thread is interrupted
     */
    private <T, U> U applyParalleling(int threads, List<? extends T> values, final Function<Stream<? extends T>, U> function,
                                      final Function<Stream<? extends U>, U> collectPartitions) throws InterruptedException {
        threads = Math.min(threads, values.size());
        int part = values.size() / threads;
        int remainder = values.size() % threads;

        List<Thread> workers = new ArrayList<>();
        List<U> resultsOfApplyingFunctionsToParts = new ArrayList<>(Collections.nCopies(threads, null));

        int right = 0;
        for (int i = 0; i < threads; i++) {
            int left = right;
            right += part;
            if (remainder - 1 >= 0) {
                remainder--;
                right++;
            }
            final int finalRight = right;
            final int finalI = i;
            Thread worker = new Thread(() ->
                    resultsOfApplyingFunctionsToParts.set(
                            finalI,
                            function.apply(values.subList(left, finalRight).stream())));
            worker.start();
            workers.add(worker);
        }

        for (Thread worker : workers) {
            worker.join();
        }

        return collectPartitions.apply(resultsOfApplyingFunctionsToParts.stream());
    }
}
