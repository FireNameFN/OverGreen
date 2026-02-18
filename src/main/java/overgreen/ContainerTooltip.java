package overgreen;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public final class ContainerTooltip implements TooltipComponent {
    public final NonNullList<ItemStack> contents;

    public ContainerTooltip(NonNullList<ItemStack> contents) {
        this.contents = contents;
    }
}
