package overgreen;

import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ExternalButtonOptionBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

final class HudFormatOption {
    private transient OverGreenConfig config;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;

        OverGreen.updateHudFormat(value);

        config.dirty();
        config.flush();
    }

    public ExternalButtonOptionBuilder buildOption(ConfigBuilder builder, OverGreenConfig config, String id, String defaultValue) {
        this.config = config;

        if(config.isDirty())
            value = defaultValue;

        OverGreen.updateHudFormat(value);

        return builder.createExternalButtonOption(Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, id))
            .setScreenConsumer(this::createScreen);
    }

    private void createScreen(Screen parent) {
        HudFormatScreen screen = new HudFormatScreen(parent, this);

        Minecraft.getInstance().setScreen(screen);
    }
}
