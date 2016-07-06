package gov.hhs.onc.sdcct.json;

import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import java.util.List;
import javax.annotation.Nullable;

public interface JsonDecodeOptions extends JsonCodecOptions<JsonDecodeOptions> {
    public JsonDecodeOptions addBeanDeserializerModifiers(BeanDeserializerModifier ... beanDeserializerModifiers);

    public boolean hasBeanDeserializerModifiers();

    public List<BeanDeserializerModifier> getBeanDeserializerModifiers();

    public JsonDecodeOptions setBeanDeserializerModifiers(@Nullable List<BeanDeserializerModifier> beanDeserializerModifiers);
}
