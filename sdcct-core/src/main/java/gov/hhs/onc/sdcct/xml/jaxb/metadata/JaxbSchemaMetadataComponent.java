package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import com.sun.msv.grammar.ReferenceExp;
import javax.xml.namespace.QName;

public interface JaxbSchemaMetadataComponent<T extends ReferenceExp> extends JaxbContextMetadataComponent<T> {
    public boolean isAbstract();

    public QName getQname();

    public JaxbSchemaMetadata getSchema();
}
