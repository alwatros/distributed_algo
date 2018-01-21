package lamport;


public class LamportThread implements Runnable {

	private int id;
	private LamportLocker locker;

	public LamportThread(int id, LamportLocker locker) {
		this.id = id;
		this.locker = locker;
	}

	@Override
	public void run() {
		while (true) {
			locker.lock(this.id);
		}
	}

}