package org.mangorage.ddgo.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(BlockBehaviour.class)
public class BlockBehaviorMixin {
    @Unique
    private static final Codec<BlockBehaviour.Properties> PROPERTIES_CODEC = RecordCodecBuilder.create(
            i -> i.group(
                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("destroyTime").forGetter(a -> Optional.of(0f)),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("lightLevel").forGetter(a -> Optional.of(0)),
                    ResourceKey.codec(Registries.BLOCK).fieldOf("id").forGetter(a -> null)
            ).apply(i, (destroyTime, lightLevel, id) -> {
                var properties = BlockBehaviour.Properties.of();
                destroyTime.ifPresent(properties::destroyTime);
                lightLevel.ifPresent(l -> properties.lightLevel(state -> l));
                properties.setId(id);
                return properties;
            })
    );

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static <B extends Block> RecordCodecBuilder<B, BlockBehaviour.Properties> propertiesCodec() {
        return PROPERTIES_CODEC.fieldOf("properties").forGetter(a -> BlockBehaviour.Properties.of());
    }
}
