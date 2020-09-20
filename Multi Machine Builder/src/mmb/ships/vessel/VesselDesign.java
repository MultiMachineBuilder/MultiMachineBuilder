/**
 * 
 */
package mmb.ships.vessel;

import mmb.ships.DefectGenerator;
import mmb.ships.Defects;
import mmb.ships.Modularized;
import mmb.ships.Module;
import mmb.ships.modules.ShipModule;

/**
 * @author oskar
 *
 */
public class VesselDesign implements Modularized<VesselDesign, ShipModule>, UnitDesign  {

	public int designNumber;
	public VesselDraft assDraft;
	public boolean hasDraft = false;
	/**
	 * 
	 */
	public VesselDesign() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//Static subclasses
	/**
	 * Applies design changes
	 * @author oskar
	 *
	 */
	public static class DesignApprovalProcess implements DefectGenerator<DesignApprovalProcess,VesselDesign>{
		public DesignRequirements reqs;
		VesselDesign user;
		public DefectKeys data = new DefectKeys();
		public int[] numberOfLots;
		public VesselLot[] generatedLots;
		public DesignApprovalProcess(VesselDesign that, VesselDraft draft) {
			user = that;
		}
		public DesignApprovalProcess(VesselDesign that) {
			user = that;
		}

		/* (non-Javadoc)
		 * @see mmb.ships.DefectGenerator#createDefectGenerator()
		 */
		@Override
		public void createDefectGenerator() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see mmb.ships.Module#createModule(java.lang.Object)
		 */
		@Override
		public void createModule(VesselDesign object) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see mmb.ships.Module#startApplying()
		 */
		@Override
		public void startApplying() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see mmb.ships.Module#stopApplying()
		 */
		@Override
		public void stopApplying() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see mmb.ships.Module#getData()
		 */
		@Override
		public DefectKeys getData() {
			return data;
		}

	}
	
	/**
	 * Bring the design to the life
	 * @author oskar
	 *
	 */
	public static class DesignToLife {
		DesignApprovalProcess dap;
		public DesignToLife(VesselDesign design) {
			if(design.hasDraft) {
				dap = new DesignApprovalProcess(design, design.assDraft);
			}else {
				dap = new DesignApprovalProcess(design, VesselDraft.undrafted());
			}
			init();
			
		}
		public DesignToLife(DesignApprovalProcess rates) {
			dap = rates;
			init();
		}
		private void init() {
			
		}
		public VesselOrder createOrder() {
				VesselOrder order = new VesselOrder(); //create blank order
				order.assign(); //create order number
				Defects defects = new Defects(); //create blank defect generator
				defects.applyRates(dap); //use given defect rates
				order.defects = defects;
				defects.generate();
				dap.generatedLots = new VesselLot[dap.numberOfLots.length];
				for(int i = 0; i < dap.numberOfLots.length; i++) {
					int amount = dap.numberOfLots[i];
					VesselLot lot = new VesselLot();
					for(int j = 0; j < amount; j++) {
						VesselInstance vi = new VesselInstance();
					}
					dap.generatedLots[i] = lot;
					lot.defects.merge(defects);
					order.add(lot);
				}
		}
		public VesselLot createLot(int amount, VesselOrder order) {
			
		}
	}

	/* (non-Javadoc)
	 * @see mmb.ships.Modularized#add(mmb.ships.Module)
	 */
	@Override
	public void add(ShipModule module) {
		// TODO Auto-generated method stub
		
	}

}
