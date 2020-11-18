/**
 * 
 */
package mmb.MENU.toolkit.components;

import java.awt.Graphics;
import java.awt.Point;
import mmb.MENU.toolkit.Container;
import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.auxiliary.Pane;

/**
 * @author oskar
 *
 */
public class UILayoutLinear extends Container{
	public enum Axis {
		HORIZONTAL, VERTICAL
	}
	/**
	 * Spacing between components.
	 *  0 means that neighboring edges coincide
	 *  below 0 means components overlap
	 *  above 0 means they are separated
	 *  1 means components touch (but not overlap or coincide)
	 */
	public int spacing = 2;
	
	public UIComponent[] contents;
	
	public UILayoutLinear(Axis direction, UIComponent... components) {
		super();
		contents = components;
		this.direction = direction;
	}
	public UILayoutLinear(Axis direction) {
		super();
		contents = new UIComponent[0];
		this.direction = direction;
	}

	public Axis direction;
	private Graphics g2;

	@Override
	public void prepare(Graphics g) {
		g2 = g;
		comppos = new Pane[contents.length];
		for(int i = 0; i < contents.length; i++) {
			comppos[i] = new Pane(new Point(), contents[i]);
		}
		switch(direction) {
		case HORIZONTAL:
			arrangeHorizontal();
			break;
		case VERTICAL:
			arrangeVertical();
			break;
		default:
			throw new IllegalArgumentException("Invalid orientation");
		}
	}
	
	private void arrangeVertical() { //arrange vertically
		int totalBaseSize = 0;
		int totalWeightedSize = 0;
		
		//Sum up all sizes
		for (int i = 0; i < comppos.length; i++) {
			UIComponent uiComponent = comppos[i].comp;
			totalBaseSize += uiComponent.baseHeight;
			totalWeightedSize += uiComponent.weightHeight;
		}
		
		int freeSpace = getHeight() - totalBaseSize;
		if(freeSpace < 0) {
			System.out.println("Too little free space!");
		}
		double weightedSizeUnit;
		if(totalWeightedSize == 0) {
			weightedSizeUnit = 0;
		}else {
			weightedSizeUnit = freeSpace / totalWeightedSize;
		}
		
		//Resize components. If weighted horizontal size is > 0 , then component fills the layout horizontally
		int offset = spacing;
		for(int i = 0; i < comppos.length; i++) {
			comppos[i].setPos(0, offset);
			UIComponent item = comppos[i].comp;
			item.setWidth(item.baseWidth);
			if(item.weightWidth > 0) {
				item.setWidth(getWidth());
			}
			double finalHeight = item.baseHeight;
			finalHeight += item.weightHeight * weightedSizeUnit;
			item.setHeight((int) finalHeight);
			offset += spacing;
			offset += finalHeight;
			item.prepare(g2);
		}
		
	}
	private void arrangeHorizontal() { //arrange horizontally
		int totalBaseSize = 0;
		int totalWeightedSize = 0;
		
		//Sum up all sizes
		for (int i = 0; i < comppos.length; i++) {
			UIComponent uiComponent = comppos[i].comp;
			totalBaseSize += uiComponent.baseWidth;
			totalWeightedSize += uiComponent.weightWidth;
		}
		
		int freeSpace = getWidth() - totalBaseSize;
		if(freeSpace < 0) {
			System.out.println("Too little free space!");
		}
		double weightedSizeUnit;
		if(totalWeightedSize == 0) {
			weightedSizeUnit = 0;
		}else {
			weightedSizeUnit = freeSpace / totalWeightedSize;
		}
		
		//Resize components. If weighted horizontal size is > 0 , then component fills the layout horizontally
		int offset = spacing;
		for(int i = 0; i < comppos.length; i++) {
			comppos[i].setPos(offset, 0);
			UIComponent item = comppos[i].comp;
			item.setWidth(item.baseHeight);
			if(item.weightHeight > 0) {
				item.setHeight(getHeight());
			}
			double finalWidth = item.baseWidth;
			finalWidth += item.weightWidth * weightedSizeUnit;
			item.setWidth((int) finalWidth);
			offset += spacing;
			offset += finalWidth;
			item.prepare(g2);
		}
	}
	public UILayoutLinear() {
		super();
	}
}
