package lamport;

public class Main {

	static int NUMBER_OF_THREADS = 40;


	public static void main(String[] args) {
		LamportLocker lamport = new LamportLocker(NUMBER_OF_THREADS);
		lamport.start();
	}
}
