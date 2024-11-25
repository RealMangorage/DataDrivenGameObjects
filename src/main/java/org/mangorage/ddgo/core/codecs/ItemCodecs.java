package org.mangorage.ddgo.core.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.Optional;

public final class ItemCodecs {
    public static final Codec<Item.Properties> PROPERTIES_CODEC = RecordCodecBuilder.create(
            i -> i.group(
                    ResourceKey.codec(Registries.ITEM).fieldOf("id").forGetter(a -> null),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("stacksTo").forGetter(a -> Optional.empty())
            ).apply(i, (id, stacksTo) -> {
                var properties = new Item.Properties();
                properties.setId(id);
                stacksTo.ifPresent(properties::stacksTo);
                return properties;
            })
    );

    public static final MapCodec<BlockItem> BLOCK_ITEM = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    ResourceLocation.CODEC.fieldOf("block").forGetter(a -> ResourceLocation.fromNamespaceAndPath("minecraft", "stone")),
                    PROPERTIES_CODEC.fieldOf("properties").forGetter(a -> new Item.Properties())
            ).apply(i, (block, properties) -> new BlockItem(BuiltInRegistries.BLOCK.getValue(block), properties))
    );
}
