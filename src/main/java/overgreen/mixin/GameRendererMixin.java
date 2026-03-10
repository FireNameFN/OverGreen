package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import overgreen.OverGreen;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), allow = 1)
    private void tickZoom(DeltaTracker delta, boolean advance, CallbackInfo callback) {
        OverGreen.tickZoom(delta.getGameTimeDeltaTicks());
    }

    @ModifyReturnValue(method = "getFov", at = @At(value = "RETURN", ordinal = 1), allow = 1)
    private float performZoom(float fov) {
        return fov * (float)OverGreen.getDynamicZoom();
    }

    @ModifyExpressionValue(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/ClientAvatarState;getInterpolatedBob(F)F"), allow = 1)
    private float reduceBobView(float bob) {
        return bob * (float)OverGreen.getDynamicZoom();
    }
}
