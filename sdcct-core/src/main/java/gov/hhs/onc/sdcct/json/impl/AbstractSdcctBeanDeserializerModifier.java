package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import java.util.List;

public abstract class AbstractSdcctBeanDeserializerModifier extends BeanDeserializerModifier {
    @Override
    public List<BeanPropertyDefinition> updateProperties(DeserializationConfig config, BeanDescription desc, List<BeanPropertyDefinition> propDefs) {
        return (this.canModify(desc) ? this.updatePropertiesInternal(config, desc, propDefs) : super.updateProperties(config, desc, propDefs));
    }

    @Override
    public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BeanDescription desc, BeanDeserializerBuilder deserializerBuilder) {
        return (this.canModify(desc) ? this.updateBuilderInternal(config, desc, deserializerBuilder) : super.updateBuilder(config, desc, deserializerBuilder));
    }

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription desc, JsonDeserializer<?> deserializer) {
        return (this.canModify(desc) ? this.modifyDeserializerInternal(config, desc, deserializer) : super.modifyDeserializer(config, desc, deserializer));
    }

    protected List<BeanPropertyDefinition> updatePropertiesInternal(DeserializationConfig config, BeanDescription desc, List<BeanPropertyDefinition> propDefs) {
        return super.updateProperties(config, desc, propDefs);
    }

    protected BeanDeserializerBuilder updateBuilderInternal(DeserializationConfig config, BeanDescription desc, BeanDeserializerBuilder deserializerBuilder) {
        return super.updateBuilder(config, desc, deserializerBuilder);
    }

    protected JsonDeserializer<?> modifyDeserializerInternal(DeserializationConfig config, BeanDescription desc, JsonDeserializer<?> deserializer) {
        return super.modifyDeserializer(config, desc, deserializer);
    }

    protected abstract boolean canModify(BeanDescription desc);
}
