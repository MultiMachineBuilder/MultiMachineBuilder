package mmb.items;

import mmb.parts.tank.ChangeReport;

public interface Consumable {
	public ChangeReport consume(Consumable toRemove, double quantity);
}
