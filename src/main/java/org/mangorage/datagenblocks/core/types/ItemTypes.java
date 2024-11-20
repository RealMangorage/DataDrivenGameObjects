package org.mangorage.datagenblocks.core.types;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.mangorage.datagenblocks.core.codecs.ItemCodecs;

public final class ItemTypes {
    public static final ResourceKey<Registry<MapCodec<? extends Item>>> KEY = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath("datagenblocks", "item_types")
    );

    public static final Registry<MapCodec<? extends Item>> TYPES = FabricRegistryBuilder
            .createSimple(KEY)
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();

    public static void bootstrap() {
        Registry.register(
                TYPES,
                ResourceLocation.fromNamespaceAndPath("datagenblocks", "blockitem"),
                ItemCodecs.BLOCK_ITEM
        );
    }
}
