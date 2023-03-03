/**
 * 
 */
package mmb.engine;

/**
 * A three-way consumer
 * @author oskar
 * @param <T1> type of the first input
 * @param <T2> type of the second input
 * @param <T3> type of the third input
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
