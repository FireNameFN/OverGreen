package overgreen;

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

        OverGreen.saveConfig();
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
}
