/**
 * 
 */
package mmb.LAMBDAS;

/**
 * @author oskar
 * Represents a function that can be reversed
 */
public interface Reversible<A, B> {
	/**
	 * @param in value to convert normally
	 * @return the original calculation
	 */
	public B forward(A in);
	/**
	 * Runs this function backwards
	 * @param in result to convert back to original type
	 * @return the original function reversed
	 */
	public A backward(B in);
	/**
	 * Reverses this function
	 * @return function, in which directions are reversed
	 */
	default Reversible<B, A> reverse(){
		Reversible<A, B> that = this;
		return new Reversible<B, A>() {
			@Override
			public A forward(B in) {
				return that.backward(in);
			}
			@Override
			public B backward(A in) {
				return that.forward(in);
			}
			@Override
			public Reversible<A, B> reverse(){
				return that;
			}
		};
	}
}
