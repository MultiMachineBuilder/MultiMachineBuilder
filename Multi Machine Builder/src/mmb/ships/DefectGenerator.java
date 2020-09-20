/**
 * 
 */
package mmb.ships;

/**
 * @author oskar
 *
 */
@SuppressWarnings("rawtypes")
public interface DefectGenerator<T extends DefectGenerator, U> extends Module<DefectGenerator<T, U>, U, DefectGenerator.DefectKeys> {
	public static class DesignDefect implements Defect {

		/* (non-Javadoc)
		 * @see mmb.ships.DefectGenerator.Defect#listAffectedUnits()
		 */
		@Override
		public UnitWorld[] listAffectedUnits() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	public static class LotDefect implements Defect {

		/* (non-Javadoc)
		 * @see mmb.ships.DefectGenerator.Defect#listAffectedUnits()
		 */
		@Override
		public UnitWorld[] listAffectedUnits() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	public static class UnitDefect implements Defect {
		private UnitWorld affected;
		/* (non-Javadoc)
		 * @see mmb.ships.DefectGenerator.Defect#listAffectedUnits()
		 */
		@Override
		public UnitWorld[] listAffectedUnits() {
			return new UnitWorld[] {affected};
		}
		
	}
	public static interface Defect {
		public UnitWorld[] listAffectedUnits();
	}
	
	public static class DefectKeys{
		double defectRateOrder = 0;
		double defectRateLot = 0;
		double defectRateUnit = 0;
		double defectRatePiece = 0;
	}
	
	public void createDefectGenerator();
}
