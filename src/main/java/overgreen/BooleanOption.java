package overgreen;

import net.caffeinemc.mods.sodium.api.config.structure.BooleanOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.resources.Identifier;

final class BooleanOption {
    private transient OverGreenConfig config;

    private boolean value;

    public boolean getValue() {
        return value;
    }

    private void setValue(boolean value) {
        this.value = value;

        config.dirty();
    }

    public BooleanOptionBuilder buildOption(ConfigBuilder builder, OverGreenConfig config, String id, boolean defaultValue) {
        this.config = config;

        if(config.isDirty())
            value = defaultValue;

        return builder.createBooleanOption(Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, id))
            .setStorageHandler(config::flush)
            .setBinding(this::setValue, this::getValue)
            .setDefaultValue(defaultValue);
    }
}
