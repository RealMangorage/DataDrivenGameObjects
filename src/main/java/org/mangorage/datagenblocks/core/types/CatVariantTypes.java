package org.mangorage.datagenblocks.core.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.CatVariant;
import org.mangorage.datagenblocks.core.misc.Constants;

public final class CatVariantTypes {
    public static final ResourceKey<Registry<MapCodec<? extends CatVariant>>> KEY = Constants.createRegistryKey("cat_variant");

    public static final Registry<MapCodec<? extends CatVariant>> TYPES = FabricRegistryBuilder
            .createDefaulted(KEY, Constants.create("default"))
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();

    public static final MapCodec<CatVariant> CAT = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    ResourceLocation.CODEC.fieldOf("texture").forGetter(CatVariant::texture)
            ).apply(i, CatVariant::new)
    );

    public static void bootstrap() {
        Registry.register(
                TYPES,
                Constants.create("default"),
                CAT
        );
    }
}
