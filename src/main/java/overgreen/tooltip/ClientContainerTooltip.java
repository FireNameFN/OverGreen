package overgreen.tooltip;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import overgreen.OverGreen;

public final class ClientContainerTooltip implements ClientTooltipComponent {
    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(OverGreen.MOD_ID, "textures/gui/container/tooltip/background.png");

    private static final int WIDTH = 18 * 9;

    private static final int HEIGHT = 18 * 3;

    private final List<Optional<ItemStackTemplate>> contents;

    public ClientContainerTooltip(List<Optional<ItemStackTemplate>> contents) {
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
	public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x, y, 0, 0, WIDTH, HEIGHT, 256, 64);

        x++;
        y++;

        for(int i = 0; i < contents.size(); i++) {
            Optional<ItemStackTemplate> itemTemplate = contents.get(i);

            if(itemTemplate.isEmpty())
                continue;

            ItemStack item = itemTemplate.get().create();

            int slotX = x + (i % 9) * 18;
            int slotY = y + (i / 9) * 18;

            graphics.item(item, slotX, slotY);
            graphics.itemDecorations(font, item, slotX, slotY);
        }
	}
}
