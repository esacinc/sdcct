package gov.hhs.onc.sdcct.json.xml.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import javax.annotation.Resource;
import org.codehaus.stax2.XMLStreamWriter2;
import org.springframework.beans.factory.annotation.Autowired;

public class XmlTranscoder {
    @Autowired
    private SdcctXmlMapper xmlMapper;

    @Resource(name = "objMapper")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper objMapper;

    @Autowired
    private JaxbContextRepository jaxbContextRepo;

    public <T, U extends XMLStreamWriter2> U transcode(byte[] src, Class<T> srcClass, U resultWriter) throws Exception {
        try (
            JsonParser srcParser = this.objMapper.getFactory().createParser(src);
            ToXmlGenerator resultGen = this.xmlMapper.getFactory().createGenerator(resultWriter)) {
            this.xmlMapper.getSerializerProvider().initialize(resultGen, this.jaxbContextRepo.findTypeMetadata(srcClass).getQname());

            while (srcParser.nextToken() != null) {
                resultGen.copyCurrentEvent(srcParser);
            }

            return resultWriter;
        }
    }
}
