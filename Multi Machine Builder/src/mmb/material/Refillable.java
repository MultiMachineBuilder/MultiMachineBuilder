package mmb.material;

import mmb.items.Addable;
import mmb.world.parts.tank.ChangeReport;

public interface Refillable {
	public ChangeReport addMaterial(Addable toAdd, double quantity);
}
