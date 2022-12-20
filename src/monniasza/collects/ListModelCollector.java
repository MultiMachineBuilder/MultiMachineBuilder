/**
 * 
 */
package monniasza.collects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import javax.swing.DefaultListModel;

import mmb.NN;

/**
 * @author oskar
 * @param <T> type of values
 */
public class ListModelCollector<T> implements Collector<T, List<T>, DefaultListModel<T>> {
	
	@SuppressWarnings("unchecked")
	/**
	 * @param <T> type of values
	 * @return a stream collector for list models
	 */
	@NN public static <T> ListModelCollector<T> create(){return (ListModelCollector<T>) collector;}
	@NN private static final ListModelCollector<?> collector = new ListModelCollector<>();
	private ListModelCollector(){}
	
	@Override
	public Supplier<List<T>> supplier() {
		return ArrayList::new;
	}

	@Override
	public BiConsumer<List<T>, T> accumulator() {
		return List::add;
	}

	@Override
	public BinaryOperator<List<T>> combiner() {
		return Collects::inplaceAddLists;
	}

	@Override
	public Function<List<T>, DefaultListModel<T>> finisher() {
		return Collects::newListModel;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.emptySet();
	}
}
