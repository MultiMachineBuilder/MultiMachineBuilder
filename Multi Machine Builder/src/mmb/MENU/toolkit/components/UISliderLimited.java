/**
 * 
 */
package mmb.MENU.toolkit.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import mmb.MENU.toolkit.ComponentEventHandler;
import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.UIDefaults;
import mmb.MENU.toolkit.components.UILayoutLinear.Axis;
import mmb.MENU.toolkit.events.UIMouseEvent;

/**
 * @author oskar
 *
 */
public class UISliderLimited extends UIComponent {
	private Runnable scrolled;
	public Color sliderFill = UIDefaults.slider;
	public double value = 0;
	/**
	 * Limits of the value
	 */
	public double min=0, max = 100;
	public boolean bounded = true;
	
	public int getLength() {
		return length;
	}
	
	/**
	 * @return position of slider from top or left.
	 */
	public double getProgress() {
		double progress = (value-min) / (max-min);
		return progress*length;		
	}

	/**
	 * Set position of slider from top or left.
	 * @param progress the progress to set
	 */
	public void setProgress(double progress) {
		double interp = progress / length;
		value =  (interp*max)+((1-interp)*min);
		if(bounded) {
			if(progress < 0) value = min;
			if(progress > getLength()) value = max;
		}
	}
	/**
	 * Width of slider rectangle
	 */
	public int thickness = 8;
	private UILayoutLinear.Axis alignment;
	private int length;

	@Override
	public void prepare(Graphics g) {
		if(getHeight() > getWidth()) {
			alignment = Axis.VERTICAL;
			length = getHeight();
		}else{
			alignment = Axis.HORIZONTAL;
			length = getWidth();
		}
	}

	@Override
	public void render(Graphics g) {
		drawBound(g);
		int hthick = thickness / 2;
		int progress = (int)getProgress();
		if(alignment == null) return;
		switch(alignment) {
			case HORIZONTAL:
				g.drawRect(progress-hthick, 0, thickness, getHeight());
				g.setColor(sliderFill);
				g.fillRect(progress-hthick, 0, thickness, getHeight());
				break;
			case VERTICAL:
				g.drawRect(0, progress-hthick, getWidth(), thickness);
				g.setColor(sliderFill);
				g.fillRect(0, progress-hthick, getWidth(), thickness);
				break;
			default:
				break;
		}
		
	}
	
	private class SliderCEH implements ComponentEventHandler {
		private Point prevPos;
		@Override
		public void drag(UIMouseEvent e) {
			int diff = 0;
			double progress = getProgress();
			switch(alignment) {
				case HORIZONTAL:
					diff = prevPos.x - e.p.x;
					progress -= diff;
					break;
				case VERTICAL:
					diff = prevPos.y - e.p.y;
					progress -= diff;
					break;
				default:
					throw new IllegalArgumentException("Invalid orientation");
			}
			prevPos = e.p;
			setProgress(progress);
			scrolled();
		}

		@Override
		public void press(UIMouseEvent e) {
			prevPos = e.p;
		}
		
	}
	private final ComponentEventHandler ceh = new SliderCEH();
	@Override
	public ComponentEventHandler getHandler() {
		return ceh;
	}
	
	//OPTIONAL INTERFACE METHODS
	public void scrolled() {
		if(scrolled == null) return;
		scrolled.run();
	}
	
	//CONSTRUCTORS
	public UISliderLimited(Runnable scrolled) {
		super();
		this.scrolled = scrolled;
	}

	public UISliderLimited() {
		super();
	}

}
