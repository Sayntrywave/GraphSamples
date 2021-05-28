package ru.vsu.cs.course1.graph.demo;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import static guru.nidi.graphviz.attribute.Label.Justification.LEFT;
import guru.nidi.graphviz.attribute.LinkAttr;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Records;
import static guru.nidi.graphviz.attribute.Records.rec;
import static guru.nidi.graphviz.attribute.Records.turn;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import static guru.nidi.graphviz.model.Compass.NORTH;
import static guru.nidi.graphviz.model.Compass.NORTH_WEST;
import static guru.nidi.graphviz.model.Compass.SOUTH;
import static guru.nidi.graphviz.model.Compass.SOUTH_WEST;
import static guru.nidi.graphviz.model.Compass.WEST;
import static guru.nidi.graphviz.model.Factory.between;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Factory.nodeAttrs;
import static guru.nidi.graphviz.model.Factory.port;
import static guru.nidi.graphviz.model.Factory.to;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
import guru.nidi.graphviz.parse.Parser;
import java.io.File;
import java.io.IOException;

/**
 * Примеры из описания graphviz-java:
 *   https://github.com/nidi3/graphviz-java
 */
public class GraphvizExamples {

    public static String ex1() {
        Graph g = graph("example1").directed()
            .graphAttr().with(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT))
            .with(
                node("a").with(Color.RED).link(node("b")),
                node("b").link(to(node("c")).with(Style.DASHED))
            );

        Graphviz gv = Graphviz.fromGraph(g).height(100);
        try {
            gv.render(Format.PNG).toFile(new File("./files/output/ex1.png"));
            gv.render(Format.SVG).toFile(new File("./files/output/ex1.svg"));
        } catch (Exception ignored) {
        }
        return gv.render(Format.SVG).toString();
    }

    public static String ex1m() {
        MutableGraph g = mutGraph("example1").setDirected(true).add(
                mutNode("a").add(Color.RED).addLink(mutNode("b")));

        Graphviz gv = Graphviz.fromGraph(g).width(200);
        try {
            gv.render(Format.PNG).toFile(new File("./files/output/ex1m.png"));
            gv.render(Format.SVG).toFile(new File("./files/output/ex1m.svg"));
        } catch (Exception ignored) {
        }
        return gv.render(Format.SVG).toString();
    }

    public static String ex1i() {
        MutableGraph g = mutGraph("example1").setDirected(true).use((gr, ctx) -> {
            mutNode("b");
            nodeAttrs().add(Color.RED);
            mutNode("a").addLink(mutNode("b"));
        });

        Graphviz gv = Graphviz.fromGraph(g).width(200);
        try {
            gv.render(Format.PNG).toFile(new File("./files/output/ex1.png"));
            gv.render(Format.SVG).toFile(new File("./files/output/ex1.svg"));
        } catch (Exception ignored) {
        }
        return gv.render(Format.SVG).toString();
    }

    public static String ex2() {
        Node main = node("main").with(Label.html("<b>main</b><br/>start"), Color.rgb("1020d0").font()),
            init = node(Label.markdown("**_init_**")),
            execute = node("execute"),
            compare = node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0)),
            mkString = node("mkString").with(Label.lines(LEFT, "make", "a", "multi-line")),
            printf = node("printf");

        Graph g = graph("example2").directed().with(
            main.link(
                to(node("parse").link(execute)).with(LinkAttr.weight(8)),
                to(init).with(Style.DOTTED),
                node("cleanup"),
                to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED)),
            execute.link(
                graph().with(mkString, printf),
                to(compare).with(Color.RED)),
            init.link(mkString)
        );

        Graphviz gv = Graphviz.fromGraph(g).width(900);
        try {
            gv.render(Format.PNG).toFile(new File("./files/output/ex2.png"));
            gv.render(Format.SVG).toFile(new File("./files/output/ex2.svg"));
        } catch (Exception ignored) {
        }
        return gv.render(Format.SVG).toString();
    }

    public static String ex3() {
        Node node0 = node("node0").with(Records.of(rec("f0", ""), rec("f1", ""), rec("f2", ""), rec("f3", ""), rec("f4", ""))),
            node1 = node("node1").with(Records.of(turn(rec("n4"), rec("v", "719"), rec("")))),
            node2 = node("node2").with(Records.of(turn(rec("a1"), rec("805"), rec("p", "")))),
            node3 = node("node3").with(Records.of(turn(rec("i9"), rec("718"), rec("")))),
            node4 = node("node4").with(Records.of(turn(rec("e5"), rec("989"), rec("p", "")))),
            node5 = node("node5").with(Records.of(turn(rec("t2"), rec("v", "959"), rec("")))),
            node6 = node("node6").with(Records.of(turn(rec("o1"), rec("794"), rec("")))),
            node7 = node("node7").with(Records.of(turn(rec("s7"), rec("659"), rec(""))));
        Graph g = graph("example3").directed()
            .graphAttr().with(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT))
            .with(
                node0.link(
                    between(port("f0"), node1.port("v", SOUTH)),
                    between(port("f1"), node2.port(WEST)),
                    between(port("f2"), node3.port(WEST)),
                    between(port("f3"), node4.port(WEST)),
                    between(port("f4"), node5.port("v", NORTH))
                ),
                node2.link(between(port("p"), node6.port(NORTH_WEST))),
                node4.link(between(port("p"), node7.port(SOUTH_WEST)))
            );

        Graphviz gv = Graphviz.fromGraph(g).width(900);
        try {
            gv.render(Format.PNG).toFile(new File("./files/output/ex3.png"));
            gv.render(Format.SVG).toFile(new File("./files/output/ex3.svg"));
        } catch (Exception ignored) {
        }
        return gv.render(Format.SVG).toString();
    }

    public static String ex4() throws IOException {
        MutableGraph g = new Parser().read(new File("./files/input/color.dot"));

        Graphviz gv1 = Graphviz.fromGraph(g).width(700);
        try {
            gv1.render(Format.PNG).toFile(new File("./files/output/ex4.1.png"));
            gv1.render(Format.SVG).toFile(new File("./files/output/ex4.1.svg"));
        } catch (Exception ignored) {
        }

        g.graphAttrs()
            .add(Color.WHITE.gradient(Color.rgb("888888")).background().angle(90))
            .nodeAttrs().add(Color.WHITE.fill())
            .nodes().forEach(node
                -> node.add(
                Color.named(node.name().toString()),
                Style.lineWidth(4).and(Style.FILLED)));

        Graphviz gv2 = Graphviz.fromGraph(g).width(700);
        try {
            gv2.render(Format.PNG).toFile(new File("./files/output/ex4.2.png"));
            gv2.render(Format.SVG).toFile(new File("./files/output/ex4.2.svg"));
        } catch (Exception ignored) {
        }
        return gv2.render(Format.SVG).toString();
    }
}
