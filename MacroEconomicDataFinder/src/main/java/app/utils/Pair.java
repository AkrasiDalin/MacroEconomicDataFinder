package app.utils;

/**
 * @param <L> The left element
 * @param <R> The right element
 */
public class Pair<L, R> {
	private L l;
	private R r;

	/**
	 * Creates a new pair of 2 elements
	 * 
	 * @param l left element of the pair
	 * @param r right elemet of the pair
	 */
	public Pair(L l, R r) {
		this.l = l;
		this.r = r;
	}

	/**
	 * Returns the left element
	 * 
	 * @return L retunrs the left element
	 */
	public L getL() {
		return l;
	}

	/**
	 * Returns the right element
	 * 
	 * @return R return the right element 
	 */
	public R getR() {
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.l + ", " + this.r + ")";
	}
}
