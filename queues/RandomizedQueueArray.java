import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueueArray<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueueArray() {
        items = (Item[]) new Object[16];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[size++] = item;
    }


    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(0, size);
        return dequeue(index);
    }

    // remove and return given item
    private Item dequeue(int index) {
        Item item = items[index];
        for (int i = index; i < size - 1; i++) {
            items[i] = items[i + 1];
        }
        size--;
        items[size] = null;
        if (size < items.length / 2) {
            resize(items.length / 2);
        }
        return item;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(0, size);
        return items[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        return new IteratorRandomizedQueue();
    }

    private class IteratorRandomizedQueue implements Iterator<Item> {
        private int[] indexes;
        private int current;

        public IteratorRandomizedQueue() {
            initIndexes();
            shuffle();
        }

        private void initIndexes() {
            indexes = new int[size];
            for (int i = 0; i < size; i++) {
                indexes[i] = i;
            }
        }

        private void shuffle() {
            for (int i = 0; i < size; i++) {
                int r = StdRandom.uniform(i + 1);
                swap(indexes, i, r);
            }
        }

        private void swap(int[] indexes, int i, int j) {
            int temp = indexes[i];
            indexes[i] = indexes[j];
            indexes[j] = temp;
        }

        public boolean hasNext() {
            return current < size;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = items[indexes[current]];
            current++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> deque = new RandomizedQueue<>();
        deque.enqueue("1");
        deque.enqueue("2");
        deque.enqueue("3");
        print(deque);
        StdOut.println(deque.sample());
        deque.dequeue();
        print(deque);

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue("A");
        randomizedQueue.enqueue("B");
        randomizedQueue.enqueue("C");

        Iterator<String> iterator1 = randomizedQueue.iterator();
        Iterator<String> iterator2 = randomizedQueue.iterator();

        while (iterator1.hasNext()) {
            System.out.print(iterator1.next() + " ");
        }
        System.out.println();
        while (iterator2.hasNext()) {
            System.out.print(iterator2.next() + " ");
        }
    }

    private static void print(RandomizedQueue<?> deque) {
        for (Object s : deque) {
            StdOut.print(s + " ");
        }
        StdOut.println();
    }
}