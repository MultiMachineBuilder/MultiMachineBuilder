package mmb.fluid;

import java.util.List;
import java.util.Objects;

import com.pploder.events.Event;

import mmb.annotations.Nil;
import mmb.engine.CatchingEvent;
import mmb.engine.Verify;
import mmb.engine.debug.Debugger;

/**
 * A storage for a single fluid.
 */
public class FluidTank implements FluidHandler {
	private static final Debugger debug = new Debugger("FLUID TANK");
	
	private double capacity;
	private double quantity;
	@Nil private Fluid fluid;
	private boolean locked;
	private final Event<FluidEvent> event;
	
	public FluidTank() {
		this.capacity = 1;
		this.quantity = 0;
		this.fluid = null;
		this.locked = false;
		this.event = new CatchingEvent<>(debug, "Failed to process tank chnage event");
	}
	
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		FluidState oldContents = getTankContents();
		this.capacity = capacity;
		fire(oldContents);
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		FluidState oldContents = getTankContents();
		this.quantity = quantity;
		fire(oldContents);
	}
	public Fluid getFluid() {
		return fluid;
	}
	public void setFluid(Fluid fluid) {
		FluidState oldContents = getTankContents();
		if(fluid == null || !Objects.equals(fluid, this.fluid)) this.locked = false;
		this.fluid = fluid;
		fire(oldContents);
	}
	public void setLocked(Fluid fluid) {
		FluidState oldContents = getTankContents();
		if(fluid == null) {
			this.locked = false;
		}else if(quantity == 0) {
			this.fluid = fluid;
			this.locked = true;
		}else if(Objects.equals(fluid, this.fluid)) {
			this.locked = true;
		}
		fire(oldContents);
	}
	public boolean isLocked() {
		return locked;
	}
	
	private void fire(FluidState oldState) {
		FluidState newState = getTankContents();
		if(Objects.equals(oldState, newState)) return;
		FluidEvent eventData = new FluidEvent(oldState, newState, 0);
		event.trigger(eventData);
	}
	
	@Override
	public double insertFluid(Fluid fluid1, double amount) {
		double difference = checkInsert(fluid1, amount);
		FluidState oldContents = getTankContents();
		if(difference == 0) return 0;
		this.quantity += difference;
		this.fluid = fluid1;
		fire(oldContents);
		return difference;
	}
	@Override
	public double extractFluid(Fluid fluid1, double amount) {
		double difference = checkExtract(fluid1, amount);
		FluidState oldContents = getTankContents();
		if(difference == 0) return 0;
		this.quantity -= difference;
		this.fluid = (quantity == 0 && !locked) ? null : fluid1;
		fire(oldContents);
		return difference;
	}
	@Override
	public double checkInsert(Fluid fluid1, double amount) {
		Objects.requireNonNull(fluid1, "fluid is null");
		Verify.requireNonNegative(amount);
		if((locked || quantity > 0) && !Objects.equals(fluid1, fluid)) return 0;
		double remainingVolume = capacity - quantity;
		if(remainingVolume <= 0) return 0;
		return Math.min(amount, remainingVolume);
	}
	@Override
	public double checkExtract(Fluid fluid1, double amount) {
		Objects.requireNonNull(fluid1, "fluid is null");
		Verify.requireNonNegative(amount);
		if(!Objects.equals(fluid1, fluid)) return 0;
		return Math.min(amount, quantity);
	}
	@Override
	public Event<FluidEvent> fluidEvent() {
		return event;
	}
	@Override
	public @Nil FluidState getTankContents() {
		return new FluidState(capacity, quantity, fluid, locked);
	}
	@Override
	public List<FluidHandler> getTanks() {
		return List.of(this);
	}

	@Override
	public void setTankContents(FluidState contents) {
		Objects.requireNonNull(contents, "contents is null");
		FluidState oldContents = getTankContents();
		capacity = contents.capacity();
		quantity = contents.quantity();
		fluid = contents.fluid();
		locked = contents.locked();
		fire(oldContents);
	}
}
