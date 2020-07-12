/**
 * 
 */
package mmb.world.parts.utils;

import java.util.*;

import e3d.MathPlus;

/**
 * @author oskar
 * Instantiate with createNumericalCurve(ISPKey...) for custom choice or createNumericalCurveArr(ISPKey[]) for array
 */
public class NumericalCurve {
	static {
		
	}
	public static NumericalCurve 	ground = NumericalCurve.createNumericalCurve(new ISPKey(0, 0.8), new ISPKey(100000, 0.95), new ISPKey(500000, 0.7), new ISPKey(2500000, 0)),
									middle = NumericalCurve.createNumericalCurve(new ISPKey(0, 0.95), new ISPKey(10000, 1.1), new ISPKey(100000, 0.6), new ISPKey(500000, 0)),
									vacuum = NumericalCurve.createNumericalCurve(new ISPKey(0, 1.2), new ISPKey(10000, 0.5), new ISPKey(100000, 0.05), new ISPKey(200000, 0)),
									heavyAtm = NumericalCurve.createNumericalCurve(new ISPKey(0, 0.6), new ISPKey(100000, 0.7), new ISPKey(500000, 0.95), new ISPKey(2500000, 1), new ISPKey(250000000, 0));
	
	public static NumericalCurve createNumericalCurve(ISPKey... data) {
		return new NumericalCurve(data);
	}
	public static NumericalCurve createNumericalCurveArr(ISPKey[] data) {
		return new NumericalCurve(data);
	}

	public ISPKey[] points;
	public boolean constant = false;
	
	
	private NumericalCurve(ISPKey[] data) {
		super();
		this.points = data;
	}

	public NumericalCurve(double ispMAX) {
		super();
		points = new ISPKey[]{new ISPKey(0, ispMAX)};
		constant = true;
	}

	public double performance(double press) {
		if(constant) {
			return points[0].val;
		}else {
			for(int i = 0; i < points.length; i++) {
				ISPKey j = points[i];
				if(press == j.press) {
					return j.val;
				}else if(press < j.press) {
					if(i == points.length - 1) {
						return j.val;
					}else {
						ISPKey k = points[i+1];
						return MathPlus.remap(j.press, k.press, j.val, k.val, press);
					}
				}
			}
		}
		return 0;
	}
	
	public NumericalCurve mutiplyPerformance(double x) {
		SortedSet<ISPKey> d = new TreeSet<ISPKey>();
		for(int i = 0; i < points.length; i++) {
			d.add(new ISPKey(points[i].press, points[i].val*x));
		}
		return createNumericalCurve((ISPKey[]) d.toArray());
	}

}
