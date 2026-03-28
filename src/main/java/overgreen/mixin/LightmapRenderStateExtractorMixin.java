package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import overgreen.OverGreen;

@Mixin(LightmapRenderStateExtractor.class)
abstract class LightmapRenderStateExtractorMixin {
    @ModifyExpressionValue(method = "extract", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 0), allow = 1)
    private float multiplyGamma(float gamma) {
        return gamma * OverGreen.getConfig().getGammaMultiplier();
    }
}
