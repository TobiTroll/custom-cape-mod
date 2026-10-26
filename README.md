# Custom Cape Mod

A client-side Fabric mod for Minecraft Java Edition 26.3. It adds a **Custom Cape Mod** button to the title screen. Choose a vanilla-format 64 × 32 PNG, and the mod equips it on your player immediately.

## Install

Install Fabric Loader and Fabric API for Minecraft 26.3. Put `custom-cape-mod-1.0.0.jar` in `.minecraft/mods`.

## Use

From the title screen choose **Custom Cape Mod → Upload PNG** and select a 64 × 32 cape image. The mod stores it at `config/customcapemod/cape.png` and loads it automatically on the next launch. Uploading another PNG replaces the current cape.

The image stays on your computer. The cape is rendered only by your client and is not sent to a server or shown to other players.

## Build from source

Requires JDK 25. Run `./gradlew build` (Windows: `gradlew.bat build`). The mod jar is written to `build/libs/`.

## License

MIT
