package gov.hhs.onc.sdcct.rfd.ws.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.validate.SdcctValidatorService;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.apache.cxf.databinding.DataReader;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.jaxb.io.DataReaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class RfdJaxbDataBinding extends JAXBDataBinding implements InitializingBean {
    private class RfdJaxbDataReader extends DataReaderImpl<XMLStreamReader> {
        public RfdJaxbDataReader() {
            super(RfdJaxbDataBinding.this, RfdJaxbDataBinding.this.isUnwrapJAXBElement());
        }

        /**
         * TODO: Re-enable once Saxon <-> StAX(2) translation is fixed for validation.
         */
        // @formatter:off
        /*
        @Override
        public Object read(MessagePartInfo msgPartInfo, XMLStreamReader reader) {
            QName msgPartQname = msgPartInfo.getElementQName();

            if (RfdJaxbDataBinding.this.schemaMetadatas.containsKey(msgPartQname.getNamespaceURI())) {
                XdmDocument msgPartDoc;

                try {
                    msgPartDoc = RfdJaxbDataBinding.this.validatorService.validate(new SdcctXmlStreamReader(
                        ((XMLStreamReader2) ((reader instanceof DepthXMLStreamReader) ? ((DepthXMLStreamReader) reader).getReader() : reader))));
                } catch (ValidationException e) {
                    throw new RfdWsException(
                        String.format("Web service request message part (qname=%s, typeClass=%s) is invalid.", msgPartQname, msgPartInfo.getTypeClass()), e);
                }

                reader = new UnmarshallingXmlStreamReader(msgPartDoc.getUnderlyingNode(), RfdJaxbDataBinding.this.config.makePipelineConfiguration());

                LOGGER.trace(String.format("Web service request message part (qname=%s, typeClass=%s) is valid.", msgPartQname, msgPartInfo.getTypeClass()));
            }

            return super.read(msgPartInfo, reader);
        }
        */
        // @formatter:on
    }

    private final static Class<?>[] SUPPORTED_READER_FORMATS = new Class<?>[] { XMLStreamReader.class };
    private final static Class<?>[] SUPPORTED_WRITER_FORMATS = new Class<?>[] { XMLStreamWriter.class };

    private final static Logger LOGGER = LoggerFactory.getLogger(RfdJaxbDataBinding.class);

    @Autowired
    private SdcctConfiguration config;

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
