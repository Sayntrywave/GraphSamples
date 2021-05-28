package ru.vsu.cs.course1.graph;

/**
 * Реализация ориентированного графа (орграфа) на основе матрицы смежности
 */
public class AdjMatrixDigraph extends AdjMatrixGraph implements Digraph {
    /**
     * Реализация полность совпадает
     * (в AdjMatrixGraph специально учтено)
     */

    /**
     * Конструктор
     * @param vertexCount Кол-во вершин графа (может увеличиваться при добавлении дуг)
     */
    public AdjMatrixDigraph(int vertexCount) {
        super(vertexCount);
    }

    /**
     * Конструктор без парметров
     * (лучше не использовать, т.к. при добавлении вершин каждый раз пересоздается матрица)
     */
    public AdjMatrixDigraph() {
        this(0);
    }
}
