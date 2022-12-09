/**
 * 
 */
package mmbbase.menu.components;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * @author http://www.devx.com/tips/Tip/5342
 *
 */
public class CheckBoxList extends JList<@Nonnull JCheckBox>
{
	private static final long serialVersionUID = -6753085033817535388L;
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

   public CheckBoxList() {
      setCellRenderer(new CellRenderer());

      addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
               int index = locationToIndex(e.getPoint());
               if(index < 0) return;
               JCheckBox checkbox = getModel().getElementAt(index);
               if(checkbox != null) {
            	   checkbox.setSelected(!checkbox.isSelected());
            	   repaint();
               }
            }
      });

      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   }

   protected class CellRenderer implements ListCellRenderer<JCheckBox>
   {
      @Override
	public Component getListCellRendererComponent(
                    JList list, JCheckBox value, int index,
                    boolean isSelected, boolean cellHasFocus)
      {
         value.setBackground(isSelected ?
                 getSelectionBackground() : getBackground());
         value.setForeground(isSelected ?
                 getSelectionForeground() : getForeground());
         value.setEnabled(isEnabled());
         value.setFont(getFont());
         value.setFocusPainted(false);
         value.setBorderPainted(true);
         value.setBorder(isSelected ?
          UIManager.getBorder(
           "List.focusCellHighlightBorder") : noFocusBorder);
         return value;
      }
   }
}