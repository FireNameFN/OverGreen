package overgreen;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.caffeinemc.mods.sodium.api.config.structure.BooleanOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.resources.Identifier;

final class BooleanOption implements Option {
    private transient OverGreenConfig config;

    private boolean value;

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;

        config.dirty();
    }

    @Override
    public void write(JsonWriter writer) throws IOException {
        writer.value(value);
    }

    @Override
    public void read(JsonReader reader) throws IOException {
        value = reader.nextBoolean();
    }

    public BooleanOptionBuilder buildOption(ConfigBuilder builder, OverGreenConfig config, String id, boolean defaultValue) {
        this.config = config;

        if(config.isDirty())
            value = defaultValue;

        BooleanOptionBuilder optionBuilder = builder.createBooleanOption(Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, id))
            .setBinding(this::setValue, this::getValue)
            .setDefaultValue(defaultValue);

        config.buildOption(optionBuilder, id);

        return optionBuilder;
    }
}
