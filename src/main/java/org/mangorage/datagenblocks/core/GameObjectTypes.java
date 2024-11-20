package org.mangorage.datagenblocks.core;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.mangorage.datagenblocks.core.types.CreativeModeTabTypes;
import org.mangorage.datagenblocks.core.types.ItemTypes;

import static org.mangorage.datagenblocks.core.misc.Constants.MOD_ID;

public final class GameObjectTypes {
    public static final ResourceKey<Registry<GameObjectType<?>>> KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("datagenblocks", "game_object_types"));
    public static final Registry<GameObjectType<?>> GAME_OBJECT_TYPES = FabricRegistryBuilder
            .createSimple(KEY)
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();


    public static void bootstrap() {
        register(BuiltInRegistries.BLOCK_TYPE, BuiltInRegistries.BLOCK);
        register(ItemTypes.TYPES, BuiltInRegistries.ITEM);
        register(CreativeModeTabTypes.TYPES, BuiltInRegistries.CREATIVE_MODE_TAB);
    }

    private static <T> void register(Registry<MapCodec<? extends T>> registryType, Registry<T> registry) {
        Registry.register(
                GAME_OBJECT_TYPES,
                ResourceLocation.fromNamespaceAndPath(
                        MOD_ID,
                        registry.key().location().getPath()
                ),
                new GameObjectType<>(registryType, registry)
        );
    }

    public static void prepareAll() {
        GAME_OBJECT_TYPES.stream().forEach(GameObjectType::prepare);
    }

    public static void registerAll() {
        GAME_OBJECT_TYPES.stream().forEach(GameObjectType::registerAll);
    }
}
