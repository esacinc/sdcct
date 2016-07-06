package gov.hhs.onc.sdcct.json;

import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.util.List;
import javax.annotation.Nullable;

public interface JsonEncodeOptions extends JsonCodecOptions<JsonEncodeOptions> {
    public JsonEncodeOptions addBeanSerializerModifiers(BeanSerializerModifier ... beanSerializerModifiers);

    public boolean hasBeanSerializerModifiers();

    public List<BeanSerializerModifier> getBeanSerializerModifiers();

    public JsonEncodeOptions setBeanSerializerModifiers(@Nullable List<BeanSerializerModifier> beanSerializerModifiers);
}
