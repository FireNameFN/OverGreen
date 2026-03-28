package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import overgreen.OverGreen;

@Mixin(Camera.class)
abstract class CameraMixin {
    @Inject(method = "update", at = @At("HEAD"), allow = 1)
    private void updateZoom(DeltaTracker delta, CallbackInfo callback) {
        OverGreen.updateZoom(delta.getGameTimeDeltaTicks());
    }

    @ModifyReturnValue(method = "calculateFov", at = @At(value = "RETURN", ordinal = 1), allow = 1)
    private float performZoom(float fov) {
        return fov * (float)OverGreen.getDynamicZoom();
    }

    @ModifyReturnValue(method = "calculateHudFov", at = @At("RETURN"), allow = 1)
    private float performHudZoom(float fov) {
        return fov * (float)OverGreen.getDynamicZoom();
    }

    @ModifyExpressionValue(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/ClientAvatarState;getInterpolatedBob(F)F"), allow = 1)
    private float reduceBobView(float bob) {
        return bob * (float)OverGreen.getDynamicZoom();
    }
}
