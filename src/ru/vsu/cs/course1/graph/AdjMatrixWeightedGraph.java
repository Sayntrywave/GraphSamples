package ru.vsu.cs.course1.graph;

import com.kitfox.svg.A;

import java.util.*;

public class AdjMatrixWeightedGraph implements WeightedGraph {
    private Double[][] adjMatrix = null;
    private int vCount = 0;
    private int eCount = 0;

    /**
     * Конструктор
     * @param vertexCount Кол-во вершин графа (может увеличиваться при добавлении ребер)
     */
    public AdjMatrixWeightedGraph(int vertexCount) {
        adjMatrix = new Double[vertexCount][vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            Arrays.fill(adjMatrix[i], Double.MAX_VALUE);
        }
        vCount = vertexCount;
    }

    /**
     * Конструктор без парметров
     * (лучше не использовать, т.к. при добавлении вершин каждый раз пересоздается матрица)
     */
    public AdjMatrixWeightedGraph() {
        this(0);
    }

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




    @Override
    public Iterable<WeightedEdgeTo> adjacencyWithWeights(int v) {
        List<WeightedEdgeTo> list = new ArrayList<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            if((Double.MAX_VALUE - adjMatrix[v][i] > 1)){
                list.add(new WeightedEdge(i,adjMatrix[v][i]));
            }
        }
        return list.isEmpty() ? nullIterableWithWeights : list;
    }

    @Override
    public int vertexCount() {
        return vCount;
    }

    @Override
    public int edgeCount() {
        return eCount;
    }

    @Override
    public void addAdge(int v1, int v2, double weight) {
        int maxV = Math.max(v1, v2);
        if (maxV >= vertexCount()) {
            adjMatrix = Arrays.copyOf(adjMatrix, maxV + 1);
            for (int i = 0; i <= maxV; i++) {
                if(i < vCount){
                    adjMatrix[i] = Arrays.copyOf(adjMatrix[i], maxV + 1);
                    for (int j = vCount; j < maxV + 1; j++) {
                        adjMatrix[i][j] = Double.MAX_VALUE;
                    }
                }
                else {
                    adjMatrix[i] = getInfinityDouble(maxV + 1);
                }
//                adjMatrix[i] = (i < vCount) ? Arrays.copyOf(adjMatrix[i], maxV + 1) : getInfinityDouble(maxV+1);
            }

            vCount = maxV + 1;
        }
        if (((Double.MAX_VALUE - adjMatrix[v1][v2]) < 1)) {
            adjMatrix[v1][v2] = weight;
            eCount++;
            // для наследников
            if (!(this instanceof Digraph)) {
                adjMatrix[v2][v1] = weight;
            }
        }
    }
    private Double[] getInfinityDouble(int length){
        Double[] doubles =  new Double[length];
        Arrays.fill(doubles, Double.MAX_VALUE);
        return doubles;
    }

    @Override
    public void removeAdge(int v1, int v2) {
        if (!adjMatrix[v1][v2].isInfinite()) {
            adjMatrix[v1][v2] = Double.MAX_VALUE;
            eCount--;
            // для наследников
            if (!(this instanceof Digraph)) {
                adjMatrix[v2][v1] = Double.MAX_VALUE;
            }
        }
    }

    @Override
    public Iterable<Integer> adjacency(int v) {
        return new Iterable<Integer>() {
            Integer nextAdj = null;
            @Override
            public Iterator<Integer> iterator() {
                for (int i = 0; i < vCount; i++) {
                    if (!adjMatrix[v][i].isInfinite()) {
                        nextAdj = i;
                        break;
                    }
                }

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return nextAdj != null;
                    }

                    @Override
                    public Integer next() {
                        Integer result = nextAdj;
                        nextAdj = null;
                        for (int i = result + 1; i < vCount; i++) {
                            if (!adjMatrix[v][i].isInfinite()) {
                                nextAdj = i;
                                break;
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }

    @Override
    public void addAdge(int v1, int v2) {

    }
    /*    // Перегрузка для быстродействия
    @Override
    public boolean isAdj(int v1, int v2) {
        return adjMatrix[v1][v2];
    }*/


}
