package mmb.items;

import mmb.world.parts.tank.ChangeReport;

public interface Consumable {
	public ChangeReport consume(Consumable toRemove, double quantity);
}
