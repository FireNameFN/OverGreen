package overgreen;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.fabricmc.api.ClientModInitializer;

public final class OverGreenInitializer implements ClientModInitializer, ConfigEntryPoint {
    @Override
    public void onInitializeClient() {
        OverGreenConfigManager.loadConfig();

        OverGreenKeyHandler.initialize();
    }

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        OverGreenConfigManager.registerConfig(builder);
    }
}
