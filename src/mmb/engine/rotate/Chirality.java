/**
 * 
 */
package mmb.engine.rotate;

import java.util.EventListener;
import java.util.function.Consumer;

import mmb.NN;

/**
 * Defines chirality (the direction of rotation, without the rotation)
 * @author oskar
 */
public enum Chirality {
	/**
	 * Describes anti-clockwise chirality, in order of [+X, -Y, -X, +Y]
	 */
	L {
		@Override
		public Rotation right(Rotation rotation) {
			return rotation.ccw();
		}

		@Override
		public Rotation left(Rotation rotation) {
			return rotation.cw();
		}

		@Override
		public Side right(Side rotation) {
			return rotation.ccw();
		}

		@Override
		public Side left(Side rotation) {
			return rotation.cw();
		}

		@Override
		public Chirality reverse() {
			return R;
		}

	},
	/**
	 * Describes clockwise chirality, in order of [+X, +Y, -X, -Y]
	 */
	R {
		@Override
		public Rotation right(Rotation rotation) {
			return rotation.cw();
		}

		@Override
		public Rotation left(Rotation rotation) {
			return rotation.ccw();
		}

		@Override
		public Side right(Side rotation) {
			return rotation.cw();
		}

		@Override
		public Side left(Side rotation) {
			return rotation.ccw();
		}

		@Override
		public Chirality reverse() {
			return L;
		}
	};
	
	/**
	 * Rotates the object in the chirality's "right" direction, meaning the same as positive
	 * @param rotation rotation to rotate
	 * @return rotated rotation
	 */
	public abstract @NN Rotation right(Rotation rotation);
	
	/**
	 * Rotates the object in the chirality's "left" direction, meaning the same as negative
	 * @param rotation rotation to rotate
	 * @return rotated rotation
	 */
	public abstract @NN Rotation left(Rotation rotation);
	/**
	 * Rotates the object in the chirality's "right" direction, meaning the same as positive
	 * @param rotation rotation to rotate
	 * @return rotated rotation
	 */
	public abstract @NN Side right(Side rotation);
	
	/**
	 * Rotates the object in the chirality's "left" direction, meaning the same as negative
	 * @param rotation rotation to rotate
	 * @return rotated rotation
	 */
	public abstract @NN Side left(Side rotation);
	
	/**
	 * @return reverse of this chirality
	 */
	public abstract @NN Chirality reverse();
	/**
	 * Called when chirality changes
	 * @author oskar
	 */
	public static interface ChiralityListener extends EventListener, Consumer<Chirality>{
		//unused
	}

}
