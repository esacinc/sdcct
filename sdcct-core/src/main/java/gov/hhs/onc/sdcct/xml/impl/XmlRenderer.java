package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.io.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.transform.render.RenderOptions;
import gov.hhs.onc.sdcct.transform.render.RenderType;
import gov.hhs.onc.sdcct.transform.render.impl.AbstractSdcctRenderer;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class XmlRenderer extends AbstractSdcctRenderer {
    @Resource(name = "jaxbMarshaller")
    private Jaxb2Marshaller jaxbMarshaller;

    @Resource(name = "jaxbMarshallerPretty")
    private Jaxb2Marshaller prettyJaxbMarshaller;

    public XmlRenderer() {
        super(RenderType.XML);
    }

    @Override
    protected byte[] renderInternal(Object obj, Map<String, Object> opts) throws Exception {
        ByteArrayResult result = new ByteArrayResult();

        try (ByteArrayOutputStream resultOutStream = result.getOutputStream()) {
            ((opts.containsKey(RenderOptions.PRETTY_NAME)
                ? BooleanUtils.toBooleanObject(opts.get(RenderOptions.PRETTY_NAME).toString().toLowerCase())
                : ((Boolean) this.defaultOpts.get(RenderOptions.PRETTY_NAME))) ? this.prettyJaxbMarshaller : this.jaxbMarshaller).marshal(obj, result);

            resultOutStream.flush();
        }

        return result.getBytes();
    }
}
