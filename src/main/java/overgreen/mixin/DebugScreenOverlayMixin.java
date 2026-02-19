package overgreen.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import overgreen.OverGreen;

@Mixin(DebugScreenOverlay.class)
abstract class DebugScreenOverlayMixin {
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z", ordinal = 0))
    private boolean isEmptyPermanentHud(boolean isEmpty) {
        return isEmpty && !OverGreen.isPermanentHudVisible();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;getLevel()Lnet/minecraft/world/level/Level;"), allow = 1)
    private void displayPermanentHud(CallbackInfo callback, @Local(ordinal = 0) List<String> list) {
        if(OverGreen.isPermanentHudVisible())
            OverGreen.formatHud(list);
    }
}
