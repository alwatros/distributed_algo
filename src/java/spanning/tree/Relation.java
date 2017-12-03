package spanning.tree;

public enum Relation {

	PROBABLY_CHILD(false), UNCOMPLETED(false), COMPLETED(true), FORGOTTEN(true);

	private boolean finished;

	private Relation(boolean finished) {
		this.finished = finished;
	}


	public boolean isFinished() {
		return finished;
	}

}
