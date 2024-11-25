package org.mangorage.ddgo.core.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.mangorage.ddgo.core.misc.ComponentObject;
import org.mangorage.ddgo.core.misc.Constants;
import org.mangorage.ddgo.core.tab.TabDisplayParameter;

public final class CreativeModeTabTypes {
    public static final ResourceKey<Registry<MapCodec<? extends CreativeModeTab>>> KEY = Constants.createRegistryKey("creative_mode_types");

    public static final Registry<MapCodec<? extends CreativeModeTab>> TYPES = FabricRegistryBuilder
            .createDefaulted(KEY, Constants.create("default"))
            .attribute(RegistryAttribute.MODDED)
            .buildAndRegister();


    public static final MapCodec<CreativeModeTab> TAB = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    ItemStack.CODEC.fieldOf("icon").forGetter(a -> null),
                    TabDisplayParameter.CODEC.listOf().fieldOf("items").forGetter(a -> null),
                    ComponentObject.CODEC.fieldOf("title").forGetter(a -> null)
            ).apply(i, (icon, items, title) -> {
                return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                        .icon(() -> icon)
                        .title(title.getComponent())
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
                Constants.create("default"),
                TAB
        );
    }
}
