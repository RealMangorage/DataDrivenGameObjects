package org.mangorage.datagenblocks.core.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.mangorage.datagenblocks.core.tab.TabDisplayParameter;

import static org.mangorage.datagenblocks.core.misc.Constants.MOD_ID;

public final class CreativeModTabTypes {
    public static final ResourceKey<Registry<MapCodec<? extends CreativeModeTab>>> KEY = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath("datagenblocks", "creative_mode_types")
    );

    public static final Registry<MapCodec<? extends CreativeModeTab>> TYPES = FabricRegistryBuilder
            .createDefaulted(KEY, ResourceLocation.fromNamespaceAndPath(MOD_ID, "default"))
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();


    public static final MapCodec<CreativeModeTab> TAB = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    ItemStack.CODEC.fieldOf("icon").forGetter(a -> null),
                    TabDisplayParameter.CODEC.listOf().fieldOf("items").forGetter(a -> null),
                    ExtraCodecs.NON_EMPTY_STRING.fieldOf("title").forGetter(a -> null)
            ).apply(i, (icon, items, title) -> {
                return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                        .icon(() -> icon)
                        .title(Component.translatable(title))
                        .displayItems(new CreativeModeTab.DisplayItemsGenerator() {
                            @Override
                            public void accept(CreativeModeTab.ItemDisplayParameters display, CreativeModeTab.Output output) {
                                items.forEach(i -> i.accept(display, output));
                            }
                        })
                        .build();
            })
    );

    public static void bootstrap() {
        Registry.register(
                TYPES,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "default"),
                TAB
        );
    }
}
