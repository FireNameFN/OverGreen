package overgreen.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.mixin.transfer.ItemContainerContentsAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import overgreen.OverGreen;
import overgreen.tooltip.ContainerTooltip;

@Mixin(Item.class)
abstract class ItemMixin {
    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true, allow = 1)
    private void getContainerTooltip(ItemStack item, CallbackInfoReturnable<Optional<TooltipComponent>> callback) {
        if(!OverGreen.getConfig().getShowContainerTooltip())
            return;

        ItemContainerContents container = item.get(DataComponents.CONTAINER);

        if(container == null)
            return;

        List<Optional<ItemStackTemplate>> contents = ((ItemContainerContentsAccessor)(Object)container).fabric_getItems();

        if(contents.isEmpty())
            return;

        callback.setReturnValue(Optional.of(new ContainerTooltip(contents)));
    }
}
