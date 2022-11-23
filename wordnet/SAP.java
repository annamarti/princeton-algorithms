import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SAP {

    private Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.digraph = new Digraph(G.V());
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                this.digraph.addEdge(v, w);
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(Collections.singletonList(v), Collections.singletonList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(Collections.singletonList(v), Collections.singletonList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkForNull(v);
        checkForNull(w);
        v.forEach(vertex -> validateVertex(vertex));
        w.forEach(vertex -> validateVertex(vertex));

        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }

        BreadthFirstDirectedPaths pathToV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathToW = new BreadthFirstDirectedPaths(digraph, w);
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (pathToV.hasPathTo(i) && pathToW.hasPathTo(i)
                    && (pathToV.distTo(i) + pathToW.distTo(i) < minDistance)) {
                minDistance = pathToV.distTo(i) + pathToW.distTo(i);
            }
        }
        if (minDistance == Integer.MAX_VALUE) {
            return -1;
        }
        return minDistance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkForNull(v);
        checkForNull(w);
        v.forEach(vertex -> validateVertex(vertex));
        w.forEach(vertex -> validateVertex(vertex));

        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }

        BreadthFirstDirectedPaths pathToV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathToW = new BreadthFirstDirectedPaths(digraph, w);
        int minDistance = Integer.MAX_VALUE;
        int minAncestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (pathToV.hasPathTo(i) && pathToW.hasPathTo(i)
                    && (pathToV.distTo(i) + pathToW.distTo(i) < minDistance)) {
                minDistance = pathToV.distTo(i) + pathToW.distTo(i);
                minAncestor = i;
            }
        }
        return minAncestor;
    }


    private void checkForNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateVertex(Integer v) {
        if (v == null || v < 0 || v >= digraph.V()) {
            throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            StdOut.printf("length = %d, ancestor = %d\n",
                          sap.length(new ArrayList<>(), new ArrayList<>()),
                          sap.ancestor(Arrays.asList(5, null), new ArrayList<>()));
        }
    }
}
