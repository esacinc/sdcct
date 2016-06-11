package gov.hhs.onc.sdcct.xml.impl;

import net.sf.saxon.om.DocumentURI;
import org.apache.commons.lang3.StringUtils;

public class SdcctDocumentUri extends DocumentURI {
    private String uri;

    public SdcctDocumentUri(String uri) {
        super(StringUtils.EMPTY);

        this.uri = uri;
    }

    @Override
    public String toString() {
        return this.uri;
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof SdcctDocumentUri) && this.uri.equals(((SdcctDocumentUri) obj).getUri()));
    }

    @Override
    public int hashCode() {
        return this.uri.hashCode();
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
