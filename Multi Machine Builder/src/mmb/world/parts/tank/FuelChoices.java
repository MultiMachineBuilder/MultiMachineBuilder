package mmb.world.parts.tank;

import java.util.*;

public class FuelChoices {
	public Tank assigned;
	public List<FueltankCombo> choices = new ArrayList<FueltankCombo>();
	public boolean add(FueltankCombo arg0) {
		return choices.add(arg0);
	}
	
	public FueltankCombo chosen;
	public void openSelector() {};
	public void apply() {
		
	}
}
