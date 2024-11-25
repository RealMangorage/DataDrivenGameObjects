package org.mangorage.ddgo.core.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;

public record ComponentObject(String text, boolean translate) {
    public static final Codec<ComponentObject> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                    PrimitiveCodec.STRING.fieldOf("text").forGetter(ComponentObject::text),
                    PrimitiveCodec.BOOL.fieldOf("translate").orElse(true).forGetter(ComponentObject::translate)
            ).apply(i, ComponentObject::new)
    );

    public Component getComponent() {
        return translate ? Component.translatable(text) : Component.literal(text);
    }
}
