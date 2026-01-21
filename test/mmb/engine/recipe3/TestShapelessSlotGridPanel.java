package mmb.engine.recipe3;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TestShapelessSlotGridPanel {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Shapeless Recipe Test");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            SlotGridPanel<String> panel = new SlotGridPanel<String>(item -> {
                Icon icon = new ColorIcon(item.equals("Catalyst") ? Color.GREEN : Color.GRAY);
                Color border = Color.BLACK;
                boolean convex = item.equals("Uncertain");
                return new SlotGridPanel.SlotData(icon, border, convex, "");
            }, false, 4);

            panel.setItems(List.of("Iron", "Copper", "Catalyst", "Uncertain", "Tin", "Copper"));

            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    static class ColorIcon implements Icon {
        private final Color color;
        public ColorIcon(Color color){ this.color = color; }
        @Override
		public int getIconWidth() { return 32; }
        @Override
		public int getIconHeight() { return 32; }
        @Override
		public void paintIcon(Component c, Graphics g, int x, int y){
            g.setColor(color);
            g.fillRect(x,y,32,32);
        }
    }
}
