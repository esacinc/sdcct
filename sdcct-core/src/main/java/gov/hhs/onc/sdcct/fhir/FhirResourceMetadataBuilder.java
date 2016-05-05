package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataBuilder;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import java.util.Map;

public interface FhirResourceMetadataBuilder extends ResourceMetadataBuilder<ResourceType, DomainResource, FhirResourceMetadata<? extends DomainResource>> {
    public SdcctXpathExecutable getResourceDefXpathExecutable();

    public void setResourceDefXpathExecutable(SdcctXpathExecutable resourceDefXpathExec);

    public XdmDocument getResourceDocument();

    public void setResourceDocument(XdmDocument resourceDoc);

    public SdcctXpathExecutable getResourceParamDefXpathExecutable();

    public void setResourceParamDefXpathExecutable(SdcctXpathExecutable resourceParamDefXpathExec);

    public SdcctXpathExecutable getResourceParamValueSetUriXpathExecutable();

    public void setResourceParamValueSetUriXpathExecutable(SdcctXpathExecutable resourceParamValueSetUriXpathExec);

    public SdcctXpathExecutable getSearchParamDefXpathExecutable();

    public void setSearchParamDefXpathExecutable(SdcctXpathExecutable searchParamDefXpathExec);

    public XdmDocument getSearchParamDocument();

    public void setSearchParamDocument(XdmDocument searchParamDoc);

    public Map<String, String> getSearchParamValueTypes();

    public void setSearchParamValueTypes(Map<String, String> searchParamValueTypes);
}
