package gov.hhs.onc.sdcct.xml.impl;

import net.sf.saxon.om.DocumentURI;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component("convDocUri")
public class DocumentUriConverter implements Converter<String, DocumentURI> {
    @Override
    public DocumentURI convert(String src) {
        return new DocumentURI(src);
    }
}
