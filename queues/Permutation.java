/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<Object> queue = new RandomizedQueue<>();
        String str;
        while (!StdIn.isEmpty()) {
            str = StdIn.readString();
            if (queue.size() < k) {
                queue.enqueue(str);
            } else {
                int i = StdRandom.uniform(2*k);
                if (i < queue.size()) {
                    queue.dequeue();
                    queue.enqueue(str);
                }
            }
        }

        Iterator<Object> iterator = queue.iterator();
        int i = 0;
        while (iterator.hasNext() && i++ < k) {
            StdOut.println(iterator.next());
        }
    }
}
