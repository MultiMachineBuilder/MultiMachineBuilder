/**
 * 
 */
package mmb.LAMBDAS;

/**
 * @author oskar
 *
 */
public interface ReversibleApplicator<A, B> {
	public void forward(A in, B tgt);
	public void backward(B in, A tgt);
	public default ReversibleApplicator<B, A> reverse(){
		ReversibleApplicator<A, B> that = this;
		return new ReversibleApplicator<B, A>(){
			@Override
			public void forward(B in, A tgt) {
				that.backward(in, tgt);
			}
			@Override
			public void backward(A in, B tgt) {
				that.forward(in, tgt);
			}
			@Override
			public ReversibleApplicator<A, B> reverse(){
				return that;
			}
		};
	}
}
