package overgreen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class ClientContainerTooltip implements ClientTooltipComponent {
    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, "textures/gui/container/tooltip/background.png");

    private static final int WIDTH = 18 * 9;

    private static final int HEIGHT = 18 * 3;

    private final NonNullList<ItemStack> contents;

    public ClientContainerTooltip(NonNullList<ItemStack> contents) {
        this.contents = contents;
    }

    @Override
    public int getWidth(Font font) {
        return WIDTH;
    }

    @Override
    public int getHeight(Font font) {
        return HEIGHT + 4;
    }

    @Override
	public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x, y, 0, 0, WIDTH, HEIGHT, 256, 64);

        x++;
        y++;

        for(int i = 0; i < contents.size(); i++) {
            ItemStack item = contents.get(i);

            if(item.isEmpty())
                continue;

            int slotX = x + (i % 9) * 18;
            int slotY = y + (i / 9) * 18;

            graphics.renderItem(item, slotX, slotY);
            graphics.renderItemDecorations(font, item, slotX, slotY);
        }
	}
}
