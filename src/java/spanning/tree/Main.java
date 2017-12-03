package spanning.tree;

import static spanning.tree.SpanningTreeUtils.*;

public class Main {

	public static void main(String[] args) {
		for (int i = 0; i < NODE_COUNT; i++)
			createNode();

		addTestEdges();
		startNodeThreads();
		getNode(random.nextInt(NODE_COUNT)).generateMST();
	}


}
