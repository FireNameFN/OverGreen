package overgreen;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

final class OverGreenKeyHandler {
    private static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, "category"));

    private static final KeyMapping TOGGLE_FORCE_REDUCED_INFO_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.overgreen.toggle_force_reduced_info", GLFW.GLFW_KEY_EQUAL, CATEGORY));

    private static final Component CHAT_PREFIX = Component.translatable("overgreen.chat.prefix").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD);
    private static final Component CHAT_WARNING = Component.translatable("overgreen.chat.toggle_force_reduced_info.warning").withStyle(ChatFormatting.RED);
    private static final Component CHAT_FORCED = Component.translatable("overgreen.chat.toggle_force_reduced_info.forced").withStyle(ChatFormatting.RED);
    private static final Component CHAT_NOT_FORCED = Component.translatable("overgreen.chat.toggle_force_reduced_info.not_forced").withStyle(ChatFormatting.GREEN);

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
            if(toggleForceReducedInfoKeyPressedTicks == 0)
                addMessage(minecraft, CHAT_WARNING);

            if(++toggleForceReducedInfoKeyPressedTicks < 40)
                return;
        }

        toggleForceReducedInfoKeyPressedTicks = -1;

        boolean force = !config.forceReducedInfo.getValue();

        config.forceReducedInfo.setValue(force);

        config.flush();

        addMessage(minecraft, Component.translatable("overgreen.chat.toggle_force_reduced_info.message", force ? CHAT_FORCED : CHAT_NOT_FORCED));
    }

    static private void addMessage(Minecraft minecraft, Component component) {
        minecraft.gui.getChat().addMessage(Component.empty()
            .append(CHAT_PREFIX)
            .append(component));
    }
}
