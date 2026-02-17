package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.ScreenEffectRenderer;
import overgreen.OverGreen;

@Mixin(ScreenEffectRenderer.class)
abstract class ScreenEffectRendererMixin {
    @ModifyArg(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"), index = 1, allow = 1)
    private static float offsetFire(float value) {
        return value - OverGreen.getConfig().getFireOffset();
    }

    @ModifyArg(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), index = 3, require = 4, allow = 4)
    private static float transparentFire(float value) {
        return value * OverGreen.getConfig().getFireTransparency();
    }
}
