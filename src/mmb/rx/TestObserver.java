package mmb.rx;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import mmb.annotations.Nil;

/**
 * An {@link Observer} class for use in tests.
 * This class stores all received events, the cancellation and completion state.
 */
public class TestObserver<T> implements Observer<T> {
	@Nil private Disposable disposable;
	public final List<T> events = new ArrayList<>();
	private boolean complete;
	@Nil private Throwable error;
	
	public boolean isComplete() {
		return complete;
	}
	
	@Nil public Throwable getError() {
		return error;
	}
	
	@Nil public Disposable getDisposable() {
		return disposable;
	}
	
	@Override
	public void onSubscribe(@NonNull Disposable d) {
		disposable = d;
	}

	@Override
	public void onNext(@NonNull T t) {
		events.add(t);
	}

	@Override
	public void onError(@NonNull Throwable e) {
		complete = true;
		error = e;
	}

	@Override
	public void onComplete() {
		complete = true;
	}

}
