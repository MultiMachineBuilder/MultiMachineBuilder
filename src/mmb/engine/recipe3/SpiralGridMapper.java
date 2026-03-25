package mmb.engine.recipe3;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import monniasza.collects.grid.Grid;

public final class SpiralGridMapper {
    private SpiralGridMapper() {}

    /**
     * Maps a grid into a spiral-ordered list.
     *
     * The point (centerX, centerY) in the grid is treated as spiral coordinate (0,0).
     * Negative center coordinates are allowed.
     *
     * Example:
     * If centerX = 1 and centerY = 1, then grid[1][1] is the spiral center.
     *
     * Spiral order:
     * center, right, up, left, left, down, down, right, right, right, ...
     *
     * @param grid input grid
     * @param centerX X coordinate of spiral center in the input grid
     * @param centerY Y coordinate of spiral center in the input grid
     * @param <T> item type
     * @return list of all in-bounds grid elements in spiral order
     */
    public static <T> List<T> toSpiralList(Grid<T> grid, int centerX, int centerY) {
        List<Point> coords = toSpiralCoordinates(grid.width(), grid.height(), centerX, centerY);
        List<T> result = new ArrayList<>(coords.size());

        for (Point p : coords) {
            result.add(grid.get(p.x, p.y));
        }

        return result;
    }

    /**
     * Generates the spiral traversal coordinates for a grid.
     *
     * The point (centerX, centerY) in the grid is treated as spiral coordinate (0,0).
     * Only coordinates inside the grid are returned.
     *
     * @param width grid width
     * @param height grid height
     * @param centerX X coordinate of spiral center in the input grid
     * @param centerY Y coordinate of spiral center in the input grid
     * @return list of in-bounds grid coordinates in spiral order
     */
    public static List<Point> toSpiralCoordinates(int width, int height, int centerX, int centerY) {
        List<Point> result = new ArrayList<>(Math.max(0, width * height));
        if (width <= 0 || height <= 0) return result;

        // Start at spiral center (0,0) => grid (centerX, centerY)
        int sx = 0;
        int sy = 0;

        // Add center if in bounds
        addIfInBounds(result, centerX + sx, centerY + sy, width, height);

        if (result.size() >= width * height) return result;

        int stepLength = 1;

        while (result.size() < width * height) {
            // Right
            for (int i = 0; i < stepLength && result.size() < width * height; i++) {
                sx += 1;
                addIfInBounds(result, centerX + sx, centerY + sy, width, height);
            }

            // Up
            for (int i = 0; i < stepLength && result.size() < width * height; i++) {
                sy -= 1;
                addIfInBounds(result, centerX + sx, centerY + sy, width, height);
            }

            stepLength++;

            // Left
            for (int i = 0; i < stepLength && result.size() < width * height; i++) {
                sx -= 1;
                addIfInBounds(result, centerX + sx, centerY + sy, width, height);
            }

            // Down
            for (int i = 0; i < stepLength && result.size() < width * height; i++) {
                sy += 1;
                addIfInBounds(result, centerX + sx, centerY + sy, width, height);
            }

            stepLength++;
        }

        return result;
    }

    private static void addIfInBounds(List<Point> out, int x, int y, int width, int height) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            out.add(new Point(x, y));
        }
    }
}