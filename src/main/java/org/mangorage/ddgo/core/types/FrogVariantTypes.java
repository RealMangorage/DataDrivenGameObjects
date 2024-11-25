package org.mangorage.ddgo.core.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.FrogVariant;
import org.mangorage.ddgo.core.misc.Constants;

public final class FrogVariantTypes {
    public static final ResourceKey<Registry<MapCodec<? extends FrogVariant>>> KEY = Constants.createRegistryKey("wolf_variant");

    public static final Registry<MapCodec<? extends FrogVariant>> TYPES = FabricRegistryBuilder
            .createDefaulted(KEY, Constants.create("default"))
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();


    public static final MapCodec<FrogVariant> DEFAULT_TYPE = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("texture").forGetter(FrogVariant::texture)
            ).apply(instance, FrogVariant::new)
    );

    public static void bootstrap() {
        Registry.register(
                TYPES,
                Constants.create("default"),
                DEFAULT_TYPE
        );
    }
}
