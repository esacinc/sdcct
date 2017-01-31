package gov.hhs.onc.sdcct.validate.schematron.impl;

import gov.hhs.onc.sdcct.schematron.Namespace;
import gov.hhs.onc.sdcct.schematron.Schema;
import gov.hhs.onc.sdcct.schematron.impl.SchemaImpl;
import gov.hhs.onc.sdcct.transform.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.validate.schematron.SdcctSchematron;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlOutputFactory;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.xslt.saxon.impl.SdcctXsltCompiler;
import gov.hhs.onc.sdcct.xml.xslt.saxon.impl.SdcctXsltExecutable;
import gov.hhs.onc.sdcct.xml.xslt.saxon.impl.SdcctXsltTransformer;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.sf.saxon.stax.XMLStreamWriterDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctSchematronImpl implements SdcctSchematron {
    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctSchematronImpl.class);

    @Autowired
    private SdcctXsltCompiler xsltCompiler;

    @Autowired
    private XmlCodec xmlCodec;

    @Autowired
    private SdcctXmlOutputFactory xmlOutFactory;

    private String id;
    private String name;
    private XdmDocument doc;
    private String queryBinding;
    private String schemaVersion;
    private SdcctXsltExecutable[] xsltExecs;
    private Map<String, String> schemaNamespaces;
    private SdcctXsltExecutable schemaXsltExec;

    public SdcctSchematronImpl(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.buildSchema();
    }

    private void buildSchema() throws Exception {
        Schema schema = this.xmlCodec.decode(this.doc.getSource(), SchemaImpl.class, null);
        schema.setId(this.id);
        schema.setQueryBinding(this.queryBinding);
        schema.setSchemaVersion(this.schemaVersion);

        this.schemaNamespaces = SdcctStreamUtils.asInstances(schema.getContent().stream(), Namespace.class)
            .collect(SdcctStreamUtils.toMap(Namespace::getPrefix, Namespace::getUri, TreeMap::new));

        String publicId = this.doc.getPublicId(), sysId = this.doc.getSystemId();

        SdcctXsltTransformer[] xsltTransformers = Stream.of(this.xsltExecs).map(SdcctXsltExecutable::load).toArray(SdcctXsltTransformer[]::new);
        xsltTransformers[0].setSource(new ByteArraySource(this.xmlCodec.encode(schema, null), publicId, sysId));

        IntStream.range(0, (xsltTransformers.length - 1))
            .forEach(xsltTransformerIndex -> xsltTransformers[xsltTransformerIndex].setDestination(xsltTransformers[(xsltTransformerIndex + 1)]));

        ByteArrayResult schemaResult = new ByteArrayResult(publicId, sysId);
        xsltTransformers[(xsltTransformers.length - 1)].setDestination(new XMLStreamWriterDestination(this.xmlOutFactory.createXMLStreamWriter(schemaResult)));

        xsltTransformers[0].transform();

        this.schemaXsltExec = this.xsltCompiler.compile(new ByteArraySource(schemaResult.getBytes(), sysId));

        // noinspection ConstantConditions
        LOGGER.debug(String.format("Built Schematron schema (id=%s, name=%s) from document (publicId=%s, sysId=%s, uri=%s).", this.id, this.name, publicId,
            sysId, this.doc.getDocumentUri().getUri()));
    }

    @Override
    public XdmDocument getDocument() {
        return this.doc;
    }

    @Override
    public void setDocument(XdmDocument doc) {
        this.doc = doc;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getQueryBinding() {
        return this.queryBinding;
    }

    @Override
    public void setQueryBinding(String queryBinding) {
        this.queryBinding = queryBinding;
    }

    @Override
    public Map<String, String> getSchemaNamespaces() {
        return this.schemaNamespaces;
    }

    @Override
    public String getSchemaVersion() {
        return this.schemaVersion;
    }

    @Override
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    @Override
    public SdcctXsltExecutable getSchemaXsltExecutable() {
        return this.schemaXsltExec;
    }

    @Override
    public SdcctXsltExecutable[] getXsltExecutables() {
        return this.xsltExecs;
    }

    @Override
    public void setXsltExecutables(SdcctXsltExecutable ... xsltExecs) {
        this.xsltExecs = xsltExecs;
    }
}
