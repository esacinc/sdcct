package gov.hhs.onc.sdcct.fhir.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.SearchParamMetadata;
import gov.hhs.onc.sdcct.data.metadata.impl.AbstractResourceMetadataBuilder;
import gov.hhs.onc.sdcct.data.metadata.impl.SearchParamMetadataImpl;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.ElementDefinition;
import gov.hhs.onc.sdcct.fhir.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.FhirResourceMetadataBuilder;
import gov.hhs.onc.sdcct.fhir.ResourceType;
import gov.hhs.onc.sdcct.fhir.SearchParameter;
import gov.hhs.onc.sdcct.fhir.StructureDefinition;
import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.utils.SdcctClassUtils;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.xml.SdcctXmlPrefixes;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.xpath.impl.DynamicXpathOptions;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import gov.hhs.onc.sdcct.xml.xpath.impl.StaticXpathOptions;
import java.net.URI;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmNode;
import org.apache.commons.lang3.StringUtils;

public class FhirResourceMetadataBuilderImpl extends
    AbstractResourceMetadataBuilder<ResourceType, DomainResource, FhirResourceMetadata<? extends DomainResource>> implements FhirResourceMetadataBuilder {
    private final static String BASE_RESOURCE_XPATH_EXPR_PREFIX = SdcctXmlPrefixes.FHIR_XPATH + SdcctStringUtils.COLON + ResourceType.RESOURCE.getId();

    private final static StaticXpathOptions SEARCH_PARAM_STATIC_XPATH_OPTS = new StaticXpathOptions().addNamespace(SdcctXmlPrefixes.FHIR_XPATH,
        SdcctUris.FHIR_URL_VALUE);

    private final static QName RESOURCE_PARAM_PATH_XPATH_VAR_QNAME = new QName("path");
    private final static QName SEARCH_PARAM_BASE_XPATH_VAR_QNAME = new QName("base");

    private SdcctXpathExecutable resourceDefXpathExec;
    private XdmDocument resourceDoc;
    private SdcctXpathExecutable resourceParamDefXpathExec;
    private SdcctXpathExecutable resourceParamValueSetUriXpathExec;
    private SdcctXpathExecutable searchParamDefXpathExec;
    private XdmDocument searchParamDoc;
    private Map<String, String> searchParamValueTypes;

    public FhirResourceMetadataBuilderImpl() {
        super(SpecificationType.FHIR);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected Map<ResourceType, FhirResourceMetadata<? extends DomainResource>> buildInternal() throws Exception {
        List<XdmNode> baseSearchParamDefNodes =
            this.searchParamDefXpathExec.load(
                new DynamicXpathOptions().setContextNode(this.searchParamDoc).addVariable(SEARCH_PARAM_BASE_XPATH_VAR_QNAME,
                    new XdmAtomicValue(ResourceType.RESOURCE.getId()))).evaluateNodes();
        Map<String, SearchParameter> baseSearchParamDefs = new TreeMap<>();
        SearchParameter searchParamDef;

        for (XdmNode baseSearchParamDefNode : baseSearchParamDefNodes) {
            baseSearchParamDefs.put((searchParamDef = this.buildSearchParamDef(baseSearchParamDefNode)).getName().getValue(), searchParamDef);
        }

        Map<ResourceType, FhirResourceMetadata<? extends DomainResource>> resources = new EnumMap<>(ResourceType.class);
        StructureDefinition resourceDef;
        String resourceName;
        ResourceType resourceType;
        Package resourcePkg = DomainResource.class.getPackage();
        Class<DomainResource> resourceClass;
        FhirResourceMetadata<?> resource;
        Map<String, SearchParamMetadata> searchParamMetadatas;
        SearchParamMetadata searchParam;

        for (XdmNode resourceDefNode : this.resourceDefXpathExec.load(new DynamicXpathOptions().setContextNode(this.resourceDoc)).evaluateNodes()) {
            resourceDef = this.xmlCodec.decode(resourceDefNode.getUnderlyingNode(), StructureDefinitionImpl.class, null);

            resources
                .put(
                    (resourceType = SdcctEnumUtils.findById(ResourceType.class, (resourceName = resourceDef.getName().getValue()))),
                    (resource =
                        new FhirResourceMetadataImpl<>(resourceDef.getId().getValue(), resourceName, URI.create(resourceDef.getUrl().getValue()), resourceType,
                            (resourceClass =
                                ((Class<DomainResource>) SdcctClassUtils.buildInterfaceClass(this.beanClassLoader, DomainResource.class, resourcePkg,
                                    resourceName))), SdcctClassUtils.buildImplClass(this.beanClassLoader, DomainResourceImpl.class, resourceClass))));

            searchParamMetadatas = resource.getSearchParamMetadatas();

            for (String searchParamName : baseSearchParamDefs.keySet()) {
                searchParamMetadatas.put(searchParamName,
                    this.buildSearchParamMetadata(resourceDefNode, true, resourceType, baseSearchParamDefs.get(searchParamName)));
            }

            // noinspection ConstantConditions
            for (XdmNode searchParamDefNode : this.searchParamDefXpathExec.load(
                new DynamicXpathOptions().setContextNode(this.searchParamDoc).addVariable(SEARCH_PARAM_BASE_XPATH_VAR_QNAME,
                    new XdmAtomicValue(resourceType.getId()))).evaluateNodes()) {
                searchParamMetadatas
                    .put((searchParam = this.buildSearchParamMetadata(resourceDefNode, false, resourceType, this.buildSearchParamDef(searchParamDefNode)))
                        .getName(), searchParam);
            }
        }

        return resources;
    }

    private SearchParamMetadata buildSearchParamMetadata(XdmNode resourceDefNode, boolean baseResource, ResourceType resourceType,
        SearchParameter searchParamDef) throws Exception {
        SearchParamType searchParamType = SdcctEnumUtils.findById(SearchParamType.class, searchParamDef.getType().getValue().getId());
        String searchParamId = searchParamDef.getId().getValue(), searchParamName = searchParamDef.getName().getValue(), searchParamValueType;
        URI searchParamUri = URI.create(searchParamDef.getUrl().getValue());
        SdcctXpathExecutable searchParamXpathExec = null;

        if (searchParamDef.hasXpath()) {
            String searchParamXpathExpr = searchParamDef.getXpath().getValue();

            if (baseResource) {
                searchParamXpathExpr =
                    (SdcctXmlPrefixes.FHIR_XPATH + SdcctStringUtils.COLON + resourceType.getId() + StringUtils.removeStart(searchParamXpathExpr,
                        BASE_RESOURCE_XPATH_EXPR_PREFIX));
            }

            searchParamXpathExec = this.xpathCompiler.compile(searchParamXpathExpr, SEARCH_PARAM_STATIC_XPATH_OPTS.clone());
        }

        SearchParamMetadata searchParamMetadata =
            new SearchParamMetadataImpl(SpecificationType.FHIR, searchParamId, searchParamName, searchParamUri, searchParamType, searchParamXpathExec,
                baseResource);

        searchParamValueType = null;

        if (searchParamDef.hasExpression()) {
            XdmNode resourceParamDefNode =
                this.resourceParamDefXpathExec.load(
                    new DynamicXpathOptions().setContextNode(resourceDefNode).addVariable(RESOURCE_PARAM_PATH_XPATH_VAR_QNAME,
                        new XdmAtomicValue(searchParamDef.getExpression().getValue()))).evaluateNode();

            if (resourceParamDefNode != null) {
                ElementDefinition resourceParamDef = this.xmlCodec.decode(resourceParamDefNode.getUnderlyingNode(), ElementDefinitionImpl.class, null);

                searchParamValueType = resourceParamDef.getType().get(0).getCode().getValue();

                String resourceParamValueSetUriStr =
                    this.resourceParamValueSetUriXpathExec.load(new DynamicXpathOptions().setContextNode(resourceParamDefNode)).evaluateString();

                if (resourceParamValueSetUriStr != null) {
                    searchParamMetadata.setValueSetUri(URI.create(resourceParamValueSetUriStr));
                }
            }
        }

        searchParamMetadata.setValueType(((searchParamValueType != null) ? searchParamValueType : this.searchParamValueTypes.get(searchParamName)));

        return searchParamMetadata;
    }

    private SearchParameter buildSearchParamDef(XdmNode searchParamDefNode) throws Exception {
        return this.xmlCodec.decode(searchParamDefNode.getUnderlyingNode(), SearchParameterImpl.class, null);
    }

    @Override
    public SdcctXpathExecutable getResourceDefXpathExecutable() {
        return this.resourceDefXpathExec;
    }

    @Override
    public void setResourceDefXpathExecutable(SdcctXpathExecutable resourceDefXpathExec) {
        this.resourceDefXpathExec = resourceDefXpathExec;
    }

    @Override
    public XdmDocument getResourceDocument() {
        return this.resourceDoc;
    }

    @Override
    public void setResourceDocument(XdmDocument resourceDoc) {
        this.resourceDoc = resourceDoc;
    }

    @Override
    public SdcctXpathExecutable getResourceParamDefXpathExecutable() {
        return this.resourceParamDefXpathExec;
    }

    @Override
    public void setResourceParamDefXpathExecutable(SdcctXpathExecutable resourceParamDefXpathExec) {
        this.resourceParamDefXpathExec = resourceParamDefXpathExec;
    }

    @Override
    public SdcctXpathExecutable getResourceParamValueSetUriXpathExecutable() {
        return this.resourceParamValueSetUriXpathExec;
    }

    @Override
    public void setResourceParamValueSetUriXpathExecutable(SdcctXpathExecutable resourceParamValueSetUriXpathExec) {
        this.resourceParamValueSetUriXpathExec = resourceParamValueSetUriXpathExec;
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
    public Map<String, String> getSearchParamValueTypes() {
        return this.searchParamValueTypes;
    }

    @Override
    public void setSearchParamValueTypes(Map<String, String> searchParamValueTypes) {
        this.searchParamValueTypes = searchParamValueTypes;
    }
}
