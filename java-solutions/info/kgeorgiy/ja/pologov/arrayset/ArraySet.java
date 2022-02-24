package info.kgeorgiy.ja.pologov.arrayset;

import java.util.*;

// :NOTE: "удалить" методы add, remove
public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {
    private List<E> data;
    private final Comparator<? super E> comparator;

    public ArraySet(List<E> data, Comparator<? super E> comparator) {
        this.data = data;
        this.comparator = comparator;
    }

    public ArraySet() {
        this(Collections.emptyList(), null);
    }

    public ArraySet(Collection<E> collection, Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.data = new ArrayList<>(collection);
        // :NOTE: использовать TreeSet
        data.sort(comparator);
        if (!data.isEmpty()) {
            List<E> uniqueData = new ArrayList<>();
            uniqueData.add(data.get(0));
            for (int i = 1; i < data.size(); i++) {
                E currentData = data.get(i);
                E lastUniqueData = uniqueData.get(uniqueData.size() - 1);
                if (comparator != null && comparator.compare(currentData, lastUniqueData) != 0) {
                    uniqueData.add(currentData);
                } else if (comparator == null && currentData != lastUniqueData) {
                    uniqueData.add(currentData);
                }
            }
            data = uniqueData;
        }
    }

    public ArraySet(Collection<E> collection) {
        this(collection, null);
    }

    public ArraySet(Comparator<? super E> comparator) {
        this(new ArrayList<>(), comparator);
    }

    @Override
    public Iterator<E> iterator() {
        return data.iterator();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        boolean check;
        if (comparator != null) {
            check = comparator.compare(fromElement, toElement) > 0;
        } else {
            check = fromElement == toElement;
        }
        if (check) {
            throw new IllegalArgumentException("fromElement > subset");
        }
        int begin = Collections.binarySearch(data, (E) fromElement, comparator);
        int end = Collections.binarySearch(data, (E) toElement, comparator);
        if (begin < 0) {
            begin = -begin - 1;
        }
        if (end < 0) {
            end = -end - 1;
        }
        if (begin < end)
            return new ArraySet<E>(data.subList(begin, end), comparator);
        return new ArraySet<E>(comparator);
    }

    @Override
    public SortedSet<E> headSet(Object toElement) {
        @SuppressWarnings("unchecked") int end = Collections.binarySearch(data, (E) toElement, comparator);
        if (end < 0) {
            end = -end - 1;
        }
        if (!data.isEmpty())
            return new ArraySet<E>(data.subList(0, end), comparator);
        return new ArraySet<E>(comparator);
    }

    @Override
    public SortedSet<E> tailSet(Object fromElement) {
        @SuppressWarnings("unchecked") int begin = Collections.binarySearch(data, (E) fromElement, comparator);
        if (begin < 0) {
            begin = -begin - 1;
        }
        if (!data.isEmpty())
            return new ArraySet<E>(data.subList(begin, data.size()), comparator);
        return new ArraySet<E>(comparator);
    }

    @Override
    public E first() {
        if (data.isEmpty())
            throw new NoSuchElementException();
        return data.get(0);
    }

    @Override
    public E last() {
        if (data.isEmpty())
            throw new NoSuchElementException();
        return data.get(data.size() - 1);
    }

    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return Collections.binarySearch(data,  (E) o, comparator) >= 0;
    }
}
