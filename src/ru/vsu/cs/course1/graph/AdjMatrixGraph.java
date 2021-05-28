package ru.vsu.cs.course1.graph;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Реализация графа на основе матрицы смежности
 */
public class AdjMatrixGraph implements Graph {

    private boolean[][] adjMatrix = null;
    private int vCount = 0;
    private int eCount = 0;

    /**
     * Конструктор
     * @param vertexCount Кол-во вершин графа (может увеличиваться при добавлении ребер)
     */
    public AdjMatrixGraph(int vertexCount) {
        adjMatrix = new boolean[vertexCount][vertexCount];
        vCount = vertexCount;
    }

    /**
     * Конструктор без парметров
     * (лучше не использовать, т.к. при добавлении вершин каждый раз пересоздается матрица)
     */
    public AdjMatrixGraph() {
        this(0);
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
    public void addAdge(int v1, int v2) {
        int maxV = Math.max(v1, v2);
        if (maxV >= vertexCount()) {
            adjMatrix = Arrays.copyOf(adjMatrix, maxV + 1);
            for (int i = 0; i <= maxV; i++) {
                adjMatrix[i] = i < vCount ? Arrays.copyOf(adjMatrix[i], maxV + 1) : new boolean[maxV + 1];
            }
            vCount = maxV + 1;
        }
        if (!adjMatrix[v1][v2]) {
            adjMatrix[v1][v2] = true;
            eCount++;
            // для наследников
            if (!(this instanceof Digraph)) {
                adjMatrix[v2][v1] = true;
            }
        }
    }

    @Override
    public void removeAdge(int v1, int v2) {
        if (adjMatrix[v1][v2]) {
            adjMatrix[v1][v2] = false;
            eCount--;
            // для наследников
            if (!(this instanceof Digraph)) {
                adjMatrix[v2][v1] = false;
            }
        }
    }

    @Override
    public Iterable<Integer> adjacencies(int v) {
        return new Iterable<Integer>() {
            Integer nextAdj = null;

            @Override
            public Iterator<Integer> iterator() {
                for (int i = 0; i < vCount; i++) {
                    if (adjMatrix[v][i]) {
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
                            if (adjMatrix[v][i]) {
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

    // Перегрузка для быстродействия
    @Override
    public boolean isAdj(int v1, int v2) {
        return adjMatrix[v1][v2];
    }
}
