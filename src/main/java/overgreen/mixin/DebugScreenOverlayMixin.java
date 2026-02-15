package overgreen.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.world.entity.Entity;
import overgreen.OverGreen;

@Mixin(DebugScreenOverlay.class)
abstract class DebugScreenOverlayMixin {
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z"))
    private boolean isEmptyPermanentHud(boolean isEmpty) {
        return isEmpty && !shouldDisplayPermanentHud(Minecraft.getInstance());
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;getLevel()Lnet/minecraft/world/level/Level;"))
    private void displayPermanentHud(CallbackInfo callback, @Local(ordinal = 0) List<String> list) {
        Minecraft minecraft = Minecraft.getInstance();

        if(!shouldDisplayPermanentHud(minecraft))
            return;

        Entity entity = minecraft.getCameraEntity();

        if(entity == null)
            return;

        OverGreen.formatHud(list, entity);
    }

    private static final boolean shouldDisplayPermanentHud(Minecraft minecraft) {
        if(minecraft.showOnlyReducedInfo())
            return false;

        if(minecraft.debugEntries.isOverlayVisible())
            return false;

        return true;
    }
}
