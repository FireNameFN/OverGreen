package overgreen;

import net.minecraft.util.Mth;

final class OverGreenZoomHandler {
    private boolean handleScroll;

    private int steps;

    private double zoom = 1;

    private double dynamicZoom = 1;

    private double sensitivityReduction = 1;

    public void enableZoom() {
        OverGreenConfig config = OverGreen.getConfig();

        handleScroll = config.getZoomStep() > 1;

        this.zoom = 1 / config.getInitialZoom();
    }

    public void disableZoom() {
        handleScroll = false;

        steps = 0;

        zoom = 1;

        sensitivityReduction = 1;
    }

    public void stepZoom(int value) {
        int nextSteps = steps + value;

        OverGreenConfig config = OverGreen.getConfig();

        double nextZoom = config.getInitialZoom() * Math.pow(config.getZoomStep(), nextSteps);

        if(nextZoom < 1)
            return;

        steps = nextSteps;

        zoom = 1 / nextZoom;
    }

    public void tickZoom(float delta) {
        OverGreenConfig config = OverGreen.getConfig();

        dynamicZoom = OverGreenMath.expDecay(dynamicZoom, zoom, config.getZoomExpDecay(), delta);

        sensitivityReduction = 1 / Mth.lerp(config.getSensitivityReduction(), 1, 1 / dynamicZoom);
    }

    public boolean shouldHandleScroll() {
        return handleScroll;
    }

    public double getDynamicZoom() {
        return dynamicZoom;
    }

    public double getSensitivityReduction() {
        return sensitivityReduction;
    }
}
