package ru.vsu.cs.course1.graph;

import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Утилиты работы с графами
 */
public class GraphUtils {

    public static Graph fromStr(String str, Class clz) throws IOException, InstantiationException, IllegalAccessException {
        Graph graph = (Graph) clz.newInstance();
        Map<String, Integer> names = new HashMap<>();
        int vertexCount = 0;
        if (Pattern.compile("^\\s*(strict\\s+)?(graph|digraph)\\s*\\{").matcher(str).find()) {
            // dot-формат
            MutableGraph g = new Parser().read(str);
            vertexCount = g.nodes().size();
            graph.addAdge(vertexCount - 1, vertexCount - 1);
            graph.removeAdge(vertexCount - 1, vertexCount - 1);

            // проверка, являются ли все вершины целыми (-2 - не являются)
            Pattern intPattern = Pattern.compile("^\\d+$");
            int maxVertex = -1;
            for (Link l : g.links()) {
                String fromStr = l.from().toString();
                if (intPattern.matcher(fromStr).matches()) {
                    maxVertex = Math.max(maxVertex, Integer.parseInt(fromStr));
                } else {
                    maxVertex = -2;
                    break;
                }
                String toStr = l.from().toString();
                if (intPattern.matcher(toStr).matches()) {
                    maxVertex = Math.max(maxVertex, Integer.parseInt(toStr));
                } else {
                    maxVertex = -2;
                    break;
                }
            }
            vertexCount = 0;
            for (Link l : g.links()) {
                String fromStr = l.from().toString();
                Integer from = null;
                if (maxVertex == -2) {
                    from = names.get(fromStr);
                    if (from == null) {
                        from = vertexCount;
                        names.put(fromStr, from);
                        vertexCount++;
                    }
                } else {
                    from = Integer.parseInt(fromStr);
                }
                String toStr = l.to().toString();
                Integer to = null;
                if (maxVertex == -2) {
                    to = names.get(toStr);
                    if (to == null) {
                        to = vertexCount;
                        names.put(toStr, to);
                        vertexCount++;
                    }
                } else {
                    to = Integer.parseInt(toStr);
                }
                graph.addAdge(from, to);
            }
        } else if (Pattern.compile("^\\s*\\d+").matcher(str).find()) {
            Scanner scanner = new Scanner(str);
            vertexCount = scanner.nextInt();
            int edgeCount = scanner.nextInt();
            for (int i = 0; i < edgeCount; i++) {
                graph.addAdge(scanner.nextInt(), scanner.nextInt());
            }
        } else {
            Scanner scanner = new Scanner(str);
            vertexCount = scanner.nextInt();
            while (scanner.hasNext()) {
                String fromStr = scanner.next();
                Integer from = names.get(fromStr);
                if (from == null) {
                    from = vertexCount;
                    names.put(fromStr, from);
                    vertexCount++;
                }
                String toStr = scanner.next();
                Integer to = names.get(toStr);
                if (to == null) {
                    to = vertexCount;
                    names.put(toStr, to);
                    vertexCount++;
                }
                graph.addAdge(from, to);
            }
        }

        return graph;
    }


    /**
     * Получение dot-описяния графа (для GraphViz)
     * @return
     */
    public static String toDot(Graph graph) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = graph instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < graph.vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : graph.adjacencies(v1)) {
                sb.append(String.format("  %d %s %d", v1, (isDigraph ? "->" : "--"), v2)).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
}
