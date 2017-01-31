package gov.hhs.onc.sdcct.json.xml.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlInputFactory;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlOutputFactory;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

public class SdcctXmlMapper extends ObjectMapper {
    public static class SdcctXmlFactory extends XmlFactory {
        private final static long serialVersionUID = 0L;

        public SdcctXmlFactory(SdcctXmlInputFactory xmlInFactory, SdcctXmlOutputFactory xmlOutFactory) {
            super(xmlInFactory, xmlOutFactory);
        }

        public SdcctXmlFactory(XmlFactory factory) {
            super(factory, null);
        }

        @Override
        public SdcctXmlFactory copy() {
            return new SdcctXmlFactory(this);
        }

        @Override
        protected void _initFactories(XMLInputFactory xmlInFactory, XMLOutputFactory xmlOutFactory) {
        }
    }

    public static class SdcctXmlSerializerProvider extends XmlSerializerProvider {
        private final static long serialVersionUID = 0L;

        public SdcctXmlSerializerProvider(SdcctXmlSerializerProvider prov) {
            this(prov.getRootNameLookup());
        }

        public SdcctXmlSerializerProvider(XmlRootNameLookup rootNameLookup) {
            super(rootNameLookup);
        }

        public void initialize(ToXmlGenerator gen, QName rootQname) throws IOException {
            super._initWithRootName(gen, rootQname);
        }

        @Override
        public SdcctXmlSerializerProvider copy() {
            return new SdcctXmlSerializerProvider(this);
        }

        public XmlRootNameLookup getRootNameLookup() {
            return this._rootNameLookup;
        }
    }

    private final static long serialVersionUID = 0L;

    public SdcctXmlMapper(SdcctXmlInputFactory xmlInFactory, SdcctXmlOutputFactory xmlOutFactory) {
        super(new SdcctXmlFactory(xmlInFactory, xmlOutFactory), new SdcctXmlSerializerProvider(new XmlRootNameLookup()), null);
    }

    public SdcctXmlMapper(SdcctXmlMapper mapper) {
        super(mapper);
    }

    @Override
    public SdcctXmlMapper copy() {
        return new SdcctXmlMapper(this);
    }

    @Override
    public SdcctXmlFactory getFactory() {
        return ((SdcctXmlFactory) super.getFactory());
    }

    @Override
    public SdcctXmlSerializerProvider getSerializerProvider() {
        return ((SdcctXmlSerializerProvider) super.getSerializerProvider());
    }
}
