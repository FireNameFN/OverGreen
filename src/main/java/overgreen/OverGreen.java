package overgreen;

import java.util.List;

import net.minecraft.client.Minecraft;

public final class OverGreen {
    public static final String MOD_ID = "overgreen";

    private static final HudFormatter HUD_FORMATTER = new HudFormatter();

    public static OverGreenConfig getConfig() {
        return OverGreenConfigManager.getConfig();
    }

    public static boolean isPermanentHudVisible() {
        Minecraft minecraft = Minecraft.getInstance();

        if(!getConfig().getEnablePermanentHud() && !(minecraft.screen instanceof HudFormatScreen))
            return false;

        if(minecraft.debugEntries.isOverlayVisible())
            return false;

        if(minecraft.getCameraEntity() == null)
            return false;

        return true;
    }

    public static void formatHud(List<String> list) {
        HUD_FORMATTER.format(list);
    }

    static void updateHudFormat(String format) {
        HUD_FORMATTER.updateFormat(format);
    }
}
