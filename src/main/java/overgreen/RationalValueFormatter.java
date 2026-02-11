package overgreen;

import java.text.DecimalFormat;

import net.caffeinemc.mods.sodium.api.config.option.ControlValueFormatter;
import net.minecraft.network.chat.Component;

final class RationalValueFormatter implements ControlValueFormatter {
    private final double multiplier;

    private final DecimalFormat format;

    public RationalValueFormatter(double multiplier, String format) {
        this.multiplier = multiplier;
        this.format = new DecimalFormat(format);
    }

    @Override
    public Component format(int value) {
        return Component.literal(format.format(value * multiplier));
    }
}
