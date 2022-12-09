/**
 * 
 */
package mmbbase.menu.components;

import javax.swing.*;

import mmbbase.menu.components.ImageTextList.ImageAndText;

import java.awt.*;

/**
 * @author oskar
 *
 */
public class ImageTextList extends JList<ImageAndText> {
	public ImageTextList() {
		super();
		setCellRenderer(new CellRenderer());
	}

	/**
		 * @author oskar
		 *
		 */
	public class ImageAndText {
		public Image img;
		public String s;
	}

	protected class CellRenderer implements ListCellRenderer<ImageAndText>{

		@Override
		public Component getListCellRendererComponent(JList<? extends ImageAndText> list, ImageAndText value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = new JLabel();
			label.setText(value.s);
			label.setIcon(new ImageIcon(value.img));
			return label;
		}
		
	}
}
