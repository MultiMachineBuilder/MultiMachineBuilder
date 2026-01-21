package mmb.engine.recipe3;

import javax.swing.*;

import mmb.engine.texture.Textures;
import mmb.string.UnitFormatters;

import java.awt.*;
import java.util.List;

public class RecipePanel extends JPanel {
    public RecipePanel(Recipe recipe) {
        setLayout(new BorderLayout(4,4));
        
        // Top: Machine name
        String machineName = recipe.group.name;
        JLabel machineLabel = new JLabel(machineName, SwingConstants.CENTER);
        machineLabel.setFont(machineLabel.getFont().deriveFont(24f));
        add(machineLabel, BorderLayout.NORTH);

        // Bottom: Voltage / time / energy
        VoltageTier tier = recipe.recipe.minVoltage();
        String voltageText = tier.id.toUpperCase()
        + ", -" + UnitFormatters.energyFormatter.format(recipe.recipe.energyIn())
        + ", +" + UnitFormatters.energyFormatter.format(recipe.recipe.energyOut());
        JLabel infoLabel = new JLabel(voltageText, SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(16f));
        add(infoLabel, BorderLayout.SOUTH);

        // Center: Arrow
        JLabel arrow = new JLabel(new ImageIcon(Textures.get("arrow.png")));
        arrow.setHorizontalAlignment(SwingConstants.CENTER);
        arrow.setVerticalAlignment(SwingConstants.CENTER);
        add(arrow, BorderLayout.CENTER);

        // Left: Ingredients
        List<RecipeInput> ingredients = recipe.recipe.inputs();
        SlotGridPanel<RecipeInput> ingredientPanel = new SlotGridPanel<RecipeInput>(
                RecipeItemConverters::inputToSlotData,
                !recipe.recipe.shapeless() && recipe.group.isCrafting,  // shaped / Ulam spiral
                5      // columns
        );
        ingredientPanel.setItems(ingredients);
        add(ingredientPanel, BorderLayout.WEST);

        // Right: Outputs
        List<OutputRow> outputs = recipe.recipe.outputsRaw().rows;
        SlotGridPanel<OutputRow> outputPanel = new SlotGridPanel<OutputRow>(
                RecipeItemConverters::outputToSlotData,
                false, // scanline
                5
        );
        add(outputPanel, BorderLayout.EAST);
    }
}

