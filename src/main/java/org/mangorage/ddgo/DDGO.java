package org.mangorage.ddgo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.mangorage.ddgo.core.misc.Constants;
import org.mangorage.ddgo.core.types.CatVariantTypes;
import org.mangorage.ddgo.core.types.CreativeModeTabTypes;
import org.mangorage.ddgo.core.types.ItemTypes;
import org.mangorage.ddgo.core.GameObjectTypes;
import org.mangorage.ddgo.core.types.FrogVariantTypes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mangorage.ddgo.core.misc.Constants.MOD_ID;

public class DDGO implements ModInitializer {

    public DDGO() {
        // Bootstrap the Registries first!
        GameObjectTypes.bootstrap();
        ItemTypes.bootstrap();
        CreativeModeTabTypes.bootstrap();
        CatVariantTypes.bootstrap();
        FrogVariantTypes.bootstrap();
        
        Path directory = FabricLoader.getInstance().getConfigDir().getParent().resolve(MOD_ID).resolve("data");

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
        }
    }

    @Override
    public void onInitialize() {

    }
}
