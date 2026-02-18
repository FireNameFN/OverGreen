package overgreen;

import net.caffeinemc.mods.sodium.api.config.structure.OptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.StatefulOptionBuilder;
import net.minecraft.network.chat.Component;

public final class OverGreenConfig {
    private transient boolean dirty;

    final IntegerOption gammaMultiplier = new IntegerOption();

    final BooleanOption disableOverworldFog = new BooleanOption();

    final BooleanOption disableNetherFog = new BooleanOption();

    final BooleanOption disableTheEndFog = new BooleanOption();

    final BooleanOption disableWaterFog = new BooleanOption();

    final IntegerOption fireTransparency = new IntegerOption();

    final IntegerOption fireOffset = new IntegerOption();

    final BooleanOption enablePermanentHud = new BooleanOption();

    final HudFormatOption hudFormat = new HudFormatOption();

    final BooleanOption forceReducedInfo = new BooleanOption();

    final BooleanOption showContainerTooltip = new BooleanOption();

    void dirty() {
        dirty = true;
    }

    boolean isDirty() {
        return dirty;
    }

    void flush() {
        if(!dirty)
            return;

        dirty = false;

        OverGreenConfigManager.saveConfig();
    }

    void buildOptionText(OptionBuilder builder, String id) {
        String key = "overgreen.config." + id;

        builder
            .setName(Component.translatable(key))
            .setTooltip(Component.translatable(key + ".tooltip"));
    }

    <T> void buildOption(StatefulOptionBuilder<T> builder, String id) {
        buildOptionText(builder, id);

        builder.setStorageHandler(this::flush);
    }

    public float getGammaMultiplier() {
        return gammaMultiplier.getValue() / 10f;
    }

    public boolean getDisableOverworldFog() {
        return disableOverworldFog.getValue();
    }

    public boolean getDisableNetherFog() {
        return disableNetherFog.getValue();
    }

    public boolean getDisableTheEndFog() {
        return disableTheEndFog.getValue();
    }

    public boolean getDisableWaterFog() {
        return disableWaterFog.getValue();
    }

    public float getFireTransparency() {
        return fireTransparency.getValue() / 20f;
    }

    public float getFireOffset() {
        return fireOffset.getValue() / 20f;
    }

    public boolean getEnablePermanentHud() {
        return enablePermanentHud.getValue();
    }

    public String getHudFormat() {
        return hudFormat.getValue();
    }

    public boolean getForceReducedInfo() {
        return forceReducedInfo.getValue();
    }

    public boolean getShowContainerTooltip() {
        return showContainerTooltip.getValue();
    }
}
