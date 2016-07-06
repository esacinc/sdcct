package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import gov.hhs.onc.sdcct.json.JsonEncodeOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public class JsonEncodeOptionsImpl extends AbstractJsonCodecOptions<JsonEncodeOptions> implements JsonEncodeOptions {
    private final static long serialVersionUID = 0L;

    private List<BeanSerializerModifier> beanSerializerModifiers = new ArrayList<>();

    public JsonEncodeOptionsImpl() {
        super();
    }

    @Override
    protected void mergeInternal(JsonEncodeOptions mergeOpts) {
        super.mergeInternal(mergeOpts);

        this.beanSerializerModifiers.addAll(mergeOpts.getBeanSerializerModifiers());
    }

    @Override
    protected JsonEncodeOptions cloneInternal() {
        return new JsonEncodeOptionsImpl();
    }

    @Override
    public JsonEncodeOptions addBeanSerializerModifiers(BeanSerializerModifier ... beanSerializerModifiers) {
        Stream.of(beanSerializerModifiers).forEach(this.beanSerializerModifiers::add);

        return this;
    }

    @Override
    public boolean hasBeanSerializerModifiers() {
        return !CollectionUtils.isEmpty(this.beanSerializerModifiers);
    }

    @Override
    public List<BeanSerializerModifier> getBeanSerializerModifiers() {
        return this.beanSerializerModifiers;
    }

    @Override
    public JsonEncodeOptions setBeanSerializerModifiers(@Nullable List<BeanSerializerModifier> beanSerializerModifiers) {
        this.beanSerializerModifiers.clear();

        if (!CollectionUtils.isEmpty(beanSerializerModifiers)) {
            this.beanSerializerModifiers.addAll(beanSerializerModifiers);
        }

        return this;
    }
}
