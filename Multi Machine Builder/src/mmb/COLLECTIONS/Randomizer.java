/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import mmb.BEANS.RandomPropability;

/**
 * @author oskar
 * the {@code Randomizer} is a collection, which can be used to randomly select its items.
 * 
 * Any change to the contents will invalidate this randomizer until 
 */
public interface Randomizer<T extends RandomPropability> extends Set<T> {
	/**
	 * @return next random value
	 * @throws IllegalStateException if recalculate[] was not called after change
	 */
	public T next();
	
	/**
	 * @return is this randomizer ready to use?
	 */
	public boolean isReady();
	
	/**
	 * Ensures that this randomizer is ready to use
	 */
	default public void ensureReady() {
		if(!isReady()) reset();
	}
	/**
	 * Creates an infinite randomizer stream
	 * @return a stream
	 */
	default public Iterator<T> streamValues(){
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public T next() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}
	/**
	 * 
	 * @throws IllegalStateException if no changes were made after previous recalculation
	 */
	public void reset();
	/**
	 * Creates a finite randomizer stream
	 * @param amount the amount of items to produce
	 * @return a stream
	 */
	default public Iterator<T> streamValues(int amount){
		return new Iterator<T>() {
			int index = 0;
			@Override
			public boolean hasNext() {
				return index < amount;
			}

			@Override
			public T next() {
				if(index >= amount) {
					throw new NoSuchElementException("Ran out of amount");
				}
				index++;
				return next();
			}
		};
	}
}
