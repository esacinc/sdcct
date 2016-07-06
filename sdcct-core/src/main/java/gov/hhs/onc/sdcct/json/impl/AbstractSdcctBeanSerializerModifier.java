package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.util.List;

public abstract class AbstractSdcctBeanSerializerModifier extends BeanSerializerModifier {
    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription desc, List<BeanPropertyWriter> propWriters) {
        return (this.canModify(desc) ? this.changePropertiesInternal(config, desc, propWriters) : super.changeProperties(config, desc, propWriters));
    }

    @Override
    public List<BeanPropertyWriter> orderProperties(SerializationConfig config, BeanDescription desc, List<BeanPropertyWriter> propWriters) {
        return (this.canModify(desc) ? this.orderPropertiesInternal(config, desc, propWriters) : super.orderProperties(config, desc, propWriters));
    }

    @Override
    public BeanSerializerBuilder updateBuilder(SerializationConfig config, BeanDescription desc, BeanSerializerBuilder serializerBuilder) {
        return (this.canModify(desc) ? this.updateBuilderInternal(config, desc, serializerBuilder) : super.updateBuilder(config, desc, serializerBuilder));
    }

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription desc, JsonSerializer<?> serializer) {
        return (this.canModify(desc) ? this.modifySerializerInternal(config, desc, serializer) : super.modifySerializer(config, desc, serializer));
    }

    protected List<BeanPropertyWriter> changePropertiesInternal(SerializationConfig config, BeanDescription desc, List<BeanPropertyWriter> propWriters) {
        return super.changeProperties(config, desc, propWriters);
    }

    protected List<BeanPropertyWriter> orderPropertiesInternal(SerializationConfig config, BeanDescription desc, List<BeanPropertyWriter> propWriters) {
        return super.orderProperties(config, desc, propWriters);
    }

    protected BeanSerializerBuilder updateBuilderInternal(SerializationConfig config, BeanDescription desc, BeanSerializerBuilder serializerBuilder) {
        return super.updateBuilder(config, desc, serializerBuilder);
    }

    protected JsonSerializer<?> modifySerializerInternal(SerializationConfig config, BeanDescription desc, JsonSerializer<?> serializer) {
        return super.modifySerializer(config, desc, serializer);
    }

    protected abstract boolean canModify(BeanDescription desc);
}
