package info.kgeorgiy.ja.pologov.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

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
// :fixed: Было неверное имя класса
public class IterativeParallelism implements ScalarIP {
    private final ParallelMapper mapper;
    public IterativeParallelism() {
        mapper = null;
    }
    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }

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
    public <T> T maximum(final int threads, final List<? extends T> values, final Comparator<? super T> comparator) throws InterruptedException {
        // :fixed: Дубль
        Function<Stream<? extends T>, T> func = stream -> stream.max(comparator).orElseThrow();
        return applyParalleling(threads, values, func, func);
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
    public <T> T minimum(final int threads, final List<? extends T> values, final Comparator<? super T> comparator) throws InterruptedException {
        Function<Stream<? extends T>, T> func = stream -> stream.min(comparator).orElseThrow();
        return applyParalleling(threads, values, func, func);
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
    public <T> boolean all(final int threads, final List<? extends T> values, final Predicate<? super T> predicate) throws InterruptedException {
        // :fixed: Дубль
        return applyParalleling(threads, values, getStreamBooleanFunction(predicate), getStreamBooleanFunction(b -> b == Boolean.TRUE));
    }

    private <T> Function<Stream<? extends T>, Boolean> getStreamBooleanFunction(Predicate<? super T> predicate) {
        return stream -> stream.allMatch(predicate);
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
    public <T> boolean any(final int threads, final List<? extends T> values, final Predicate<? super T> predicate) throws InterruptedException {
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
    private <T, U> U applyParalleling(int threads, final List<? extends T> values, final Function<Stream<? extends T>, U> function,
                                      final Function<Stream<? extends U>, U> collectPartitions) throws InterruptedException {
        threads = Math.min(threads, values.size());
        final List<Stream<? extends T>> sublist = getSublist(threads, values);
        if (mapper == null) {
            return iterativeParallelism(threads, values, function, collectPartitions, sublist);
        } else {
            return collectPartitions.apply(mapper.map(function, sublist).stream());
        }
    }

    private <U, T> U iterativeParallelism(int threads, List<? extends T> values, Function<Stream<? extends T>, U> function,
                                          Function<Stream<? extends U>, U> collectPartitions, List<Stream<? extends T>> sublist) throws InterruptedException {
        final List<Thread> workers = new ArrayList<>();
        final List<U> resultsOfApplyingFunctionsToParts = new ArrayList<>(Collections.nCopies(threads, null));
        for (int i = 0; i < threads; i++) {
            final int finalI = i;
            final Thread worker = new Thread(() ->
                    resultsOfApplyingFunctionsToParts.set(finalI, function.apply(sublist.get(finalI))));
            worker.start();
            workers.add(worker);
        }

        InterruptedException suppressedException = null;
        for (final Thread worker : workers) {
            // :fixed: Утечка потоков
            try {
                worker.join();
            } catch (InterruptedException ie) {
                if (suppressedException == null) {
                    suppressedException = ie;
                } else {
                    suppressedException.addSuppressed(ie);
                }
            }
        }
        if (suppressedException != null) {
            throw suppressedException;
        }

        return collectPartitions.apply(resultsOfApplyingFunctionsToParts.stream());
    }

    private <T> List<Stream<? extends T>> getSublist(int threads, List<? extends T> values) {
        // :fixed: Пустые списки
        if (values == null) {
            throw new IllegalArgumentException("Error: value is null.");
        }
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Error: value is empty.");
        }
        final int part = values.size() / threads;
        int remainder = values.size() % threads;

        final List<Stream<? extends T>> sublists = new ArrayList<>();

        // :fixed: IntStream
        int right = 0;
        for (int i = 0; i < threads; i++) {
            final int left = right;
            right += part;
            if (remainder - 1 >= 0) {
                remainder--;
                right++;
            }
            var sublist = values.subList(left, right).stream();
            sublists.add(sublist);
        }
        return sublists;
    }
}
