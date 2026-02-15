package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import overgreen.OverGreen;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
    @Inject(method = "showOnlyReducedInfo", at = @At(value = "HEAD"), cancellable = true)
    private void forceReducedInfo(CallbackInfoReturnable<Boolean> callback) {
        if(OverGreen.getConfig().getForceReducedInfo())
            callback.setReturnValue(true);
    }
}
