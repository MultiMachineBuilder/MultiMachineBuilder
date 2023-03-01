/**
 * 
 */
package monniasza.collects.selfset;

import java.util.HashMap;
import java.util.function.Function;

import mmb.NN;
import mmb.Nil;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 */
public class HashSelfSet{
	private HashSelfSet() {}
	@NN public static <@Nil K, V extends Identifiable<K>> SelfSet<@Nil K, V> createNullable(Class<V> valclass){
		return createNullable(valclass, Identifiable::id);
	}
	@NN public static <@Nil K, V> SelfSet<@Nil K, V> createNullable(Class<V> valclass, Function<V, K> id){
		return new BaseMapSelfSet<>(new HashMap<>(), true, valclass, id);
	}
	@NN public static <@NN K, V extends Identifiable<K>> SelfSet<@NN K, V> createNonnull(Class<V> valclass){
		return createNonnull(valclass, Identifiable::id);
	}
	@NN public static <@NN K, V> SelfSet<@NN K, V> createNonnull(Class<V> valclass, Function<V, @NN K> id){
		return new BaseMapSelfSet<>(new HashMap<>(), false, valclass, id);
	}

}
