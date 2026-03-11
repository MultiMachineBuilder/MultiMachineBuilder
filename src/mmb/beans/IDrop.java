package mmb.beans;

import mmb.annotations.NN;
import mmb.engine.chance.Chance;

public interface IDrop {
	/** @return the GUI drop */
	public @NN Chance drop();
	/**
	 * Sets the drop. If the target object does not override this method, it will fail.
	 * @param drop new drop
	 * @throws UnsupportedOperationException when the target object does not implement this method
	 */
	public default void setDrop(Chance drop) {
		throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " does not implement drop setting");
	}
}
