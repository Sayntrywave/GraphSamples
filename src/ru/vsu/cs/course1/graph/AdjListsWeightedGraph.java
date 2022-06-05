package ru.vsu.cs.course1.graph;

import ru.vsu.cs.course1.graph.AdjListsGraph;
import ru.vsu.cs.course1.graph.Digraph;
import ru.vsu.cs.course1.graph.Graph;
import ru.vsu.cs.course1.graph.WeightedGraph;

import java.util.*;

public class AdjListsWeightedGraph extends AdjListsGraph implements WeightedGraph {
    protected List<List<WeightedEdgeTo>> vEdjLists = new ArrayList<>();

    private static final Iterable<WeightedEdgeTo> nullIterableWithWeights = () -> new Iterator<WeightedEdgeTo>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public WeightedEdgeTo next() {
            return null;
        }
    };

    class WeightedEdge implements WeightedEdgeTo{
        private final int indexOfVertex;
        private double weightEdge;

        public WeightedEdge(int indexOfVertex, double weightEdge) {
            this.indexOfVertex = indexOfVertex;
            this.weightEdge = weightEdge;
        }

        public WeightedEdge (int indexOfVertex) {
            this.indexOfVertex = indexOfVertex;
            weightEdge = Double.MAX_VALUE;
        }

        public void setWeightEdge(double weightEdge) {
            this.weightEdge = weightEdge;
        }

        @Override
        public int to() {
            return indexOfVertex;
        }

        @Override
        public double weight() {
            return weightEdge;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WeightedEdge that = (WeightedEdge) o;
            return indexOfVertex == that.indexOfVertex && Double.compare(that.weightEdge, weightEdge) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(indexOfVertex, weightEdge);
        }
    }

/*    static class Node{
        private final int indexOfVertex;
        private double weightEdge;


    }*/


    @Override
    public void addAdge(int v1, int v2, double weight) {
        int maxV = Math.max(v1, v2);

        // добавляем вершин в список списков связности
        for (; vCount <= maxV; vCount++) {
            vEdjLists.add(null);
        }
        if (!isAdj(v1, v2)) {
            if (vEdjLists.get(v1) == null) {
                vEdjLists.set(v1, new LinkedList<>());
            }
            vEdjLists.get(v1).add(new WeightedEdge(v2, weight));

            eCount++;
            // для наследников
            if (!(this instanceof Digraph)) {
                if (vEdjLists.get(v2) == null) {
                    vEdjLists.set(v2, new LinkedList<>());
                }
                vEdjLists.get(v2).add(new WeightedEdge(v1,weight));
            }
        }
    }

    public boolean isAdj(int v1, int v2) {
        for (WeightedEdgeTo adj : adjacencyWithWeights(v1)) {
            if (adj.equals(v1)) {
                return true;
            }
        }
        return false;
    }

/*    @Override
    public Iterable<Integer> adjacency(int v) {
        return vEdjLists.get(v) == null ? nullIterable : vEdjLists.get(v);
    }*/




    @Override
    public Iterable<WeightedEdgeTo> adjacencyWithWeights(int v) {
        return vEdjLists.get(v) == null ? nullIterableWithWeights : vEdjLists.get(v);
    }
}
