/**
 * 
 */
package mmb;

/**
 * @author oskar
 * A version of Consumer, which accepts primitive boolean values
 */
@FunctionalInterface
public interface BooleanConsumer {
	/**
	 * Performs this operation on the given argument.
	 * @param bool input
	 */
	public void accept(boolean bool);
	/**
	 * Returns a composed {@code BooleanConsumer} that performs, in sequence, this operation followed by the after operation.
	 * If performing either operation throws an exception, it is relayed to the caller of the composed operation.
	 * If performing this operation throws an exception,the after operation will not be performed.
	 * @param other second operation
	 * @return a composed {@code BooleanConsumer} that performs in sequence this operation followed by the after operation 
	 */
	public default BooleanConsumer andThen(BooleanConsumer other) {
		return b -> {
			accept(b);
			other.accept(b);
		};
	}
}
