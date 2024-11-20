package org.mangorage.datagenblocks.core.types;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.mangorage.datagenblocks.core.codecs.ItemCodecs;
import org.mangorage.datagenblocks.core.misc.Constants;

public final class ItemTypes {
    public static final ResourceKey<Registry<MapCodec<? extends Item>>> KEY = Constants.createRegistryKey("item_types");

    public static final Registry<MapCodec<? extends Item>> TYPES = FabricRegistryBuilder
            .createSimple(KEY)
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();

    public static void bootstrap() {
        Registry.register(
                TYPES,
                Constants.create("blockitem"),
                ItemCodecs.BLOCK_ITEM
        );
    }
}
