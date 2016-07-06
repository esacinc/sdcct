package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public class SdcctModule extends SimpleModule {
    private final static long serialVersionUID = 0L;

    private List<BeanDeserializerModifier> beanDeserializerModifiers;
    private List<BeanSerializerModifier> beanSerializerModifiers;

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        if (!CollectionUtils.isEmpty(this.beanDeserializerModifiers)) {
            this.beanDeserializerModifiers.forEach(context::addBeanDeserializerModifier);
        }

        if (!CollectionUtils.isEmpty(this.beanSerializerModifiers)) {
            this.beanSerializerModifiers.forEach(context::addBeanSerializerModifier);
        }
    }

    public void setBeanDeserializerModifiers(List<BeanDeserializerModifier> beanDeserializerModifiers) {
        this.beanDeserializerModifiers = beanDeserializerModifiers;
    }

    public void setBeanSerializerModifiers(List<BeanSerializerModifier> beanSerializerModifiers) {
        this.beanSerializerModifiers = beanSerializerModifiers;
    }

    public void setDeserializerItems(List<JsonDeserializer<?>> deserializers) {
        this.setDeserializers(new SimpleDeserializers(
            deserializers.stream().collect(SdcctStreamUtils.toMap(JsonDeserializer::handledType, Function.identity(), LinkedHashMap::new))));
    }

    public void setSerializerItems(List<JsonSerializer<?>> serializers) {
        this.setSerializers(new SimpleSerializers(serializers));
    }

    @Nullable
    @Override
    public Object getTypeId() {
        return null;
    }
}
