package dev.localcapes;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import com.mojang.blaze3d.platform.NativeImage;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class LocalCapesClient implements ClientModInitializer {
    private static final Identifier CAPE_ID = Identifier.fromNamespaceAndPath("customcapemod", "custom_cape");
    private static ClientAsset.ResourceTexture capeAsset;
    private static String status = "Choose a 64 x 32 PNG cape texture.";

    @Override public void onInitializeClient() {
        Minecraft.getInstance().execute(LocalCapesClient::loadSavedCape);
    }

    public static void openScreen() { Minecraft.getInstance().setScreenAndShow(new CapeScreen()); }
    public static PlayerSkin applyCape(PlayerSkin skin) {
        if (capeAsset == null) return skin;
        return skin.with(new PlayerSkin.Patch(Optional.empty(), Optional.of(capeAsset), Optional.empty(), Optional.empty()));
    }

    private static Path capeFile() { return Minecraft.getInstance().gameDirectory.toPath().resolve("config/customcapemod/cape.png"); }
    private static void loadSavedCape() { Path file = capeFile(); if (Files.isRegularFile(file)) loadCape(file, false); }

    private static void chooseCape() {
        Thread picker = new Thread(() -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select a cape PNG (64 x 32 pixels)");
            chooser.setFileFilter(new FileNameExtensionFilter("PNG image", "png"));
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                Path selected = chooser.getSelectedFile().toPath();
                Minecraft.getInstance().execute(() -> loadCape(selected, true));
            }
        }, "custom-cape-file-picker");
        picker.setDaemon(true);
        picker.start();
    }

    private static void loadCape(Path source, boolean copyToConfig) {
        try (InputStream input = Files.newInputStream(source)) {
            NativeImage image = NativeImage.read(input);
            if (image.getWidth() != 64 || image.getHeight() != 32) {
                image.close(); status = "Cape must be exactly 64 x 32 pixels.";
                Minecraft.getInstance().setScreenAndShow(new CapeScreen()); return;
            }
            Minecraft.getInstance().getTextureManager().register(CAPE_ID, new DynamicTexture(() -> "Custom Cape", image));
            capeAsset = new ClientAsset.ResourceTexture(CAPE_ID);
            if (copyToConfig) {
                Path destination = capeFile(); Files.createDirectories(destination.getParent());
                Files.copy(source, destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            status = "Cape equipped on this client.";
        } catch (Exception exception) { status = "Could not load the cape PNG."; }
        Minecraft.getInstance().setScreenAndShow(new CapeScreen());
    }

    private static final class CapeScreen extends net.minecraft.client.gui.screens.Screen {
        private CapeScreen() { super(net.minecraft.network.chat.Component.literal("Custom Cape Mod")); }
        @Override protected void init() {
            int center = width / 2, top = height / 2;
            addRenderableWidget(net.minecraft.client.gui.components.Button.builder(net.minecraft.network.chat.Component.literal("Upload PNG"), b -> chooseCape()).bounds(center - 100, top - 4, 200, 20).build());
            addRenderableWidget(net.minecraft.client.gui.components.Button.builder(net.minecraft.network.chat.Component.literal("Done"), b -> onClose()).bounds(center - 100, top + 24, 200, 20).build());
        }
        @Override public void extractRenderState(net.minecraft.client.gui.GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
            super.extractRenderState(graphics, mouseX, mouseY, delta);
            graphics.centeredText(font, title, width / 2, height / 2 - 42, 0xFFFFFF);
            graphics.centeredText(font, status, width / 2, height / 2 - 25, 0xCCCCCC);
        }
    }
}


