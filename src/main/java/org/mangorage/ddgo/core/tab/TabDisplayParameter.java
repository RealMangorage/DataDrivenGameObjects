package org.mangorage.ddgo.core.tab;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record TabDisplayParameter(
        Optional<ResourceLocation> flag,
        boolean operator,
        ItemStack item
) {
    public static final Codec<TabDisplayParameter> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                    ResourceLocation.CODEC.optionalFieldOf("featureFlag").forGetter(TabDisplayParameter::flag),
                    PrimitiveCodec.BOOL.fieldOf("operator").orElse(false).forGetter(TabDisplayParameter::operator),
                    ItemStack.CODEC.fieldOf("item").forGetter(TabDisplayParameter::item)
            ).apply(i, TabDisplayParameter::new)
    );

    public void accept(CreativeModeTab.ItemDisplayParameters display, CreativeModeTab.Output output) {
        if (flag().isPresent()) {
            if (display.enabledFeatures().intersects(FeatureFlags.REGISTRY.fromNames(List.of(flag.get())))) {
                if (operator) {
                    if (display.hasPermissions())
                        output.accept(item);
                } else {
                    output.accept(item);
                }
            }
            System.out.println(flag());
        } else {
            if (operator) {
                if (display.hasPermissions())
                    output.accept(item);
            } else {
                output.accept(item);
            }
        }
    }
}
