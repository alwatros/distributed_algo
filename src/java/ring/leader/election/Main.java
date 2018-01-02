package ring.leader.election;

import java.util.ArrayList;
import java.util.List;

public class Main {

	private static final int NODE_COUNT = 19;
	private static List<Node> list = new ArrayList<>();

	public static void main(String[] args) throws InterruptedException {

		while(list.size() < NODE_COUNT) list.add(new Node(list.size()));

		list.stream().forEach(n -> {
			int i = list.indexOf(n);
			n.setNeighbours(list.get(i == 0 ? NODE_COUNT - 1 :  i - 1), list.get(i == NODE_COUNT - 1 ? 0 :  i + 1));
			new Thread(n).start();
		});

		list.get(Node.random.nextInt(NODE_COUNT)).rootRun();
	}


}
