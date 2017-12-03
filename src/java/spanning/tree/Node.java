package spanning.tree;

import static spanning.tree.Relation.COMPLETED;
import static spanning.tree.Relation.FORGOTTEN;
import static spanning.tree.Relation.PROBABLY_CHILD;
import static spanning.tree.Relation.UNCOMPLETED;
import static spanning.tree.SpanningTreeUtils.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Node implements Runnable {


    private final int id;
    private volatile boolean root;
    private volatile Node parent = null;
    private Map<Node, Relation> neighbours;

    public Node(int id) {
        this.id = id;
        neighbours = Collections.synchronizedMap(new HashMap<>());
    }

    private void findChildren() {
        getMyNeighbours(this, parent).stream().forEach(n -> {
            neighbours.put(n, PROBABLY_CHILD);
            n.father(this);
        });
    }

    public void father(Node father) {
        if (canAttachFather()) {
            attachFather(father);
            return;
        }
        father.forget(this);
    }

    private void attachFather(Node father) {
        parent = father;
        father.child(this);
    }

    private boolean canAttachFather() {
        return isOrphaned() && !root;
    }

    private void child(Node child) {
        neighbours.put(child, UNCOMPLETED);
    }

    private void forget(Node toForget) {
        neighbours.put(toForget, FORGOTTEN);
    }

    private void complete(Node child) {
        neighbours.put(child, COMPLETED);
    }

    public void generateMST() {
        if (!isOrphaned()) {
            log(this.getClass(), "ROOT must be \'orphaned\'! Closing app!");
            System.exit(0);
        }
        setRoot(true);
    }

    @Override
    public void run() {
        while (isOrphaned() && !root) w8();

        findChildren();

        while (!isCompleted()) w8();

        finish();
    }

    private boolean isCompleted() {
        return !neighbours.values().stream().filter(r -> !r.isFinished()).findFirst().isPresent();
    }

    private StringBuilder getSubTree(StringBuilder sb, int indent) {
        applyIndentAndPasteId(sb, indent);
        subtreeOfNeighbours(sb, indent);
        return sb;
    }

    private void applyIndentAndPasteId(StringBuilder sb, int indent) {
        sb.append("\n");
        for (int i = 0; i < indent; i++) sb.append("\t");
        sb.append(getId());
    }

    private void subtreeOfNeighbours(StringBuilder sb, int indent) {
        neighbours.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(Relation.COMPLETED))
                .forEach(entry -> entry.getKey().getSubTree(sb, indent + 1));
    }

    public int getId() {
        return this.id;
    }

    private boolean isOrphaned() {
        return parent == null;
    }

    private void w8() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }
    }

    public boolean isRoot() {
        return root;
    }

    public Node setRoot(boolean root) {
        this.root = root;
        return this;
    }

    private void finish() {
        if (isRoot()) log(this.getClass(), getSubTree(new StringBuilder(), 0).toString());
        else parent.complete(this);
    }

    @Override
    public String toString() {
        return new StringBuilder("N(").append(id).append(")").toString();
    }

}
