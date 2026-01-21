package mmb.engine.recipe3;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TestShapedSlotGridPanel {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Shaped Recipe Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            SlotGridPanel<String> panel = new SlotGridPanel<>(item -> {
                // Just use a colored square icon
                Icon icon = new ColorIcon(item.equals("Iron") ? Color.LIGHT_GRAY : Color.ORANGE);
                Color border = item.equals("Prohibited") ? Color.RED : Color.BLACK;
                return new SlotGridPanel.SlotData(icon, border, false);
            }, true, 3);

            panel.setItems(List.of("Iron", "Copper", "Tin", "Prohibited", "Iron", "Copper", "Tin", "Prohibited"));

            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Simple colored square icon for testing
    static class ColorIcon implements Icon {
        private final Color color;
        public ColorIcon(Color color){ this.color = color; }
        public int getIconWidth() { return 32; }
        public int getIconHeight() { return 32; }
        public void paintIcon(Component c, Graphics g, int x, int y){
            g.setColor(color);
            g.fillRect(x,y,32,32);
        }
    }
}

