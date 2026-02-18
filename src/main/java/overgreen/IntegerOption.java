package overgreen;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.IntegerOptionBuilder;
import net.minecraft.resources.Identifier;

final class IntegerOption implements Option {
    private transient OverGreenConfig config;

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        config.dirty();
    }

    @Override
    public void write(JsonWriter writer) throws IOException {
        writer.value(value);
    }

    @Override
    public void read(JsonReader reader) throws IOException {
        value = reader.nextInt();
    }

    public IntegerOptionBuilder buildOption(ConfigBuilder builder, OverGreenConfig config, String id, int defaultValue) {
        this.config = config;

        if(config.isDirty())
            value = defaultValue;

        IntegerOptionBuilder optionBuilder = builder.createIntegerOption(Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, id))
            .setBinding(this::setValue, this::getValue)
            .setDefaultValue(defaultValue);

        config.buildOption(optionBuilder, id);

        return optionBuilder;
    }
}
