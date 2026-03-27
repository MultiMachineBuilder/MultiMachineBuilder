package mmb.rx;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ChannelObservableTest {
	
	private record Event(String channel, int value, String tag) {}
	
	private static Event e(String channel, int value) {
		return new Event(channel, value, "");
	}
	
	private static Event e(String channel, int value, String tag) {
		return new Event(channel, value, tag);
	}
	
	@Test
	void globalSubscriberReceivesAllEvents() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = channel.test();
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onNext(e("C", 3));
		
		observer.assertValues(
			e("A", 1),
			e("B", 2),
			e("C", 3)
		);
		observer.assertNotComplete();
		observer.assertNoErrors();
	}
	
	@Test
	void classifiedSubscriberReceivesOnlyMatchingClassifier() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of("A"), null);
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onNext(e("A", 3));
		source.onNext(e("C", 4));
		
		observer.assertValues(
			e("A", 1),
			e("A", 3)
		);
		observer.assertNotComplete();
		observer.assertNoErrors();
	}
	
	@Test
	void classifiedSubscriberReceivesAnyOfMultipleClassifiers() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of("A", "C"), null);
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onNext(e("C", 3));
		source.onNext(e("D", 4));
		source.onNext(e("A", 5));
		
		observer.assertValues(
			e("A", 1),
			e("C", 3),
			e("A", 5)
		);
		observer.assertNotComplete();
		observer.assertNoErrors();
	}
	
	@Test
	void predicateOnlySubscriberReceivesOnlyMatchingEvents() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, (Event e) -> e.value() % 2 == 0);
		
		source.onNext(e("A", 1));
		source.onNext(e("A", 2));
		source.onNext(e("B", 3));
		source.onNext(e("B", 4));
		
		observer.assertValues(
			e("A", 2),
			e("B", 4)
		);
		observer.assertNotComplete();
		observer.assertNoErrors();
	}
	
	@Test
	void classifierAndPredicateAreBothApplied() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(
			observer,
			Set.of("A", "B"),
			e -> e.value() >= 10
		);
		
		source.onNext(e("A", 5));
		source.onNext(e("A", 10));
		source.onNext(e("B", 12));
		source.onNext(e("C", 20));
		source.onNext(e("B", 9));
		
		observer.assertValues(
			e("A", 10),
			e("B", 12)
		);
		observer.assertNotComplete();
		observer.assertNoErrors();
	}
	
	@Test
	void emptyFilterCompletesImmediatelyAndReceivesNothing() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of(), null);
		
		observer.assertNoValues();
		observer.assertComplete();
		observer.assertNoErrors();
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		
		observer.assertNoValues();
	}
	
	@Test
	void multipleSubscribersReceiveTheirOwnMatchingEvents() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> aOnly = new TestObserver<>();
		TestObserver<Event> bOnly = new TestObserver<>();
		TestObserver<Event> all = channel.test();
		
		channel.subscribe(aOnly, Set.of("A"), null);
		channel.subscribe(bOnly, Set.of("B"), null);
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onNext(e("C", 3));
		source.onNext(e("A", 4));
		
		aOnly.assertValues(
			e("A", 1),
			e("A", 4)
		);
		
		bOnly.assertValues(
			e("B", 2)
		);
		
		all.assertValues(
			e("A", 1),
			e("B", 2),
			e("C", 3),
			e("A", 4)
		);
	}
	
	@Test
	void disposedSubscriberStopsReceivingEvents() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of("A"), null);
		
		source.onNext(e("A", 1));
		observer.dispose();
		source.onNext(e("A", 2));
		source.onNext(e("A", 3));
		
		observer.assertValues(
			e("A", 1)
		);
		observer.assertNotComplete();
		observer.assertNoErrors();
	}
	
	@Test
	void unrelatedClassifierDoesNotLeakIntoFilteredSubscriber() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of("X"), null);
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onNext(e("C", 3));
		
		observer.assertNoValues();
		observer.assertNotComplete();
		observer.assertNoErrors();
	}
	
	@Test
	void upstreamCompletionIsForwardedToAllSubscribers() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> global = channel.test();
		TestObserver<Event> filtered = new TestObserver<>();
		channel.subscribe(filtered, Set.of("A"), null);
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onComplete();
		
		global.assertValues(
			e("A", 1),
			e("B", 2)
		);
		global.assertComplete();
		global.assertNoErrors();
		
		filtered.assertValues(
			e("A", 1)
		);
		filtered.assertComplete();
		filtered.assertNoErrors();
	}
	
	@Test
	void upstreamErrorIsForwardedToAllSubscribers() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> global = channel.test();
		TestObserver<Event> filtered = new TestObserver<>();
		channel.subscribe(filtered, Set.of("A"), null);
		
		RuntimeException ex = new RuntimeException("boom");
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onError(ex);
		
		global.assertValues(
			e("A", 1),
			e("B", 2)
		);
		global.assertError(ex);
		global.assertNotComplete();
		
		filtered.assertValues(
			e("A", 1)
		);
		filtered.assertError(ex);
		filtered.assertNotComplete();
	}
	
	@Test
	void lateSubscriberAfterCompletionCompletesImmediately() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		source.onComplete();
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of("A"), null);
		
		observer.assertNoValues();
		observer.assertComplete();
		observer.assertNoErrors();
	}
	
	@Test
	void lateSubscriberAfterErrorFailsImmediately() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		IOException ex = new IOException("broken");
		source.onError(ex);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of("A"), null);
		
		observer.assertNoValues();
		observer.assertError(ex);
		observer.assertNotComplete();
	}
	
	@Test
	void nullPredicateBehavesAsAcceptAll() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of("A"), null);
		
		source.onNext(e("A", 1));
		source.onNext(e("A", 2));
		
		observer.assertValues(
			e("A", 1),
			e("A", 2)
		);
	}
	
	@Test
	void nullFilterBehavesAsGlobalSubscription() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, null, e -> e.value() > 1);
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onNext(e("C", 3));
		
		observer.assertValues(
			e("B", 2),
			e("C", 3)
		);
	}
	
	@Test
	void classifierCanBeNullIfFilterContainsNull() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of((String) null), null);
		
		source.onNext(new Event(null, 1, ""));
		source.onNext(e("A", 2));
		source.onNext(new Event(null, 3, ""));
		
		observer.assertValues(
			new Event(null, 1, ""),
			new Event(null, 3, "")
		);
	}
	
	@Test
	void observerCanSubscribeViaNormalRxChain() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Integer> observer = channel
			.filter(e -> e.channel().equals("A"))
			.map(Event::value)
			.test();
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onNext(e("A", 3));
		
		observer.assertValues(1, 3);
		observer.assertNoErrors();
		observer.assertNotComplete();
	}
	
	@Test
	void sameSubscriberLogicCanExistForDifferentBuckets() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, Set.of("A", "B"), e -> e.value() < 5);
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		source.onNext(e("A", 5));
		source.onNext(e("C", 1));
		source.onNext(e("B", 7));
		
		observer.assertValues(
			e("A", 1),
			e("B", 2)
		);
	}
	
	@Test
	void filterSetIsDefensivelyCopied() {
		PublishSubject<Event> source = PublishSubject.create();
		ChannelObservable<Event, String> channel =
			new ChannelObservable<>(source, Event::channel);
		
		Set<String> mutable = new java.util.HashSet<>(List.of("A"));
		
		TestObserver<Event> observer = new TestObserver<>();
		channel.subscribe(observer, mutable, null);
		
		// Mutate after subscription — should not affect registration
		mutable.add("B");
		mutable.remove("A");
		
		source.onNext(e("A", 1));
		source.onNext(e("B", 2));
		
		observer.assertValues(
			e("A", 1)
		);
	}
}