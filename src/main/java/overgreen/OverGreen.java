package overgreen;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class OverGreen implements ClientModInitializer, ConfigEntryPoint {
	public static final String MOD_ID = "overgreen";

	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("overgreen.json");

	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static OverGreenConfig config;

	@Override
	public void onInitializeClient() {
		loadConfig();
	}

	private static void loadConfig() {
		if(Files.exists(CONFIG_PATH)) {
			try {
				Reader reader = Files.newBufferedReader(CONFIG_PATH);

				config = GSON.fromJson(reader, OverGreenConfig.class);

				return;
			} catch(IOException | JsonSyntaxException e) {
				LOGGER.error("Failed to load config", e);
			}
		}

		config = new OverGreenConfig();

		config.dirty();
	}

	static void saveConfig() {
		try {
			Writer writer = Files.newBufferedWriter(CONFIG_PATH);

			GSON.toJson(config, writer);

			writer.close();
		} catch(IOException e) {
			LOGGER.error("Failed to save config", e);
		}
	}

	@Override
	public void registerConfigLate(ConfigBuilder builder) {
		final RationalValueFormatter FORMATTER_10 = new RationalValueFormatter(0.1, "0.0");

		final RationalValueFormatter FORMATTER_20 = new RationalValueFormatter(0.05, "0.00");

		builder.registerOwnModOptions()
			.setIcon(Identifier.fromNamespaceAndPath(MOD_ID, "icon_mono.png"))
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
						.setValueFormatter(FORMATTER_20))));

		config.flush();
	}

	public static OverGreenConfig getConfig() {
		return config;
	}
}
