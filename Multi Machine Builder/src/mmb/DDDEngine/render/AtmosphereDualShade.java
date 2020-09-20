/**
 * 
 */
package mmb.DDDEngine.render;

import java.awt.Color;

import e3d.MathPlus;

/**
 * @author oskar
 *
 */
public class AtmosphereDualShade implements Atmosphere {

	public Color ground, first;
	public double offset;
	public double falloff;
	/**
	 * 
	 */
	public AtmosphereDualShade() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see mmb.DDDEngine.render.Atmosphere#getColor(double)
	 */
	@Override
	public Color getColor(double pos) {
		
		if(pos <= 0) {
			return first;
		}else if(pos < offset) {
			double x = pos / offset;
			double r = MathPlus.interp(ground.getRed(), first.getRed(), x);
			double g = MathPlus.interp(ground.getGreen(), first.getGreen(), x);
			double b = MathPlus.interp(ground.getBlue(), first.getBlue(),x);
			double a = MathPlus.interp(ground.getAlpha(), first.getAlpha(),x);
			return new Color((int)r, (int)g ,(int)b , (int)a);
		}else if(pos == offset) {
			return first;
		}else {
			double x = 1/(1+(falloff/(pos-offset)));
			double a = 255 * x;
			double r = first.getRed()*x;
			double g = first.getGreen()*x;
			double b = first.getBlue()*x;
			return new Color((int)r, (int)g ,(int)b , (int)a);
		}
	}

}
