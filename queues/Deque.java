/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item);
        if (first == null) {
            first = node;
            last = node;
        }
        else {
            first.prev = node;
            node.next = first;
            first = node;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item);
        if (last == null) {
            first = node;
            last = node;
        }
        else {
            last.next = node;
            node.prev = last;
            last = node;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        if (first.next != null) {
            first.next.prev = null;
            first = first.next;
        }
        else {
            first = null;
            last = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = last.item;
        if (last.prev != null) {
            last.prev.next = null;
            last = last.prev;
        }
        else {
            first = null;
            last = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new IteratorDeque();
    }

    private class Node {
        Node next;
        Node prev;
        Item item;

        public Node(Item item) {
            this.item = item;
        }
    }

    private class IteratorDeque implements Iterator<Item> {

        Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addFirst("2");
        deque.addFirst("1");
        deque.addLast("3");
        print(deque);
        deque.removeFirst();
        print(deque);
        deque.removeLast();
        print(deque);
    }

    private static void print(Deque<?> deque) {
        for (Object s : deque) {
            StdOut.print(s + " ");
        }
        StdOut.println();
    }

}
