package mmb.rx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class Source<T> extends Observable<T> {
	private final CopyOnWriteArrayList<Observer<? super T>> observers = new CopyOnWriteArrayList<>();
	
	@Override
	protected synchronized void subscribeActual(@NonNull Observer<? super @NonNull T> observer) {
		// TODO Auto-generated method stub
		Disposable d = Disposable.fromRunnable(() -> observers.remove(observer));
		try {
			if(error != null) {
				observer.onError(error);
				return;
			}
			if(complete) {
				observer.onComplete();
				return;
			}
		} catch (Exception e) {
			RxJavaPlugins.onError(e);
			return;
		}
		observers.add(observer);
		observer.onSubscribe(d);
	}
	
	
	private boolean complete = false;
	private Throwable error = null;
	private void check() {
		if(complete) throw new IllegalStateException("This Source is complete");
	}
	
	/**
	 * Fires the supplied value as an event
	 * @param value value for the event
	 * @throws IllegalStateException after an error
	 */
	public synchronized void next(T value) {
		check();
		List<Observer<? super T>> deadObservers = new ArrayList<>();
		for(Observer<? super T> action: observers) {
			 try {
				 action.onNext(value);
			 }catch(Exception e) {
				 RxJavaPlugins.onError(e);
				 deadObservers.add(action);
			 }
		}
		observers.removeAll(deadObservers);
	}
	
	public synchronized void fireError(Throwable value) {
		check();
		for(Observer<? super T> action: observers) {
			 try {
				 action.onError(value);
			 }catch(Exception e) {
				 RxJavaPlugins.onError(e);
			 }
		}
		complete = true;
		error = value;
		observers.clear();
	}
	
	public synchronized void fireComplete() {
		check();
		for(Observer<? super T> action: observers) {
			 try {
				 action.onComplete();
			 }catch(Exception e2) {
				 RxJavaPlugins.onError(e2);
			 }
		}
		complete = true;
		observers.clear();
	}

}
