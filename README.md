# Custom Cape Mod

A client-side Fabric mod for Minecraft Java Edition 26.3. Adds a **Custom Cape Mod** button to the title screen. Choose a vanilla-format 64 x 32 PNG and equip it immediately.

Install Fabric Loader and Fabric API for Minecraft 26.3. Select **Custom Cape Mod → Upload PNG** from the title screen. The image is saved to `config/customcapemod/cape.png` and restored on the next launch.

This mod stores the image locally and does not send it to a service or server; only your own client sees the cape.

Build with JDK 25 using `./gradlew build`. The mod jar is created in `build/libs/`.