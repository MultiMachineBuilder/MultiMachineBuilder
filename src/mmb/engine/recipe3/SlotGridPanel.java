package mmb.engine.recipe3;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Panel to display recipe ingredients or outputs.
 * Supports crafting (shaped) and non-crafting (linear) recipes.
 * Items can be updated dynamically.
 * @param <T> Type of item
 */
public class SlotGridPanel<T> extends JPanel {

    public record SlotData(Icon icon, Color borderColor, boolean convex, String tooltip) {}

    private List<T> items = List.of();
    private final Function<T, SlotData> converter;
    private final boolean craftingRecipe;
    private final int shapedColumns;

    /**
     * @param converter Function to convert item -> SlotData
     * @param craftingRecipe True if this is a crafting recipe group
     * @param shapedColumns Number of columns to use for shaped recipes
     */
    public SlotGridPanel(Function<T, SlotData> converter, boolean craftingRecipe, int shapedColumns) {
        this.converter = converter;
        this.craftingRecipe = craftingRecipe;
        this.shapedColumns = shapedColumns;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
    }

    /** Set items/recipe to display and rebuild layout */
    public void setItems(List<T> newItems) {
        this.items = newItems != null ? newItems : List.of();
        rebuildGrid();
    }

    private void rebuildGrid() {
        removeAll();

        if (craftingRecipe) {
            buildShapedGrid();
        } else {
            buildLinearGrid();
        }

        revalidate();
        repaint();
    }

    /** Generates a scanline/flow layout */
    private void buildLinearGrid() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
        for (T item : items) {
            SlotData data = converter.apply(item);
            add(new SlotComponent(data.icon(), data.borderColor(), data.convex()));
        }
    }

    /** Generates a grid layout using Ulam spiral coordinates */
    private void buildShapedGrid() {
        // Determine rows
        int rows = (int) Math.ceil(items.size() / (double) shapedColumns);
        setLayout(new GridLayout(rows, shapedColumns, 2, 2));

        // Generate spiral coordinates
        List<Point> spiralCoords = generateUlamSpiralCoordinates(items.size(), shapedColumns, rows);

        // Determine center slot
        Point center = new Point(shapedColumns / 2, rows / 2);

        // Map each coordinate to its item
        Map<Point, T> coordItemMap = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            coordItemMap.put(spiralCoords.get(i), items.get(i));
        }

        // Fill grid row-major
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < shapedColumns; x++) {
                T item = coordItemMap.get(new Point(x, y));
                SlotData data;
                if (item != null) {
                    data = converter.apply(item);
                    // Darken center slot
                    if (x == center.x && y == center.y) {
                        data = new SlotData(data.icon(), data.borderColor.darker(), data.convex(), data.tooltip());
                    }
                } else {
                    data = new SlotData(null, Color.DARK_GRAY, false, "");
                }
                add(new SlotComponent(data.icon(), data.borderColor(), data.convex()));
            }
        }
    }

    /** Generate spiral coordinates for a rectangular grid */
    private List<Point> generateUlamSpiralCoordinates(int n, int cols, int rows) {
        List<Point> coords = new ArrayList<>(n);
        boolean[][] visited = new boolean[rows][cols];

        int x = cols / 2;
        int y = rows / 2;

        int dx = 0;
        int dy = -1;
        int maxSteps = Math.max(cols, rows) * Math.max(cols, rows);

        for (int i = 0; coords.size() < n && i < maxSteps; i++) {
            if (x >= 0 && x < cols && y >= 0 && y < rows && !visited[y][x]) {
                coords.add(new Point(x, y));
                visited[y][x] = true;
            }

            // Change direction at spiral corners
            if (x == y || (x < cols/2 && x == -y) || (x > cols/2 && x == 1 - y)) {
                int tmp = dx;
                dx = -dy;
                dy = tmp;
            }

            x += dx;
            y += dy;
        }

        // Fill remaining coordinates in row-major order if spiral didn't generate enough
        outer:
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (coords.size() >= n) break outer;
                Point p = new Point(col, row);
                if (!coords.contains(p)) coords.add(p);
            }
        }

        return coords;
    }



    /** Basic slot component with border, convex, and optional icon */
    private static class SlotComponent extends JComponent {
        private final Icon icon;
        private final Color borderColor;
        private final boolean convex;
        private static final int SLOT_SIZE = 40;

        public SlotComponent(Icon icon, Color borderColor, boolean convex) {
            this.icon = icon;
            this.borderColor = borderColor;
            this.convex = convex;
            setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
            setMinimumSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
            setMaximumSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2.setColor(convex ? borderColor.brighter() : Color.LIGHT_GRAY);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

            // Border
            g2.setColor(borderColor.darker());
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);

            // Icon centered
            if (icon != null) {
                int iconX = (getWidth() - icon.getIconWidth()) / 2;
                int iconY = (getHeight() - icon.getIconHeight()) / 2;
                icon.paintIcon(this, g2, iconX, iconY);
            }

            g2.dispose();
        }
    }

}