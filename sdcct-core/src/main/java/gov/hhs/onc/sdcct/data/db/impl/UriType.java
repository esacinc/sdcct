package gov.hhs.onc.sdcct.data.db.impl;

import java.net.URI;
import org.springframework.stereotype.Component;

@Component("typeUri")
public class UriType extends AbstractVarcharType<URI> {
    private static class UriTypeDescriptor extends AbstractVarcharTypeDescriptor<URI> {
        private final static UriTypeDescriptor INSTANCE = new UriTypeDescriptor();

        private final static long serialVersionUID = 0L;

        private UriTypeDescriptor() {
            super(URI.class, URI::create, Object::toString);
        }
    }

    private final static long serialVersionUID = 0L;

    public UriType() {
        super(URI.class, UriTypeDescriptor.INSTANCE);
    }
}
