package overgreen.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.world.level.material.FogType;
import overgreen.OverGreen;
import overgreen.OverGreenConfig;

@Mixin(FogRenderer.class)
abstract class FogRendererMixin {
    @ModifyVariable(method = "setupFog", at = @At("STORE"))
    private FogType disableWaterFog(FogType type) {
        if(type == FogType.WATER && OverGreen.getConfig().getDisableWaterFog())
            return FogType.ATMOSPHERIC;

        return type;
    }

    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/client/Camera;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V"))
    private void disableAtmosphericFog(FogEnvironment environment, FogData fog, Camera camera, ClientLevel level, float distance, DeltaTracker delta, @Local(ordinal = 2) LocalFloatRef distanceRef) {
        environment.setupFog(fog, camera, level, distance, delta);

        if(!(environment instanceof AtmosphericFogEnvironment))
            return;

        OverGreenConfig config = OverGreen.getConfig();

        boolean disabled = switch(level.dimensionType().skybox()) {
            case OVERWORLD -> config.getDisableOverworldFog();
            case NONE -> config.getDisableNetherFog();
            case END -> config.getDisableTheEndFog();
        };

        if(!disabled)
            return;

        distance += 64;

        fog.environmentalStart = distance;
        fog.environmentalEnd = distance;

        distanceRef.set(distance);
    }
}
