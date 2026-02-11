package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.LightTexture;
import overgreen.OverGreen;

@Mixin(LightTexture.class)
abstract class LightTextureMixin {
    @Redirect(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F"))
    private float multiplyGamma(Double value) {
        return value.floatValue() * OverGreen.getConfig().getGammaMultiplier();
    }
}
