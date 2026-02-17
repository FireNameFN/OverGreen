package overgreen;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

final class OverGreenKeyHandler {
    private static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, "category"));

    private static final KeyMapping TOGGLE_FORCE_REDUCED_INFO_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.overgreen.toggle_force_reduced_info", GLFW.GLFW_KEY_EQUAL, CATEGORY));

    private static int toggleForceReducedInfoKeyPressedTicks;

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(OverGreenKeyHandler::onEndClientTick);
    }

    private static void onEndClientTick(Minecraft minecraft) {
        if(!TOGGLE_FORCE_REDUCED_INFO_KEY.isDown()) {
            toggleForceReducedInfoKeyPressedTicks = 0;

            return;
        }

        if(toggleForceReducedInfoKeyPressedTicks < 0)
            return;

        OverGreenConfig config = OverGreen.getConfig();

        if(config.forceReducedInfo.getValue()) {
            if(toggleForceReducedInfoKeyPressedTicks == 0) {
                minecraft.gui.getChat().addMessage(Component.empty()
                    .append(Component.literal("[OverGreen]").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD))
                    .append(CommonComponents.SPACE)
                    .append(Component.literal("Disabling to force reduced info in 2 seconds...").withStyle(ChatFormatting.RED)));
            }

            if(++toggleForceReducedInfoKeyPressedTicks < 40)
                return;
        }

        toggleForceReducedInfoKeyPressedTicks = -1;

        boolean force = !config.forceReducedInfo.getValue();

        config.forceReducedInfo.setValue(force);

        config.flush();

        MutableComponent component = Component.empty()
            .append(Component.literal("[OverGreen]").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD))
            .append(CommonComponents.SPACE)
            .append(Component.literal("Reduced info is "));

        if(force)
            component.append(Component.literal("forced").withStyle(ChatFormatting.GREEN));
        else
            component.append(Component.literal("not forced").withStyle(ChatFormatting.RED));

        component.append(Component.literal("."));

        minecraft.gui.getChat().addMessage(component);
    }
}
