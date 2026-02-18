package overgreen;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

final class OverGreenConfigManager {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("overgreen.json");

    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(BooleanOption.class, new OptionAdapter(BooleanOption::new))
        .registerTypeAdapter(IntegerOption.class, new OptionAdapter(IntegerOption::new))
        .registerTypeAdapter(HudFormatOption.class, new OptionAdapter(HudFormatOption::new))
        .setPrettyPrinting().create();

    private static OverGreenConfig config;

    public static void loadConfig() {
        if(!Files.exists(CONFIG_PATH)) {
            config = new OverGreenConfig();

            config.dirty();

            return;
        }

        try(Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            config = GSON.fromJson(reader, OverGreenConfig.class);
        } catch(IOException | JsonSyntaxException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static void saveConfig() {
        try(Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        } catch(IOException e) {
            throw new RuntimeException("Failed to save config", e);
        }
    }

    public static OverGreenConfig getConfig() {
        return config;
    }

    public static void registerConfig(ConfigBuilder builder) {
        RationalValueFormatter FORMATTER_10 = new RationalValueFormatter(0.1, "0.0");

        RationalValueFormatter FORMATTER_20 = new RationalValueFormatter(0.05, "0.00");

        builder.registerOwnModOptions()
            .setIcon(Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, "icon_mono.png"))
            .setColorTheme(builder.createColorTheme().setBaseThemeRGB(0x00FF00))
            .addPage(builder.createOptionPage()
                .setName(Component.literal("Processing"))
                .addOption(config.gammaMultiplier.buildOption(builder, config, "gamma_multiplier", 10)
                    .setName(Component.literal("Gamma Multiplier"))
                    .setTooltip(Component.literal("Gamma will be multiplied on this value. Seting to 1 makes no difference, while 2 is recommended value."))
                    .setRange(0, 40, 1)
                    .setValueFormatter(FORMATTER_10)))
            .addPage(builder.createOptionPage()
                .setName(Component.literal("Effects"))
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Fog"))
                    .addOption(config.disableOverworldFog.buildOption(builder, config, "disable_overworld_fog", false)
                        .setName(Component.literal("Disable Overworld Fog"))
                        .setTooltip(Component.literal("Controls overworld fog.")))
                    .addOption(config.disableNetherFog.buildOption(builder, config, "disable_nether_fog", false)
                        .setName(Component.literal("Disable Nether Fog"))
                        .setTooltip(Component.literal("Controls Nether fog.")))
                    .addOption(config.disableTheEndFog.buildOption(builder, config, "disable_the_end_fog", false)
                        .setName(Component.literal("Disable The End Fog"))
                        .setTooltip(Component.literal("Controls The End fog.")))
                    .addOption(config.disableWaterFog.buildOption(builder, config, "disable_water_fog", false)
                        .setName(Component.literal("Disable Water Fog"))
                        .setTooltip(Component.literal("Controls water fog."))))
                .addOptionGroup(builder.createOptionGroup()
                    .setName(Component.literal("Fire Overlay"))
                    .addOption(config.fireTransparency.buildOption(builder, config, "fire_transparency", 20)
                        .setName(Component.literal("Transparency"))
                        .setTooltip(Component.literal("Controls transparency of fire when burning. 0.25 is recommended."))
                        .setRange(0, 20, 1)
                        .setValueFormatter(FORMATTER_20))
                    .addOption(config.fireOffset.buildOption(builder, config, "fire_offset", 0)
                        .setName(Component.literal("Offset"))
                        .setTooltip(Component.literal("Controls offset of fire when burning. 0.1 is recommended."))
                        .setRange(0, 10, 1)
                        .setValueFormatter(FORMATTER_20))))
            .addPage(builder.createOptionPage()
                .setName(Component.literal("HUD"))
                .addOption(config.enablePermanentHud.buildOption(builder, config, "enable_permanent_hud", false)
                    .setName(Component.literal("Enable Permanent HUD"))
                    .setTooltip(Component.literal("Controls permanent HUD.")))
                .addOption(config.hudFormat.buildOption(builder, config, "hud_format", "{x} {y} {z} {dir}")
                    .setName(Component.literal("HUD Format"))
                    .setTooltip(Component.literal("Set format for permanent HUD.")))
                .addOption(config.forceReducedInfo.buildOption(builder, config, "force_reduced_info", false)
                    .setName(Component.literal("Force Reduced Info"))
                    .setTooltip(Component.literal("Hides coordinates in permanent HUD and debug overlay. Can be binded in key bindings menu."))))
            .addPage(builder.createOptionPage()
                .setName(Component.literal("Utilities"))
                .addOption(config.showContainerTooltip.buildOption(builder, config, "show_container_tooltip", false)
                    .setName(Component.literal("Show Container Tooltip"))
                    .setTooltip(Component.literal("Show better tooltip for containers like shulker boxes."))));

        config.flush();
    }
}
