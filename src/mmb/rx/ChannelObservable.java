package mmb.rx;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import mmb.annotations.NN;
import mmb.annotations.Nil;

/**
 * A hot, shared {@link Observable} that routes events from an upstream source
 * to subscribers using an optional classifier-based index and an optional
 * predicate filter.
 * <p>
 * This class acts as a lightweight classified event hub:
 * <ul>
 *   <li>It subscribes once to the upstream {@code source} observable.</li>
 *   <li>Each event emitted by the source is classified using the supplied
 *       {@code classifier} function.</li>
 *   <li>Subscribers may either receive all events, or only events whose
 *       classifier value belongs to a given filter set.</li>
 *   <li>An additional predicate may be supplied to further refine which
 *       events are delivered to a subscriber.</li>
 * </ul>
 *
 * <h2>Filtering model</h2>
 * A subscription may specify:
 * <ul>
 *   <li><b>No classifier filter</b> – the subscriber receives all events
 *       (subject only to the optional predicate).</li>
 *   <li><b>A classifier filter set</b> – the subscriber receives only events
 *       whose classifier value is contained in that set.</li>
 *   <li><b>A predicate</b> – the subscriber receives only events for which
 *       the predicate evaluates to {@code true}.</li>
 *   <li><b>Both</b> – the event must match the classifier filter and satisfy
 *       the predicate.</li>
 * </ul>
 * <p>
 * Classifier-based filtering is implemented using internal buckets keyed by
 * classifier value, allowing event delivery to avoid scanning every filtered
 * subscriber for every event. This is useful when:
 * <ul>
 *   <li>there are many subscribers,</li>
 *   <li>each subscriber is interested in only a small subset of events, and</li>
 *   <li>events can be cheaply grouped by a stable key.</li>
 * </ul>
 *
 * <h2>Hot observable semantics</h2>
 * This class is a <b>hot observable</b>.
 * <p>
 * It subscribes to the upstream source when the {@code ChannelObservable}
 * instance is constructed, not when a downstream observer subscribes.
 * Therefore:
 * <ul>
 *   <li>Subscribers only receive events emitted <i>after</i> they subscribe.</li>
 *   <li>Past events are not replayed.</li>
 *   <li>All subscribers observe the same shared event stream.</li>
 * </ul>
 * This makes the class suitable for event-dispatch use cases such as:
 * <ul>
 *   <li>world or block update notifications,</li>
 *   <li>machine state changes,</li>
 *   <li>handler or network events,</li>
 *   <li>inventory, fluid, or energy change signals.</li>
 * </ul>
 *
 * <h2>Relationship to normal RxJava subscriptions</h2>
 * A normal RxJava subscription:
 * <pre>{@code
 * channel.subscribe(observer);
 * }</pre>
 * behaves as an unfiltered global subscription and receives all future events.
 * <p>
 * The custom overload:
 * <pre>{@code
 * channel.subscribe(observer, filter, predicate);
 * }</pre>
 * allows the caller to take advantage of the internal classifier index for
 * more efficient delivery.
 *
 * <h2>Classifier design considerations</h2>
 * The classifier should ideally produce values that are:
 * <ul>
 *   <li>stable,</li>
 *   <li>cheap to compute,</li>
 *   <li>cheap to compare and hash, and</li>
 *   <li>meaningful for routing subscriptions.</li>
 * </ul>
 * Typical examples include:
 * <ul>
 *   <li>block or chunk positions,</li>
 *   <li>IDs of items, fluids, or machines,</li>
 *   <li>enum categories,</li>
 *   <li>network or channel identifiers,</li>
 *   <li>logical subsystem keys.</li>
 * </ul>
 *
 * <h2>Threading and concurrency</h2>
 * This class is intended to support concurrent subscription registration and
 * event delivery using thread-safe internal collections.
 * <p>
 * However, observers should still be written with normal RxJava expectations
 * in mind:
 * <ul>
 *   <li>upstream emission ordering depends on the upstream source,</li>
 *   <li>observer callbacks may occur on whatever thread the source emits on,</li>
 *   <li>if stronger serialization guarantees are required, external
 *       synchronization or scheduler control may still be appropriate.</li>
 * </ul>
 *
 * <h2>Termination behavior</h2>
 * If the upstream source completes or fails:
 * <ul>
 *   <li>all currently registered subscribers are notified,</li>
 *   <li>the channel stops accepting meaningful future events, and</li>
 *   <li>late subscribers are immediately completed or failed accordingly.</li>
 * </ul>
 *
 * @param <Tevent>
 *     the type of events emitted by this observable and received by its
 *     subscribers
 * @param <Tclassifier>
 *     the type of classifier key used to group and efficiently route events
 *     to filtered subscribers
 */
public class ChannelObservable<Tevent, Tclassifier> extends Observable<Tevent> {
	private final Observable<Tevent> source;
	private final Function<Tevent, Tclassifier> classifier;

	//Subscribers interested in all events (subject to predicate). 
	private final Set<SubscriptionNode> globalSubscribers = new CopyOnWriteArraySet<>();
	
	//Subscribers interested only in events of specific classifier values.
	private final ConcurrentHashMap<Tclassifier, CopyOnWriteArraySet<SubscriptionNode>> classifiedSubscribers =
		new ConcurrentHashMap<>();
	
	//Subscription to upstream source.
	private final Disposable upstream;
	
	private volatile boolean terminated = false;
	private volatile Throwable terminalError = null;
	
	public ChannelObservable(Observable<Tevent> source, Function<Tevent, Tclassifier> classifier) {
		this.source = Objects.requireNonNull(source, "source");
		this.classifier = Objects.requireNonNull(classifier, "classifier");
		
		this.upstream = this.source.subscribe(
			this::dispatch,
			this::dispatchError,
			this::dispatchComplete
		);
	}
	
	@Override
	protected void subscribeActual(@NonNull Observer<? super @NonNull Tevent> observer) {
		subscribe(observer, null, null);
	}
	
	/**
	 * Subscribes to this channel observable using a set of classifications and a predicate
	 * @param observer the event observer. Must not be null
	 * @param filter Set of classifications. Null for all classifications.
	 * @param test Secondary test. Null for none.
	 * @throws NullPointerException when observer == null
	 */
	public void subscribe(
		@NN Observer<? super @NN Tevent> observer,
		@Nil Set<Tclassifier> filter,
		@Nil Predicate<Tevent> test
	) {
		Objects.requireNonNull(observer, "observer is null");
		
		Predicate<Tevent> predicate = test != null ? test : e -> true;
		SubscriptionNode node = new SubscriptionNode(observer, predicate);
		
		observer.onSubscribe(node);
		
		if (node.isDisposed()) return;
		
		// If already terminated, terminate immediately
		if (terminated) {
			node.dispose();
			Throwable err = terminalError;
			if (err != null) observer.onError(err);
			else observer.onComplete();
			return;
		}
		
		// No classifier filter = subscribe globally
		if (filter == null) {
			globalSubscribers.add(node);
			node.setCleanup(() -> globalSubscribers.remove(node));
			return;
		}
		
		// Empty filter = subscribe to nothing
		if (filter.isEmpty()) {
			node.dispose();
			observer.onComplete();
			return;
		}
		
		// Defensive copy so caller cannot mutate the filter after subscribing
		Set<Tclassifier> stableFilter = Collections.unmodifiableSet(new HashSet<>(filter));
		
		for (Tclassifier key : stableFilter) {
			classifiedSubscribers
				.computeIfAbsent(key, k -> new CopyOnWriteArraySet<>())
				.add(node);
		}
		
		node.setCleanup(() -> {
			for (Tclassifier key : stableFilter) {
				CopyOnWriteArraySet<SubscriptionNode> bucket = classifiedSubscribers.get(key);
				if (bucket != null) {
					bucket.remove(node);
					if (bucket.isEmpty()) {
						classifiedSubscribers.remove(key, bucket);
					}
				}
			}
		});
	}
	
	/**
	 * Subscribes to this channel observable using a set of classifications
	 * @param observer the event observer. Must not be null
	 * @param filter Set of classifications. Null for all classifications.
	 * @throws NullPointerException when observer == null
	 */
	public void subscribe(@NN Observer<? super @NN Tevent> observer, @Nil Set<Tclassifier> filter){
		subscribe(observer, filter, null);
	}
	
	/**
	 * Subscribes to this channel observable using a predicate
	 * @param observer the event observer. Must not be null
	 * @param test Secondary test. Null for none.
	 * @throws NullPointerException when observer == null
	 */
	public void subscribe(@NN Observer<? super @NN Tevent> observer, @Nil Predicate<Tevent> test){
		subscribe(observer, null, test);
	}
	
	private void dispatch(Tevent event) {
		if (terminated) return;
		
		// 1) Deliver to global subscribers
		for (SubscriptionNode node : globalSubscribers) {
			node.emit(event);
		}
		
		// 2) Deliver to classified subscribers
		Tclassifier key = classifier.apply(event);
		CopyOnWriteArraySet<SubscriptionNode> bucket = classifiedSubscribers.get(key);
		if (bucket != null) {
			for (SubscriptionNode node : bucket) {
				node.emit(event);
			}
		}
	}
	
	private void dispatchError(Throwable error) {
		if (terminated) return;
		terminated = true;
		terminalError = error;
		
		// Use a snapshot-like iteration; duplicate terminal delivery is prevented by node state
		for (SubscriptionNode node : globalSubscribers) {
			node.error(error);
		}
		for (CopyOnWriteArraySet<SubscriptionNode> bucket : classifiedSubscribers.values()) {
			for (SubscriptionNode node : bucket) {
				node.error(error);
			}
		}
		
		clearSubscribers();
	}
	
	private void dispatchComplete() {
		if (terminated) return;
		terminated = true;
		
		for (SubscriptionNode node : globalSubscribers) {
			node.complete();
		}
		for (CopyOnWriteArraySet<SubscriptionNode> bucket : classifiedSubscribers.values()) {
			for (SubscriptionNode node : bucket) {
				node.complete();
			}
		}
		
		clearSubscribers();
	}
	
	private void clearSubscribers() {
		globalSubscribers.clear();
		classifiedSubscribers.clear();
		upstream.dispose();
	}
	
	private final class SubscriptionNode implements Disposable {
		private final Observer<? super Tevent> observer;
		private final Predicate<Tevent> predicate;
		private final AtomicBoolean disposed = new AtomicBoolean(false);
		private volatile Runnable cleanup;
		
		SubscriptionNode(
			Observer<? super Tevent> observer,
			Predicate<Tevent> predicate
		) {
			this.observer = observer;
			this.predicate = predicate;
		}
		
		void setCleanup(Runnable cleanup) {
			this.cleanup = cleanup;
			if (disposed.get() && cleanup != null) {
				cleanup.run();
			}
		}
		
		void emit(Tevent event) {
			if (disposed.get()) return;
			if (!predicate.test(event)) return;
			observer.onNext(event);
		}
		
		void error(Throwable error) {
			if (disposed.compareAndSet(false, true)) {
				try {
					observer.onError(error);
				} finally {
					runCleanup();
				}
			}
		}
		
		void complete() {
			if (disposed.compareAndSet(false, true)) {
				try {
					observer.onComplete();
				} finally {
					runCleanup();
				}
			}
		}
		
		private void runCleanup() {
			Runnable c = cleanup;
			if (c != null) c.run();
		}
		
		@Override
		public void dispose() {
			if (disposed.compareAndSet(false, true)) {
				runCleanup();
			}
		}
		
		@Override
		public boolean isDisposed() {
			return disposed.get();
		}
	}
}