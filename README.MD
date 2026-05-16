# DemoDisablerMod
this mod doesnt work yet
## Overview

DemoDisablerMod is a Minecraft Forge mod designed to disable the limitations imposed by the Minecraft demo mode. Once activated, it allows players to access multiplayer features, chat, and removes all demo restrictions directly from the main menu.

This mod is **physical client-only**; it is designed to run on the game instance the user interacts with and should not be loaded on a dedicated server.

## Requirements

- **Minecraft Forge 1.20.1**: The mod is specifically built and tested for Minecraft version 1.20.1 and Forge version **47.4.10**.
- **Java 17**: Unlike newer versions of Forge that require Java 21, Minecraft 1.20.1 is officially supported using the **Java 17 Development Kit (JDK)** and a 64-bit JVM.

## Installation

1. Ensure you have Minecraft 1.20.1 installed.
2. Install **Minecraft Forge 47.4.10**. The Forge installer can be downloaded from [the official Forge website](http://files.minecraftforge.net/).
3. Download the `demodisablermod-1.0.0.jar` file.
4. Place the downloaded `.jar` file into the `mods` folder in your Minecraft directory:
   - **Windows**: `%appdata%\.minecraft\mods`
   - **Mac/Linux**: `~/.minecraft/mods`
5. Run Minecraft through the Forge profile.

## Building from Source

To build the mod from the source, follow these steps:

1. Clone the repository to your local machine.
2. Ensure your terminal is set to use **Java 17**.
3. Navigate to the cloned directory and run the following Gradle command:

   ```bash
   ./gradlew clean build
   ```

The build output JAR will be located in the **`build/libs`** folder. 

*Note: This project no longer uses Access Transformers; all field access is now handled via Java Reflection, so no `accesstransformer.cfg` is required for the build.*

## Usage

Once installed, the mod automatically integrates with Minecraft. Start your game, and the demo mode restrictions will be disabled when you access the main menu. No additional steps or configurations are required post-installation.

## License

This project is available under the **MIT License**. See the LICENSE file for more details.
