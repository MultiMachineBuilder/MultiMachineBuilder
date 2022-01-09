/**
 * 
 */
package mmb.LAMBDAS;

/**
 * @author oskar
 * @param <T1> the first type
 * @param <T2> the second type
 * @param <T3> the thirs type
 * Defines a consumer with 3 arguments
 */
public interface Consumer3<T1, T2, T3> {
	/**
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 * @param arg3 the third argument
	 */
	public void apply(T1 arg1, T2 arg2, T3 arg3);
}
