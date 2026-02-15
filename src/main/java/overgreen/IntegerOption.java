package overgreen;

import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.IntegerOptionBuilder;
import net.minecraft.resources.Identifier;

final class IntegerOption {
    private transient OverGreenConfig config;

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        config.dirty();
    }

    public IntegerOptionBuilder buildOption(ConfigBuilder builder, OverGreenConfig config, String id, int defaultValue) {
        this.config = config;

        if(config.isDirty())
            value = defaultValue;

        return builder.createIntegerOption(Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, id))
            .setStorageHandler(config::flush)
            .setBinding(this::setValue, this::getValue)
            .setDefaultValue(defaultValue);
    }
}
