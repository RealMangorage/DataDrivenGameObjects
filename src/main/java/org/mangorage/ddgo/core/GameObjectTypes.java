package org.mangorage.ddgo.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.mangorage.ddgo.core.misc.Constants;
import org.mangorage.ddgo.core.types.CatVariantTypes;
import org.mangorage.ddgo.core.types.CreativeModeTabTypes;
import org.mangorage.ddgo.core.types.ItemTypes;
import org.mangorage.ddgo.core.types.FrogVariantTypes;

import java.util.Optional;

public final class GameObjectTypes {
    public static final ResourceKey<Registry<GameObjectType<?, ?>>> KEY = Constants.createRegistryKey("game_object_types");

    public static final Registry<GameObjectType<?, ?>> GAME_OBJECT_TYPES = FabricRegistryBuilder
            .createSimple(KEY)
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();


    public record PostData(
            String id,
            Optional<BlockEntityType<?>> blockEntityType
    ) {
        public static final Codec<PostData> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                        PrimitiveCodec.STRING.fieldOf("id").forGetter(PostData::id),
                        BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec().optionalFieldOf("blockEntityType").forGetter(PostData::blockEntityType)
                ).apply(i, PostData::new)
        );
    }

    public static void bootstrap() {
        // Example of post-processing
        register(BuiltInRegistries.BLOCK_TYPE, BuiltInRegistries.BLOCK, false, PostData.CODEC, (id, b, d) -> {
            Registry.register(
                    BuiltInRegistries.ITEM,
                    id,
                    new BlockItem(b,
                            new Item.Properties()
                                    .setId(ResourceKey.create(Registries.ITEM, id))
                    )
            );
            d.blockEntityType().ifPresent(type -> {
                type.addSupportedBlock(b);
            });
        });

        // Normal, nothing occurs after registration here...
        register(ItemTypes.TYPES, BuiltInRegistries.ITEM, false);
        register(CreativeModeTabTypes.TYPES, BuiltInRegistries.CREATIVE_MODE_TAB, false);
        register(CatVariantTypes.TYPES, BuiltInRegistries.CAT_VARIANT, true);
        register(FrogVariantTypes.TYPES, BuiltInRegistries.FROG_VARIANT, true);
    }


    private static <T, P> void register(Registry<MapCodec<? extends T>> registryType, Registry<T> registry, boolean key, Codec<P> postCodec, IPostRegistration<ResourceLocation, T, P> postRegistration) {
        Registry.register(
                GAME_OBJECT_TYPES,
                Constants.create(registry.key().location().getPath()),
                new GameObjectType<>(registryType, registry, key, postCodec, postRegistration)
        );
    }

    private static <T> void register(Registry<MapCodec<? extends T>> registryType, Registry<T> registry, boolean key) {
        Registry.register(
                GAME_OBJECT_TYPES,
                Constants.create(registry.key().location().getPath()),
                new GameObjectType<>(registryType, registry, key)
        );
    }

    public static void prepareAll() {
        GAME_OBJECT_TYPES.stream().forEach(GameObjectType::prepare);
    }

    public static void registerAll() {
        GAME_OBJECT_TYPES.stream().forEach(GameObjectType::registerAll);
    }
}
