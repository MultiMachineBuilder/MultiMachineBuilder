/**
 * 
 */
package mmb.MENU.toolkit.auxiliary;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import mmb.MENU.toolkit.UIComponent;

/**
 * @author oskar
 *
 */
public class RenderAid {
	public Pane[] arrangeComponentsHorizontally(UIComponent[] comps, Dimension constraints, int spacing, Graphics g) {
		return arrangeComponentsHorizontally(comps, constraints.width, constraints.height, spacing, g);
	}
	public Pane[] arrangeComponentsHorizontally(UIComponent[] comps, int width, int height, int spacing, Graphics g) {
		Pane[] result = new Pane[comps.length];
		int totalBaseSize = 0;
		int totalWeightedSize = 0;
		
		//Sum up all sizes
		for (int i = 0; i < comps.length; i++) {
			UIComponent uiComponent = comps[i];
			totalBaseSize += uiComponent.baseWidth;
			totalWeightedSize += uiComponent.weightWidth;
		}
		
		int freeSpace = width - totalBaseSize;
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
		for(int i = 0; i < comps.length; i++) {
			UIComponent item = comps[i];
			result[i] = new Pane(new Point(offset, 0), item);
			item.setWidth(item.baseHeight);
			if(item.weightHeight > 0) {
				item.setHeight(height);
			}
			double finalWidth = item.baseWidth;
			finalWidth += item.weightWidth * weightedSizeUnit;
			item.setWidth((int) finalWidth);
			offset += spacing;
			offset += finalWidth;
			item.prepare(g);
		}
		return result;
	}
}
