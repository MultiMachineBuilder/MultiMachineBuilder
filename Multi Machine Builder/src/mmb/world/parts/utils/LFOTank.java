/**
 * 
 */
package mmb.world.parts.utils;

import mmb.science.RnD.RnD;
import mmb.world.parts.modules.*;
import mmb.world.parts.tank.*;
import mmb.material.*;
import mmb.material.helper.*;
/**
 * @author oskar
 *
 */
public class LFOTank extends TankModule{
	FuelChooser choice;
	/**
	 * @param amount amount of each ingredient
	 */
	public LFOTank(double amount) {
		choice.add(new FueltankCombo().add(MaterialUtilities.diesel, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.start));
		choice.add(new FueltankCombo().add(MaterialUtilities.gasoline, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.start));
		choice.add(new FueltankCombo().add(MaterialUtilities.ethanol, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.basicFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.LPG, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.regularFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.CNG, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.betterFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.kerosene, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.advFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.hydrazine, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.advFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.acetylene, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.supFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.hydrogen, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.xFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.deuterium, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.xFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.metHydrogen, amount).add(MaterialUtilities.oxygen, amount).requireRnD(RnD.sup2Fuels));
		
		choice.add(new FueltankCombo().add(MaterialUtilities.diesel, amount).requireRnD(RnD.start));
		choice.add(new FueltankCombo().add(MaterialUtilities.gasoline, amount).requireRnD(RnD.start));
		choice.add(new FueltankCombo().add(MaterialUtilities.ethanol, amount).requireRnD(RnD.basicFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.LPG, amount).requireRnD(RnD.regularFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.CNG, amount).requireRnD(RnD.betterFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.kerosene, amount).requireRnD(RnD.advFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.hydrazine, amount).requireRnD(RnD.advFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.acetylene, amount).requireRnD(RnD.supFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.hydrogen, amount).requireRnD(RnD.xFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.deuterium, amount).requireRnD(RnD.xFuels));
		choice.add(new FueltankCombo().add(MaterialUtilities.metHydrogen, amount).requireRnD(RnD.sup2Fuels));
	}

	/* (non-Javadoc)
	 * @see mmb.world.parts.modules.PartModule#effects()
	 */
	@Override
	public PartModuleEffects effects() {
		// TODO Auto-generated method stub
		return null;
	}

}
