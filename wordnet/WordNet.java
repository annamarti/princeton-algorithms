import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WordNet {
    // <SynsetId, Set<SynsetName>>
    private final Map<Integer, String> synsetIdNamesMap;

    // <Noun, Set<SynsetId>>
    private final Map<String, Set<Integer>> indeces;

    // Maybe only this graph is needed
    private final Digraph digraph;

    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsetIdNamesMap, String hypernyms) {
        checkForNull(synsetIdNamesMap);
        checkForNull(hypernyms);

        this.synsetIdNamesMap = new HashMap<>();
        this.indeces = new TreeMap<>();
        initSynsets(synsetIdNamesMap);

        this.digraph = new Digraph(this.synsetIdNamesMap.size());
        initDigraph(hypernyms);
        validateDigraph();
        this.sap = new SAP(digraph);
    }

    private void initSynsets(String synsets) {
        In in = new In(synsets);
        String[] synsetData;
        while (in.hasNextLine()) {
            synsetData = in.readLine().split(",");
            int synsetID = Integer.parseInt(synsetData[0]);
            String[] nouns = synsetData[1].split(" ");
            Arrays.stream(nouns)
                  .forEach(noun -> {
                      if (!this.indeces.containsKey(noun)) {
                          this.indeces.put(noun, new HashSet<>());
                      }
                      this.indeces.get(noun).add(synsetID);
                  });
            // this.synsets.put(synsetID, Arrays.stream(nouns).collect(Collectors.toSet()));
            this.synsetIdNamesMap.put(synsetID, synsetData[1]);
        }
    }

    private void initDigraph(String hypernymsFilename) {
        In in = new In(hypernymsFilename);

        while (in.hasNextLine()) {
            String[] hypernymData = in.readLine().split(",");
            Arrays.stream(Arrays.copyOfRange(hypernymData, 1, hypernymData.length))
                  .map(Integer::parseInt)
                  .forEach(hyp -> digraph.addEdge(Integer.parseInt(hypernymData[0]), hyp));
        }

        // this.digraph.reverse();
    }

    private void validateDigraph() {
        DirectedCycle directedCycle = new DirectedCycle(this.digraph);
        if (directedCycle.hasCycle() || !hasOneRoot(this.digraph)) {
            throw new IllegalArgumentException("Invalid data");
        }
    }

    private boolean hasOneRoot(Digraph graph) {
        // Count of vertex which doesn't have root
        int rootCount = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (!graph.adj(i).iterator().hasNext()) {
                rootCount++;
                if (rootCount > 1) {
                    return false;
                }
            }
        }
        return rootCount == 1;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return indeces.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkForNull(word);
        return indeces.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkForNull(nounA);
        checkForNull(nounB);
        return sap.length(this.indeces.get(nounA), this.indeces.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkForNull(nounA);
        checkForNull(nounB);
        int id = sap.ancestor(this.indeces.get(nounA), this.indeces.get(nounB));
        return synsetIdNamesMap.get(id);
    }

    private void checkForNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets3.txt", "hypernyms3InvalidCycle.txt");
        // System.out.println(wordNet.nouns());
        System.out.println(wordNet.isNoun("15_May_Organization"));
        System.out.println(wordNet.distance("three", "one"));
    }
}