package org.mangorage.datagenblocks.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.MapCodec;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class GameObjectType<T> {
    public static final Path DIRECTORY = FabricLoader.getInstance().getConfigDir().getParent().resolve("datagenblocks").resolve("data");
    public static final Gson GSON = new GsonBuilder().create();

    private final Map<ResourceLocation, GameObjectEntry> entries = new HashMap<>();

    private final Registry<MapCodec<? extends T>> typeRegistry;
    private final Registry<T> registry;
    private final String id;

    public GameObjectType(Registry<MapCodec<? extends T>> registryType, Registry<T> registry) {
        this.typeRegistry = registryType;
        this.registry = registry;
        this.id = registry.key().location().getPath();
    }

    void addEntry(ResourceLocation id, GameObjectEntry entry) {
        entries.put(id, entry.create(id));
    }

    public GameObjectEntry get(ResourceLocation id) {
        return entries.get(id);
    }

    Map<ResourceLocation, GameObjectEntry> getEntries() {
        return entries;
    }

    public String typeId() {
        return id;
    }

    public void registerAll() {
        getEntries().forEach((id, obj) -> {
            typeRegistry.get(obj.getType(this)).ifPresent(codecReference -> {
                Registry.register(
                        registry,
                        id,
                        codecReference.value().codec().decode(JavaOps.INSTANCE, obj.getData(this)).result().get().getFirst()
                );
            });
        });
    }

    public void prepare() {
        FabricLoader.getInstance().getAllMods().forEach(mod -> {
            var typeDir = DIRECTORY.resolve(mod.getMetadata().getId()).resolve("registry").resolve(typeId());
            if (Files.exists(typeDir)) {
                try (var list = Files.list(typeDir)) {
                    list.forEach(p -> {
                        try {
                            String fileName = p.toFile().getName();
                            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));

                            addEntry(
                                    ResourceLocation.fromNamespaceAndPath(mod.getMetadata().getId(), baseName),
                                    GSON.fromJson(
                                            Files.newBufferedReader(p),
                                            GameObjectEntry.class
                                    )
                            );

                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}