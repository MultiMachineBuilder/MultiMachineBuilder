package mmb.world.parts.tank;


import mmb.material.types.Fluid;

public interface IInputBuffer extends Refiller, FluidStorage {
	/**
	 * "Flush" this input buffer (send its contents to its target)
	 */
	public void flush();
	/**
	 * add @param type support
	 * @param type target of modification
	 */
	public void addAcceptableInput(Fluid type);
}
