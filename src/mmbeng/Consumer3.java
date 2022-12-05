/**
 * 
 */
package mmbeng;

/**
 * A three-way consumer
 * @author oskar
 * @param <T1>
 * @param <T2>
 * @param <T3> 
 */
public interface Consumer3<T1, T2, T3> {
	/**
	 * Runs the consumer
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public void accept(T1 arg1, T2 arg2, T3 arg3);
}
