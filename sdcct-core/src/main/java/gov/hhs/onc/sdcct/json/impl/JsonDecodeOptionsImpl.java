package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import gov.hhs.onc.sdcct.json.JsonDecodeOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public class JsonDecodeOptionsImpl extends AbstractJsonCodecOptions<JsonDecodeOptions> implements JsonDecodeOptions {
    private final static long serialVersionUID = 0L;

    private List<BeanDeserializerModifier> beanDeserializerModifiers = new ArrayList<>();

    public JsonDecodeOptionsImpl() {
        super();
    }

    @Override
    protected void mergeInternal(JsonDecodeOptions mergeOpts) {
        super.mergeInternal(mergeOpts);

        this.beanDeserializerModifiers.addAll(mergeOpts.getBeanDeserializerModifiers());
    }

    @Override
    protected JsonDecodeOptions cloneInternal() {
        return new JsonDecodeOptionsImpl();
    }

    @Override
    public JsonDecodeOptions addBeanDeserializerModifiers(BeanDeserializerModifier ... beanDeserializerModifiers) {
        Stream.of(beanDeserializerModifiers).forEach(this.beanDeserializerModifiers::add);

        return this;
    }

    @Override
    public boolean hasBeanDeserializerModifiers() {
        return !CollectionUtils.isEmpty(this.beanDeserializerModifiers);
    }

    @Override
    public List<BeanDeserializerModifier> getBeanDeserializerModifiers() {
        return this.beanDeserializerModifiers;
    }

    @Override
    public JsonDecodeOptions setBeanDeserializerModifiers(@Nullable List<BeanDeserializerModifier> beanDeserializerModifiers) {
        this.beanDeserializerModifiers.clear();

        if (!CollectionUtils.isEmpty(beanDeserializerModifiers)) {
            this.beanDeserializerModifiers.addAll(beanDeserializerModifiers);
        }

        return this;
    }
}
