package mmb.fluid;

import java.util.List;

import com.pploder.events.Event;

import mmb.annotations.Nil;

/**
 * Defines an interface to fluid tanks and inventories
 */
public interface FluidHandler {
	/**
	 * Inserts a fluid into the first available matching tank.
	 * @param fluid fluid to insert
	 * @param amount amount to insert
	 * @return amount actually inserted
	 */
	public double insertFluid(Fluid fluid, double amount);
	/**
	 * Extracts a fluid from the first matching tank.
	 * @param fluid fluid to extract
	 * @param amount amount to extract
	 * @return amount actually extracted
	 */
	public double extractFluid(Fluid fluid, double amount);
	/**
	 * Checks how much of a fluid can be inserted.
	 * @param fluid fluid to insert
	 * @param amount amount to insert
	 * @return amount to insert
	 */
	public double checkInsert(Fluid fluid, double amount);
	/**
	 * Checks how much of a fluid can be extracted.
	 * @param fluid fluid to extract
	 * @param amount amount to extract
	 * @return amount to extract
	 */
	public double checkExtract(Fluid fluid, double amount);
	
	/** Invoked when contents of this fluid handler change */
	public Event<FluidEvent> fluidEvent();
	
	
	/**
	 * Checks if the fluid handler is a tank and if true, return its contents
	 * @return contents if {@code this} is a tank, {@code null} otherwise
	 */
	@Nil public FluidState getTankContents();
	public void setTankContents(FluidState contents);
	
	/**
	 * Gets all tanks compromising this fluid handler. The list reflects changes in this fluid handler.
	 * @return all tanks compromising this fluid handler
	 */
	public List<FluidHandler> getTanks();
}
