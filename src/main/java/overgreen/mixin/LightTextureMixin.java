package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.renderer.LightTexture;
import overgreen.OverGreen;

@Mixin(LightTexture.class)
abstract class LightTextureMixin {
    @ModifyExpressionValue(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float multiplyGamma(float gamma) {
        return gamma * OverGreen.getConfig().getGammaMultiplier();
    }
}
