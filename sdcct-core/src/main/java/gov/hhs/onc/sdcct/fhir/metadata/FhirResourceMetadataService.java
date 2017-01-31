package gov.hhs.onc.sdcct.fhir.metadata;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataService;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.xpath.saxon.impl.SdcctXpathExecutable;

public interface FhirResourceMetadataService extends ResourceMetadataService<Resource, FhirResourceMetadata<?>> {
    public SdcctXpathExecutable getElementDefXpathExecutable();

    public void setElementDefXpathExecutable(SdcctXpathExecutable elemDefXpathExec);

    public XdmDocument[] getProfileDocuments();

    public void setProfileDocuments(XdmDocument ... profileDocs);

    public SdcctXpathExecutable getSearchParamDefsXpathExecutable();

    public void setSearchParamDefsXpathExecutable(SdcctXpathExecutable searchParamDefsXpathExec);

    public SdcctXpathExecutable getSearchParamDefXpathExecutable();

    public void setSearchParamDefXpathExecutable(SdcctXpathExecutable searchParamDefXpathExec);

    public XdmDocument getSearchParamDocument();

    public void setSearchParamDocument(XdmDocument searchParamDoc);

    public SdcctXpathExecutable getStructDefXpathExecutable();

    public void setStructDefXpathExecutable(SdcctXpathExecutable structDefXpathExec);
}
