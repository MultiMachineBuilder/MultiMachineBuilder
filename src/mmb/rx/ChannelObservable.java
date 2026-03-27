package mmb.rx;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import mmb.annotations.NN;
import mmb.annotations.Nil;

public class ChannelObservable<Tevent, Tclassifier> extends Observable<Tevent> {
	private final Observable<Tevent> source;
	private final Function<Tevent, Tclassifier> classifier;
	
	public ChannelObservable(Observable<Tevent> source, Function<Tevent, Tclassifier> classifier) {
		this.source = source;
		this.classifier = classifier;
	}
	
	@Override
	protected void subscribeActual(@NonNull Observer<? super @NonNull Tevent> observer) {
		subscribe(observer);
	}
	
	public void subscribe(@NN Observer<? super @NN Tevent> observer, @Nil Set<Tclassifier> filter, @Nil Predicate<Tevent> test) {
		
	}

}
