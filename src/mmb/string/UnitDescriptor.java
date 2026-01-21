package mmb.string;

public record UnitDescriptor(
        String baseName,     // "joule", "byte", "cylinder"
        String baseSymbol,   // "J", "B", "Cy"
        double baseScale     // 1 for J, 1 for B, 1 for Cy, 1000 for kg
) {}

