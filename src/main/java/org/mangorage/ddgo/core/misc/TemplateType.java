package org.mangorage.ddgo.core.misc;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum TemplateType implements StringRepresentable {
    TEMPLATE,
    GAME_OBJECT;

    public static final Codec<TemplateType> CODEC = StringRepresentable.fromValues(TemplateType::values);

    @Override
    public String getSerializedName() {
        return toString().toLowerCase();
    }
}
