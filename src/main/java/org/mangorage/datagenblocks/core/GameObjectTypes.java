package org.mangorage.datagenblocks.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.mangorage.datagenblocks.core.types.CreativeModeTabTypes;
import org.mangorage.datagenblocks.core.types.ItemTypes;

import static org.mangorage.datagenblocks.core.misc.Constants.MOD_ID;

public final class GameObjectTypes {
    public static final ResourceKey<Registry<GameObjectType<?, ?>>> KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("datagenblocks", "game_object_types"));
    public static final Registry<GameObjectType<?, ?>> GAME_OBJECT_TYPES = FabricRegistryBuilder
            .createSimple(KEY)
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();


    public record PostData(
            String id
    ) {
        public static final Codec<PostData> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                        PrimitiveCodec.STRING.fieldOf("id").forGetter(PostData::id)
                ).apply(i, PostData::new)
        );
    }

    public static void bootstrap() {
        // Example of post-processing
        register(BuiltInRegistries.BLOCK_TYPE, BuiltInRegistries.BLOCK, PostData.CODEC, (id, b, d) -> {
            System.out.println("OUTPUT -> " + d.id());
            BlockRenderLayerMap.INSTANCE.putBlock(b, RenderType.cutout());
        });


        // Normal, nothing occurs after registration here...
        register(ItemTypes.TYPES, BuiltInRegistries.ITEM);
        register(CreativeModeTabTypes.TYPES, BuiltInRegistries.CREATIVE_MODE_TAB);
    }

    private static <T, P> void register(Registry<MapCodec<? extends T>> registryType, Registry<T> registry, Codec<P> postCodec, IPostRegistration<ResourceLocation, T, P> postRegistration) {
        Registry.register(
                GAME_OBJECT_TYPES,
                ResourceLocation.fromNamespaceAndPath(
                        MOD_ID,
                        registry.key().location().getPath()
                ),
                new GameObjectType<>(registryType, registry, postCodec, postRegistration)
        );
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
