package gov.hhs.onc.sdcct.rfd.ws.impl;

import com.ctc.wstx.sr.ValidatingStreamReader;
import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsException;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import gov.hhs.onc.sdcct.transform.location.SdcctLocator;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.validate.SdcctValidatorService;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlStreamReader;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec.UnmarshallingXmlStreamReader;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import gov.hhs.onc.sdcct.xml.validate.impl.CompositeXmlValidator;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.apache.cxf.databinding.DataReader;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.jaxb.io.DataReaderImpl;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.staxutils.DepthXMLStreamReader;
import org.codehaus.stax2.util.StreamReader2Delegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class RfdJaxbDataBinding extends JAXBDataBinding implements InitializingBean {
    private static class MessagePartXmlStreamReader extends StreamReader2Delegate {
        private SdcctLocator locator;
        private CompositeXmlValidator validator;
        private boolean firstEvent = true;
        private int depth;

        public MessagePartXmlStreamReader(ValidatingStreamReader delegate, SdcctLocator locator, CompositeXmlValidator validator) {
            super(delegate);

            this.locator = locator;
            this.validator = validator;
        }

        @Override
        public int next() throws XMLStreamException {
            int eventType;

            if (this.firstEvent) {
                this.firstEvent = false;

                this.locator.popElement();

                this.validator.validateElementStart(this.getLocalName(), this.getNamespaceURI(), this.getPrefix());

                eventType = this.getEventType();
            } else {
                eventType = super.next();
            }

            if (eventType == START_ELEMENT) {
                this.depth++;
            } else if (eventType == END_ELEMENT) {
                this.depth--;
            }

            return this.getEventType();
        }

        @Override
        public boolean hasNext() throws XMLStreamException {
            return (this.firstEvent || (this.depth > 0));
        }
    }

    private class RfdJaxbDataReader extends DataReaderImpl<XMLStreamReader> {
        public RfdJaxbDataReader() {
            super(RfdJaxbDataBinding.this, RfdJaxbDataBinding.this.isUnwrapJAXBElement());
        }

        @Override
        public Object read(MessagePartInfo msgPartInfo, XMLStreamReader reader) {
            QName msgPartQname = msgPartInfo.getElementQName();

            if (RfdJaxbDataBinding.this.schemaMetadatas.containsKey(msgPartQname.getNamespaceURI())) {
                SdcctXmlStreamReader delegatingReader =
                    ((SdcctXmlStreamReader) ((reader instanceof DepthXMLStreamReader) ? ((DepthXMLStreamReader) reader).getReader() : reader));
                ValidatingStreamReader delegateReader = ((ValidatingStreamReader) delegatingReader.getParent());
                XdmDocument msgPartDoc;

                try {
                    delegatingReader.setParent(new MessagePartXmlStreamReader(delegateReader, delegatingReader.getLocator(), delegatingReader.getValidator()));

                    msgPartDoc = RfdJaxbDataBinding.this.validatorService.validate(delegatingReader);
                } catch (ValidationException e) {
                    throw new RfdWsException(
                        String.format("Web service request message part (qname=%s, typeClass=%s) is invalid.", msgPartQname, msgPartInfo.getTypeClass()), e);
                } finally {
                    delegatingReader.setParent(delegateReader);
                }

                reader = new UnmarshallingXmlStreamReader(msgPartDoc.getUnderlyingNode(), RfdJaxbDataBinding.this.config.makePipelineConfiguration());

                LOGGER.trace(String.format("Web service request message part (qname=%s, typeClass=%s) is valid.", msgPartQname, msgPartInfo.getTypeClass()));
            }

            return super.read(msgPartInfo, reader);
        }
    }

    private final static Class<?>[] SUPPORTED_READER_FORMATS = new Class<?>[] { XMLStreamReader.class };
    private final static Class<?>[] SUPPORTED_WRITER_FORMATS = new Class<?>[] { XMLStreamWriter.class };

    private final static Logger LOGGER = LoggerFactory.getLogger(RfdJaxbDataBinding.class);

    @Autowired
    private SdcctSaxonConfiguration config;

    private JaxbContextMetadata[] contextMetadatas;
    private SdcctValidatorService validatorService;
    private Map<String, JaxbSchemaMetadata> schemaMetadatas;

    public RfdJaxbDataBinding() throws JAXBException {
        super(true, Collections.singletonMap(JAXB_SCAN_PACKAGES, false));
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> DataReader<T> createReader(Class<T> formatClass) {
        return (formatClass.equals(XMLStreamReader.class) ? ((DataReader<T>) new RfdJaxbDataReader()) : null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.schemaMetadatas = Stream.of(this.contextMetadatas).flatMap(contextMetadata -> contextMetadata.getSchemas().entrySet().stream())
            .collect(SdcctStreamUtils.toMap(LinkedHashMap::new));

        this.setExtraClass(Stream.of(this.contextMetadatas).flatMap(contextMetadata -> contextMetadata.getSchemaObjectFactories().entrySet().stream())
            .map(Object::getClass).toArray(Class[]::new));
    }

    public JaxbContextMetadata[] getContextMetadatas() {
        return this.contextMetadatas;
    }

    public void setContextMetadatas(JaxbContextMetadata ... contextMetadatas) {
        this.contextMetadatas = contextMetadatas;
    }

    @Override
    public Class<?>[] getSupportedReaderFormats() {
        return SUPPORTED_WRITER_FORMATS;
    }

    @Override
    public Class<?>[] getSupportedWriterFormats() {
        return SUPPORTED_READER_FORMATS;
    }

    public SdcctValidatorService getValidatorService() {
        return this.validatorService;
    }

    public void setValidatorService(SdcctValidatorService validatorService) {
        this.validatorService = validatorService;
    }
}
