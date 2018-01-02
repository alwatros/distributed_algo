package ring.leader.election;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Node implements Runnable {

	public static final Random random = new SecureRandom();

	private static final String CONTACT_MESSAGE = "Contact from %s to %s \tLeader proposal %s";
	private static final String LEADER_MESSAGE = "%s: Leader is %s";

	private Node[] neighbours = new Node[2];
	private int id;
	private Node candidateForLeader;
	private BlockingQueue<Node> receivedContact = new ArrayBlockingQueue<>(10);
	private BlockingQueue<Node> performedContact = new ArrayBlockingQueue<>(10);
	private BlockingQueue<Node> todo = new ArrayBlockingQueue<>(10);

	private Map<Node, Integer> urn = null;


	public Node(int id) {
		this.id = id;
	}

	public void setNeighbours(Node node, Node node2) {
		neighbours[0] = node;
		neighbours[1] = node2;
		candidateForLeader = this;
	}

	public void rootRun() throws InterruptedException {
		receivedContact.put(this);
		Map<Node, Integer> urn = Collections.synchronizedMap(new HashMap<>());
		urn.put(this, 1);
		contact(neighbours[0], urn);
		contact(neighbours[1], urn);
	}

	@Override
	public void run() {
		while (receivedContact.size() < 2) {
			try {
				Thread.sleep(random.nextInt(500));
				while (todo.size() > 0 && urn != null) contact(todo.poll(), urn);
			} catch (InterruptedException e) {}
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			printLeader();
		}

	}

	private void contact(Node node, Map<Node, Integer> urn) throws InterruptedException {
		node.contact(this, candidateForLeader, urn);
		performedContact.put(node);
	}

	private void contact(Node node, Node proposedLeader, Map<Node, Integer> urn) throws InterruptedException {
		log(String.format(CONTACT_MESSAGE, node, this, proposedLeader));
		this.urn = urn;
		receivedContact.add(node);

		candidateForLeader = chooseCandidate(proposedLeader);

		vote(candidateForLeader, urn);

		Node toContact = (node == neighbours[0] ? neighbours[1] : neighbours[0]);
		if (!performedContact.contains(toContact)) {
			todo.put(toContact);
		}
	}

	private Node chooseCandidate(Node proposedLeader) {
		Node candidateForLeader = null;
		switch (random.nextInt(4)) {
			case 0:
				candidateForLeader = this;
				break;
			case 1:
				candidateForLeader = proposedLeader;
				break;
			case 2:
				candidateForLeader = neighbours[0];
				break;
			case 3:
				candidateForLeader = neighbours[1];
				break;
		}
		return candidateForLeader;
	}

	public void vote(Node candidateForLeader, Map<Node, Integer> urn) {
		if(urn.containsKey(candidateForLeader)) {
			Integer val = urn.get(candidateForLeader) + 1;
			urn.put(candidateForLeader, val);
		} else urn.put(candidateForLeader, 1);
	}

	public void printLeader() {
		urn.keySet().stream().forEach(k -> { if(urn.get(k) > urn.get(candidateForLeader)) candidateForLeader = k; });
		log(String.format(LEADER_MESSAGE, this, candidateForLeader));
	}

	@Override
	public String toString() {
		return new StringBuilder().append("{ ").append(id).append(" }").toString();
	}

	public void log(String msg) {
		System.out.println(msg);
	}

}
