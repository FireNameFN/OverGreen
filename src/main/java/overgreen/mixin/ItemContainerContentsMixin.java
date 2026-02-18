package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.item.component.ItemContainerContents;
import overgreen.OverGreen;

@Mixin(ItemContainerContents.class)
abstract class ItemContainerContentsMixin {
    @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true, allow = 1)
    public void replaceTooltip(CallbackInfo callback) {
        if(OverGreen.getConfig().getShowContainerTooltip())
            callback.cancel();
    }
}
