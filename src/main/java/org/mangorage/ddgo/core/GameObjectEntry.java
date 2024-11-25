package org.mangorage.ddgo.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.resources.ResourceLocation;
import org.mangorage.ddgo.core.misc.TemplateType;
import org.mangorage.ddgo.core.misc.Util;
import java.util.Map;

public record GameObjectEntry(
        TemplateType templateType,

        @JsonAdapter(value = ResourceLocation.Serializer.class)
        ResourceLocation parent,

        @JsonAdapter(value = ResourceLocation.Serializer.class)
        @Expose(deserialize = false)
        ResourceLocation id,

        @JsonAdapter(value = ResourceLocation.Serializer.class)
        ResourceLocation type,

        Map<String, Object> data,
        Map<String, Object> extra
) {

    public GameObjectEntry create(ResourceLocation entry) {
        return new GameObjectEntry(
                templateType == null ? TemplateType.GAME_OBJECT : templateType,
                parent,
                entry,
                type,
                data == null ? Map.of() : data,
                extra == null ? Map.of() : extra
        );
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public GameObjectEntry getParent(GameObjectType<?, ?> gameObjType) {
        return gameObjType.get(parent);
    }

    public ResourceLocation getType(GameObjectType<?, ?> gameObjType) {
        return parent != null && type == null ? getParent(gameObjType).getType(gameObjType) : type;
    }

    public Map<String, Object> getData(GameObjectType<?, ?> gameObjType) {
        return parent == null ? data : Util.deepMerge(getParent(gameObjType).getData(gameObjType), data);
    }

    public Map<String, Object> getPostData(GameObjectType<?, ?> gameObjectType) {
        return parent == null ? extra : Util.deepMerge(getParent(gameObjectType).getPostData(gameObjectType), extra);
    }
}
