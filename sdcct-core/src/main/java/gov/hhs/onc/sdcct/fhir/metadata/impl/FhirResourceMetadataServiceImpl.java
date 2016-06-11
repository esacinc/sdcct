package gov.hhs.onc.sdcct.fhir.metadata.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamMetadata;
import gov.hhs.onc.sdcct.data.metadata.impl.AbstractResourceMetadataService;
import gov.hhs.onc.sdcct.data.metadata.impl.ResourceParamBindingImpl;
import gov.hhs.onc.sdcct.data.metadata.impl.ResourceParamCardinalityImpl;
import gov.hhs.onc.sdcct.data.metadata.impl.ResourceParamMetadataImpl;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.fhir.Element;
import gov.hhs.onc.sdcct.fhir.ElementDefinition;
import gov.hhs.onc.sdcct.fhir.ElementDefinitionBinding;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.impl.ElementDefinitionImpl;
import gov.hhs.onc.sdcct.fhir.impl.SearchParameterImpl;
import gov.hhs.onc.sdcct.fhir.impl.StructureDefinitionImpl;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadataService;
import gov.hhs.onc.sdcct.fhir.Reference;
import gov.hhs.onc.sdcct.fhir.ResourceType;
import gov.hhs.onc.sdcct.fhir.SearchParameter;
import gov.hhs.onc.sdcct.fhir.UriType;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.xpath.StaticXpathOptions;
import gov.hhs.onc.sdcct.xml.xpath.impl.DynamicXpathOptionsImpl;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import java.net.URI;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.FatalBeanException;

public class FhirResourceMetadataServiceImpl extends AbstractResourceMetadataService<Resource, FhirResourceMetadata<?>> implements FhirResourceMetadataService {
    private final static QName BASE_XPATH_VAR_QNAME = new QName("base");
    private final static QName ID_XPATH_VAR_QNAME = new QName("id");
    private final static QName PATH_XPATH_VAR_QNAME = new QName("path");

    @javax.annotation.Resource(name = "xpathOptsStaticFhir")
    private StaticXpathOptions staticXpathOpts;

    private SdcctXpathExecutable elemDefXpathExec;
    private XdmDocument[] profileDocs;
    private SdcctXpathExecutable searchParamDefsXpathExec;
    private SdcctXpathExecutable searchParamDefXpathExec;
    private XdmDocument searchParamDoc;
    private SdcctXpathExecutable structDefXpathExec;

    public FhirResourceMetadataServiceImpl() {
        super(SpecificationType.FHIR);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        XdmNode structDefNode = this.buildStructDefNode(ResourceType.RESOURCE.getId());

        for (ResourceParamMetadata baseParamMetadata : this.baseParamMetadatas) {
            this.buildParamMetadata(structDefNode, this.buildSearchParamDef(this.searchParamDefXpathExec.load(new DynamicXpathOptionsImpl()
                .setContextNode(this.searchParamDoc).addVariable(ID_XPATH_VAR_QNAME, new XdmAtomicValue(baseParamMetadata.getId()))).evaluateNode()),
                baseParamMetadata);
        }

        super.afterPropertiesSet();
    }

    @Override
    protected FhirResourceMetadata<?> buildMetadata(String id, String path, FhirResourceMetadata<?> metadata) throws Exception {
        XdmNode structDefNode = this.buildStructDefNode(id);

        metadata.setUri(URI.create(
            this.xmlCodec.decode(this.xmlCodec.encode(structDefNode.getUnderlyingNode(), null), StructureDefinitionImpl.class, null).getUrl().getValue()));

        SearchParameter searchParamDef;

        for (XdmNode searchParamNode : this.searchParamDefsXpathExec
            .load(new DynamicXpathOptionsImpl().setContextNode(this.searchParamDoc).addVariable(BASE_XPATH_VAR_QNAME, new XdmAtomicValue(path)))
            .evaluateNodes()) {
            metadata.addParamMetadatas(this.buildParamMetadata(structDefNode, (searchParamDef = this.buildSearchParamDef(searchParamNode)),
                new ResourceParamMetadataImpl(this.specType, SdcctEnumUtils.findById(ResourceParamType.class, searchParamDef.getType().getValue().getId()),
                    searchParamDef.getId().getValue(), searchParamDef.getName().getValue())));
        }

        return super.buildMetadata(id, path, metadata);
    }

    private ResourceParamMetadata buildParamMetadata(XdmNode structDefNode, SearchParameter searchParamDef, ResourceParamMetadata paramMetadata)
        throws Exception {
        paramMetadata.setUri(URI.create(searchParamDef.getUrl().getValue()));

        if (!searchParamDef.hasExpression()) {
            return paramMetadata;
        }

        String paramExpr = searchParamDef.getExpression().getValue();
        paramMetadata.setExpression(StringUtils.split(paramExpr, SdcctStringUtils.PERIOD, 2)[1]);

        paramMetadata.setXpathExecutable(
            this.xpathCompiler.compile(StringUtils.split(searchParamDef.getXpath().getValue(), SdcctStringUtils.SLASH, 2)[1], this.staticXpathOpts.clone()));

        XdmNode elemDefNode = this.elemDefXpathExec
            .load(new DynamicXpathOptionsImpl().setContextNode(structDefNode).addVariable(PATH_XPATH_VAR_QNAME, new XdmAtomicValue(paramExpr))).evaluateNode();

        if (elemDefNode == null) {
            paramMetadata.setCardinality(new ResourceParamCardinalityImpl(1, 1));

            return paramMetadata;
        }

        ElementDefinition elemDef = this.xmlCodec.decode(this.xmlCodec.encode(elemDefNode.getUnderlyingNode(), null), ElementDefinitionImpl.class, null);

        // TODO: support multiple resource parameter types
        paramMetadata.setValueType(elemDef.getType().get(0).getCode().getValue());

        paramMetadata.setCardinality(new ResourceParamCardinalityImpl(elemDef.getMin().getValue(), elemDef.getMax().getValue()));

        if (!elemDef.hasBinding()) {
            return paramMetadata;
        }

        ElementDefinitionBinding elemDefBinding = elemDef.getBinding();
        Element elemDefBindingValueSet = elemDefBinding.getValueSet();

        paramMetadata.setBinding(new ResourceParamBindingImpl(
            URI.create(((elemDefBindingValueSet instanceof Reference)
                ? ((Reference) elemDefBindingValueSet).getReference().getValue() : ((UriType) elemDefBindingValueSet).getValue())),
            elemDefBinding.getStrength().getValue()));

        return paramMetadata;
    }

    private SearchParameter buildSearchParamDef(XdmNode searchParamDefNode) throws Exception {
        return this.xmlCodec.decode(this.xmlCodec.encode(searchParamDefNode.getUnderlyingNode(), null), SearchParameterImpl.class, null);
    }

    private XdmNode buildStructDefNode(String structDefId) throws Exception {
        XdmAtomicValue structDefIdValue = new XdmAtomicValue(structDefId);
        XdmNode structDefNode = null;

        for (XdmDocument profileDoc : this.profileDocs) {
            if ((structDefNode = this.structDefXpathExec
                .load(new DynamicXpathOptionsImpl().setContextNode(profileDoc).addVariable(ID_XPATH_VAR_QNAME, structDefIdValue)).evaluateNode()) != null) {
                break;
            }
        }

        if (structDefNode == null) {
            throw new FatalBeanException(String.format("Unable to find %s StructureDefinition (id=%s) XML node.", this.specType.name(), structDefId));
        }

        return structDefNode;
    }

    @Override
    public SdcctXpathExecutable getElementDefXpathExecutable() {
        return this.elemDefXpathExec;
    }

    @Override
    public void setElementDefXpathExecutable(SdcctXpathExecutable elemDefXpathExec) {
        this.elemDefXpathExec = elemDefXpathExec;
    }

    @Override
    public XdmDocument[] getProfileDocuments() {
        return this.profileDocs;
    }

    @Override
    public void setProfileDocuments(XdmDocument ... profileDocs) {
        this.profileDocs = profileDocs;
    }

    @Override
    public SdcctXpathExecutable getSearchParamDefsXpathExecutable() {
        return this.searchParamDefsXpathExec;
    }

    @Override
    public void setSearchParamDefsXpathExecutable(SdcctXpathExecutable searchParamDefsXpathExec) {
        this.searchParamDefsXpathExec = searchParamDefsXpathExec;
    }

    @Override
    public SdcctXpathExecutable getSearchParamDefXpathExecutable() {
        return this.searchParamDefXpathExec;
    }

    @Override
    public void setSearchParamDefXpathExecutable(SdcctXpathExecutable searchParamDefXpathExec) {
        this.searchParamDefXpathExec = searchParamDefXpathExec;
    }

    @Override
    public XdmDocument getSearchParamDocument() {
        return this.searchParamDoc;
    }

    @Override
    public void setSearchParamDocument(XdmDocument searchParamDoc) {
        this.searchParamDoc = searchParamDoc;
    }

    @Override
    public SdcctXpathExecutable getStructDefXpathExecutable() {
        return this.structDefXpathExec;
    }

    @Override
    public void setStructDefXpathExecutable(SdcctXpathExecutable structDefXpathExec) {
        this.structDefXpathExec = structDefXpathExec;
    }
}
