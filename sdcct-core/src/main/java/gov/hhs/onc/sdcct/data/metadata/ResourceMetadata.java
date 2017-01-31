package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.beans.PathBean;
import gov.hhs.onc.sdcct.validate.schematron.SdcctSchematron;
import gov.hhs.onc.sdcct.xml.xpath.saxon.impl.SdcctXpathExecutable;
import java.util.Map;
import javax.annotation.Nullable;

public interface ResourceMetadata<T> extends PathBean, ResourceMetadataComponent {
    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();

    public SdcctXpathExecutable[] getCanonicalRemoveXpathExecutables();

    public void setCanonicalRemoveXpathExecutables(SdcctXpathExecutable ... canonicalRemoveXpathExecs);

    public void addParamMetadatas(ResourceParamMetadata ... paramMetadatas);

    public Map<String, ResourceParamMetadata> getParamMetadatas();

    public void setParamMetadatas(ResourceParamMetadata ... paramMetadatas);

    public boolean hasSchematrons();

    @Nullable
    public SdcctSchematron[] getSchematrons();

    public void setSchematrons(SdcctSchematron ... schematrons);
}
