package org.mangorage.datagenblocks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.effect.MobEffect;
import org.mangorage.datagenblocks.core.types.CatVariantTypes;
import org.mangorage.datagenblocks.core.types.CreativeModeTabTypes;
import org.mangorage.datagenblocks.core.types.ItemTypes;
import org.mangorage.datagenblocks.core.GameObjectTypes;
import org.mangorage.datagenblocks.core.types.FrogVariantTypes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatagenBlocks implements ModInitializer {

    public DatagenBlocks() {
        // Bootstrap the Registries first!
        GameObjectTypes.bootstrap();
        ItemTypes.bootstrap();
        CreativeModeTabTypes.bootstrap();
        CatVariantTypes.bootstrap();
        FrogVariantTypes.bootstrap();

        Path directory = FabricLoader.getInstance().getConfigDir().getParent().resolve("datagenblocks").resolve("data");

        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Prepare
            GameObjectTypes.prepareAll();

            // Then Register
            GameObjectTypes.registerAll();

            var a = 1;
        }
    }

    @Override
    public void onInitialize() {

    }
}
