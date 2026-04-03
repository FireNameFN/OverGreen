package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.MouseHandler;
import overgreen.OverGreen;

@Mixin(MouseHandler.class)
abstract class MouseHandlerMixin {
    @Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z"), cancellable = true, allow = 1)
    private void handleScroll(CallbackInfo callback, @Local int value) {
        if(!OverGreen.shouldHandleScroll())
            return;

        OverGreen.stepZoom(value);

        callback.cancel();
    }

    @ModifyArgs(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), allow = 1)
    private void decreaseSensitivity(Args args) {
        double modifier = OverGreen.getSensitivityReduction();

        args.set(0, (double)args.get(0) * modifier);
        args.set(1, (double)args.get(1) * modifier);
    }
}
