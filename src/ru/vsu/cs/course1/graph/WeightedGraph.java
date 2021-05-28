package ru.vsu.cs.course1.graph;

/**
 * Интерфейс для описания взвешенного ненаправленного графа (н-графа)
 * с реализацией некоторых методов
 */
public interface WeightedGraph extends Graph {

    /**
     * Интерфейс, описывающий связанную вершину с весом связи
     * (используется для перебора связанных вершин у конкретной вершины)
     */
    interface WeightedEdgeTo {
        int to();
        double weight();
    }

    /**
     * Добавление ребра между вершинами с номерами v1 и v2 c весом w
     * @param v1
     * @param v2
     * @param weight
     */
    void addAdge(int v1, int v2, double weight);

    /**
     * @param v Номер вершины, смежные с которой необходимо найти
     * @return Объект, поддерживающий итерацию по номерам связанных с v вершин
     */
    Iterable<WeightedEdgeTo> adjacenciesWithWeights(int v);

    /**
     * Вес ребра между вершинами v1 и v2
     * @param v1
     * @param v2
     * @return вес или null, если вершины не связаны
     */
    default Double getWeight(int v1, int v2) {
        for (WeightedEdgeTo adj : adjacenciesWithWeights(v1)) {
            if (adj.to() == v2) {
                return adj.weight();
            }
        }
        return null;
    }
}
