package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import overgreen.ClientContainerTooltip;
import overgreen.ContainerTooltip;

@Mixin(ClientTooltipComponent.class)
interface ClientTooltipComponentMixin {
    @Inject(method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;", at = @At("HEAD"), cancellable = true)
    private static void createClientContainerTooltip(TooltipComponent component, CallbackInfoReturnable<ClientTooltipComponent> callback) {
        if(component instanceof ContainerTooltip containerTooltip)
            callback.setReturnValue(new ClientContainerTooltip(containerTooltip.contents));
    }
}
