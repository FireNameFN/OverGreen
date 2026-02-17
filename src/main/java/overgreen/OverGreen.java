package overgreen;

import java.util.List;

public final class OverGreen {
    public static final String MOD_ID = "overgreen";

    private static final HudFormatter HUD_FORMATTER = new HudFormatter();

    public static OverGreenConfig getConfig() {
        return OverGreenConfigManager.getConfig();
    }

    public static void formatHud(List<String> list) {
        HUD_FORMATTER.format(list);
    }

    static void updateHudFormat(String format) {
        HUD_FORMATTER.updateFormat(format);
    }
}
