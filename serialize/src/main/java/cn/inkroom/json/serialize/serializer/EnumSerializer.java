package cn.inkroom.json.serialize.serializer;

import cn.inkroom.json.core.annotation.JsonFeature;
import cn.inkroom.json.serialize.JsonSerializer;
import cn.inkroom.json.serialize.JsonWriter;
import cn.inkroom.json.serialize.SerializerProvider;
import cn.inkroom.json.serialize.exception.JsonSerializeException;

public class EnumSerializer implements JsonSerializer<Enum> {
    @Override
    public void serialize(Enum value, JsonWriter writer, SerializerProvider provider) throws JsonSerializeException {
        if (provider.getConfig().isEnable(JsonFeature.ENUM_ORDINAL)) {
            writer.writeLong(value.ordinal());
        } else {
            writer.writeString(value.name());
        }
    }
}
