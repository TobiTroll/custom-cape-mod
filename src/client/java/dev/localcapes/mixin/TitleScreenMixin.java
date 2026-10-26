package dev.localcapes.mixin;

import dev.localcapes.LocalCapesClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
abstract class TitleScreenMixin {
    @Shadow protected abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget);

    @Inject(method = "init", at = @At("TAIL"))
    private void customcapemod$addButton(CallbackInfo ci) {
        TitleScreen screen = (TitleScreen) (Object) this;
        addRenderableWidget(Button.builder(Component.literal("Custom Cape Mod"), button -> LocalCapesClient.openScreen())
                .bounds(screen.width / 2 - 100, screen.height - 44, 200, 20).build());
    }
}
