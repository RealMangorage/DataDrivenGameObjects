package org.mangorage.datagenblocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.mangorage.datagenblocks.core.types.CreativeModTabTypes;
import org.mangorage.datagenblocks.core.types.ItemTypes;
import org.mangorage.datagenblocks.core.GameObjectTypes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatagenBlocks implements ModInitializer {

    public DatagenBlocks() {
        // Bootstrap the Registries first!
        GameObjectTypes.bootstrap();
        ItemTypes.bootstrap();
        CreativeModTabTypes.bootstrap();

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

            MapCodec<CreativeModeTab> TAB = RecordCodecBuilder.mapCodec(
                    i -> i.group(
                            ItemStack.CODEC.fieldOf("item").forGetter(a -> null)
                    ).apply(i, a -> {
                        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                                .icon(() -> a)
                                .displayItems(new CreativeModeTab.DisplayItemsGenerator() {
                                    @Override
                                    public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
                                        output.accept(a);
                                    }
                                })
                                .build();
                    })
            );

            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)

                    .build();
        }
    }

    @Override
    public void onInitialize() {

    }
}
