package spanning.tree;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class SpanningTreeUtils {

    public static final int NODE_COUNT = 19;

    public static final Random random = new SecureRandom();


    private static List<Node> nodes = new ArrayList<>();

    public static Map<Node, List<Node>> edges = Collections.synchronizedMap(new HashMap<>());

    

    public static Node createNode() {
        Node n = new Node(nodes.size());
        nodes.add(n);
        edges.put(n, new ArrayList<>());
        return n;
    }

    public static Node getNode(int i) {
        return nodes.get(i);
    }

    public static void startNodeThreads() {
        nodes.stream().forEach(n -> new Thread(n).start());
    }

    public static void addEdge(Node nodeA, Node nodeB) {
        List<Node> listA = edges.get(nodeA);
        List<Node> listB = edges.get(nodeB);
        if(!listA.contains(nodeB)) {
            log(SpanningTreeUtils.class, "adding edge:  " + nodeA + " -- " + nodeB);
            listA.add(nodeB);
        }
        if(!listB.contains(nodeA)) {
            log(SpanningTreeUtils.class, "adding edge:  " + nodeB + " -- " + nodeA);
            listB.add(nodeA);
        }
    }

    public static List<Node> getMyNeighbours(Node src, Node excluded) {
        List<Node> neighbours = new ArrayList<>(edges.get(src));
        log(SpanningTreeUtils.class, neighbours.size());
        neighbours.remove(src);
        neighbours.remove(excluded);

        return neighbours;
    }

    public static void addTestEdges() {
        addEdge(getNode(1), getNode(3));
        addEdge(getNode(17), getNode(18));
        addEdge(getNode(8), getNode(6));
        addEdge(getNode(9), getNode(15));

        addEdge(getNode(6), getNode(2));
        addEdge(getNode(3), getNode(13));
        addEdge(getNode(4), getNode(15));
        addEdge(getNode(0), getNode(8));

        addEdge(getNode(5), getNode(10));
        addEdge(getNode(2), getNode(9));
        addEdge(getNode(3), getNode(12));
        addEdge(getNode(9), getNode(7));

        addEdge(getNode(6), getNode(16));
        addEdge(getNode(6), getNode(9));
        addEdge(getNode(18), getNode(0));
        addEdge(getNode(3), getNode(2));

        addEdge(getNode(1), getNode(11));
        addEdge(getNode(1), getNode(10));
        addEdge(getNode(0), getNode(1));
        addEdge(getNode(13), getNode(14));

        addEdge(getNode(9), getNode(4));
        addEdge(getNode(8), getNode(5));
        addEdge(getNode(7), getNode(14));
        addEdge(getNode(1), getNode(6));


        addEdge(getNode(6), getNode(15));
        addEdge(getNode(6), getNode(5));
    }

    public static void log(Class clazz, Object message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd___hh:mm:ss.SSS");
        System.out.println(sdf.format(new Date()) + "  " + clazz.toString() + ":   " + message.toString());
    }
}
