package org.mangorage.ddgo.core.misc;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class Constants {
    public static final String MOD_ID = "ddgo";

    public static ResourceLocation create(String id) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public static <T> ResourceKey<T> createKey(Registry<T> registry, String id) {
        return ResourceKey.create(registry.key(), create(id));
    }

    public static <T> ResourceKey<Registry<T>> createRegistryKey(String id) {
        return ResourceKey.createRegistryKey(create(id));
    }
}
