package overgreen;

public final class OverGreenConfig {
    private transient boolean dirty;

    final IntegerOption gammaMultiplier = new IntegerOption();

    final IntegerOption fireTransparency = new IntegerOption();

    final IntegerOption fireOffset = new IntegerOption();

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

    public float getFireTransparency() {
        return fireTransparency.getValue() / 20f;
    }

    public float getFireOffset() {
        return fireOffset.getValue() / 20f;
    }
}
