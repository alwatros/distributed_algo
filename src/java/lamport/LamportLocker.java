package lamport;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;


public class LamportLocker {
	private List<Integer> tickets;
	private List<Boolean> entering;
	private int threadCount;
	private int randBound = 1000;
	private Random random;

	Logger logger = Logger.getLogger("LamportLocker");

	public LamportLocker(int threadCount) {
		this.threadCount = threadCount;
		init();
	}

	public void init() {
		this.tickets = new ArrayList<>(threadCount);
		this.entering = new ArrayList<>(threadCount);
		random = new SecureRandom();
		for (int i = 0; i < threadCount; i++) {
			tickets.add(0);
			entering.add(false);
		}
	}

	public void lock(int threadId) {
		entering.set(threadId, true);
		final int[] max = {0};
		tickets.forEach(t -> {
			max[0] = Math.max(max[0], t);
		});

		tickets.set(threadId, 1 + max[0]);
		entering.set(threadId, false);


		tickets.forEach(t -> {
			int i = tickets.indexOf(t);
			if(i != threadId) {
				while(entering.get(i)) {
					Thread.yield();
				}
				while (!tickets.get(i).equals(0) && (tickets.get(threadId) > tickets.get(i)
						|| (tickets.get(threadId).equals(tickets.get(i)) && threadId > i))) {
					Thread.yield();
				}

			}
		});

		doIt(threadId);
	}

	private void doIt(int threadId) {
		logger.info(threadId + " locked");
		unlock(threadId);
	}

	public void unlock(int threadId) {
		try {
			Thread.sleep(random.nextInt(randBound) + 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info(threadId + " unlocked");
		tickets.set(threadId, 0);
	}

	public void start() {
		for (int i = 0; i < threadCount; i++) {
			new Thread(new LamportThread(i, this)).start();
		}

	}
}
