package icu.easyj.core.util;

/**
 * 比较工具类
 *
 * @author wangliang181230
 */
public abstract class ComparableUtils {

	/**
	 * Compare a and b.
	 *
	 * @param a   the comparable object a
	 * @param b   the comparable object b
	 * @param <T> the type of the a and b
	 * @return 0: equals   -1: {@code a < b}   1: {@code a > b}
	 */
	public static <T extends Comparable> int compare(T a, T b) {
		if (a == b) {
			return 0;
		} else if (a == null) {
			return -1;
		} else if (b == null) {
			return 1;
		}

		return a.compareTo(b);
	}

	/**
	 * Compare a and b.
	 * This overload method to avoid frequent packaging of primitive data.
	 *
	 * @param a the comparable object a
	 * @param b the comparable object b
	 * @return 0: equals   -1: {@code a < b}   1: {@code a > b}
	 */
	public static int compare(int a, int b) {
		if (a == b) {
			return 0;
		} else if (a < b) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * Compare a and b.
	 * This overload method to avoid frequent packaging of primitive data.
	 *
	 * @param a the comparable object a
	 * @param b the comparable object b
	 * @return 0: equals   -1: {@code a < b}   1: {@code a > b}
	 */
	public static int compare(long a, long b) {
		if (a == b) {
			return 0;
		} else if (a < b) {
			return -1;
		} else {
			return 1;
		}
	}
}
