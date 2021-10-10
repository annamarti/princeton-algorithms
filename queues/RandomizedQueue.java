import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {

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

        Node newNode = new Node(item);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
        }
        else {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        size++;
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
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            first = current.next;
        }
        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            last = current.prev;
        }
        size--;
        return current.item;
    }


    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(0, size);
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new IteratorRandomizedQueue();
    }

    private class IteratorRandomizedQueue<Item> implements Iterator<Item> {
        private int[] indexes;
        private Item[] items;
        private int current;

        public IteratorRandomizedQueue() {
            initIndexes();
            shuffle();
            initItems();
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

        private void initItems() {
            items = (Item[]) (new Object[size]);
            try {
                for (int i = 0; i < size; i++) {
                    Node node = first;
                    for (int j = 0; j < indexes[i]; j++) {
                        node = node.next;
                    }
                    items[i] = (Item) node.item;
                }
            }
            catch (NullPointerException e) {
                System.out.println(12);
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
            return items[current++];
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

    private class Node {
        Node next;
        Node prev;
        Item item;

        public Node() {
        }

        public Node(Item item) {
            this.item = item;
        }
    }

    private static void print(RandomizedQueue<?> deque) {
        for (Object s : deque) {
            StdOut.print(s + " ");
        }
        StdOut.println();
    }

}