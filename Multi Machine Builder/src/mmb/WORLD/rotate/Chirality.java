/**
 * 
 */
package mmb.WORLD.rotate;

import java.util.EventListener;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

/**
 * @author oskar
 * This enum defines chirality
 */
public enum Chirality {
	/**
	 * Describes ⥀ chirality, in order of [+X, -Y, -X, +Y]
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
	 * Describes ⥁ chirality, in order of [+X, +Y, -X, -Y]
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
	public abstract @Nonnull Rotation right(Rotation rotation);
	
	/**
	 * Rotates the object in the chirality's "left" direction, meaning the same as negative
	 * @param rotation rotation to rotate
	 * @return rotated rotation
	 */
	public abstract @Nonnull Rotation left(Rotation rotation);
	/**
	 * Rotates the object in the chirality's "right" direction, meaning the same as positive
	 * @param rotation rotation to rotate
	 * @return rotated rotation
	 */
	public abstract @Nonnull Side right(Side rotation);
	
	/**
	 * Rotates the object in the chirality's "left" direction, meaning the same as negative
	 * @param rotation rotation to rotate
	 * @return rotated rotation
	 */
	public abstract @Nonnull Side left(Side rotation);
	
	/**
	 * @return reverse of this chirality
	 */
	public abstract @Nonnull Chirality reverse();
	/**
	 * @author oskar
	 * Represents a chirality listener, used in chiral blocks
	 */
	public static interface ChiralityListener extends EventListener, Consumer<Chirality>{
		//unused
	}

}
