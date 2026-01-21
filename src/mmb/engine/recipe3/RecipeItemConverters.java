package mmb.engine.recipe3;

import java.awt.*;
import java.util.function.Function;

import javax.swing.*;

public final class RecipeItemConverters {

    /**
     * Converts a RecipeInput to SlotGridPanel.SlotData
     */
    public static SlotGridPanel.SlotData inputToSlotData(RecipeInput input) {
        Color color;
        if (input.group() == Group.NONE) color = Color.DARK_GRAY;
        else if (input.group() == Group.ANY) color = Color.LIGHT_GRAY;
        else if (input.minAmount() == 0) color = Color.RED; // prohibited
        else if (input.consumedAmount() == 0) color = Color.GREEN; // catalyst
        else color = Color.GRAY; // normal consumed

        Icon icon = getIconForGroup(input.group());
        return new SlotGridPanel.SlotData(icon, color, false, input.group().title); 
    }

    /**
     * Converts an OutputRow to SlotGridPanel.SlotData
     */
    public static SlotGridPanel.SlotData outputToSlotData(OutputRow output) {
        Icon icon = output.item().icon();
        Color color = voltageColor(output.minVoltage());
        boolean convex = output.chance() < 1;
        return new SlotGridPanel.SlotData(icon, color, convex, output.tooltip()); 
    }

    /** Dummy placeholder: fetch icon for a group */
    private static Icon getIconForGroup(Group group) {
        // Replace with real icon logic
    	return group.icon.toIcon();
    }

    /** Dummy placeholder: map voltage tier to color */
    private static Color voltageColor(VoltageTier voltage) {
        return voltage.color;
    }
}
