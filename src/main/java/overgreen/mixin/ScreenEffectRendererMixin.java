package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.util.ARGB;
import overgreen.OverGreen;
import overgreen.OverGreenConfig;

@Mixin(ScreenEffectRenderer.class)
abstract class ScreenEffectRendererMixin {
    @ModifyArgs(method = "buildFireQuad", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;buildSpriteQuad(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Matrix4f;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;FFFFFI)V"))
    private static void modifyFire(Args args) {
        OverGreenConfig config = OverGreen.getConfig();

        float y1 = (float)args.get(4);
        float y2 = (float)args.get(6);

        float offset = config.getFireOffset();

        args.set(4, y1 - offset);
        args.set(6, y2 - offset);

        int color = (int)args.get(8);

        float alpha = ARGB.alphaFloat(color) * config.getFireTransparency();

        args.set(8, ARGB.color(alpha, color));
    }
}
