package dev.localcapes.mixin;

import dev.localcapes.LocalCapesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
abstract class AbstractClientPlayerMixin {
    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    private void customcapemod$applyCape(CallbackInfoReturnable<PlayerSkin> cir) {
        if (Minecraft.getInstance().player == (Object) this) {
            cir.setReturnValue(LocalCapesClient.applyCape(cir.getReturnValue()));
        }
    }
}
