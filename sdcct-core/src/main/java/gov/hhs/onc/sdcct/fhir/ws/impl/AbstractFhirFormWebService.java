package gov.hhs.onc.sdcct.fhir.ws.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.fhir.Bundle;
import gov.hhs.onc.sdcct.fhir.BundleType;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.IssueType;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeType;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.impl.BundleEntryImpl;
import gov.hhs.onc.sdcct.fhir.impl.BundleImpl;
import gov.hhs.onc.sdcct.fhir.impl.BundleTypeComponentImpl;
import gov.hhs.onc.sdcct.fhir.impl.ResourceContainerImpl;
import gov.hhs.onc.sdcct.fhir.impl.UnsignedIntTypeImpl;
import gov.hhs.onc.sdcct.fhir.search.FhirSearchService;
import gov.hhs.onc.sdcct.fhir.ws.utils.SdcctFhirOperationOutcomeUtils.OperationOutcomeBuilder;
import gov.hhs.onc.sdcct.fhir.ws.utils.SdcctFhirOperationOutcomeUtils.OperationOutcomeIssueBuilder;
import gov.hhs.onc.sdcct.fhir.ws.FhirFormWebService;
import gov.hhs.onc.sdcct.fhir.ws.FhirWsException;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirInteractionWsMetadata;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirResourceWsMetadata;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirWsMetadata;
import gov.hhs.onc.sdcct.form.ws.impl.AbstractFormWebService;
import gov.hhs.onc.sdcct.net.http.SdcctHttpStatus;
import gov.hhs.onc.sdcct.ws.WsInteractionType;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.MessageContext;

public abstract class AbstractFhirFormWebService extends
    AbstractFormWebService<FhirResource, FhirResourceRegistry<?>, FhirSearchService<?>, FhirInteractionWsMetadata, FhirResourceWsMetadata<?>, FhirWsMetadata>
    implements FhirFormWebService {
    @Context
    protected MessageContext msgContext;

    @Override
    public Response search(String type, MultivaluedMap<String, String> params) throws Exception {
        return this.search(WsInteractionType.SEARCH_TYPE, type, params);
    }

    @Override
    public Response search(String type) throws Exception {
        return this.search(WsInteractionType.SEARCH_TYPE, type, this.msgContext.getUriInfo().getQueryParameters());
    }

    @Override
    public Response create(String type) throws Exception {
        throw new FhirWsException(String.format("FHIR resource (type=%s) operation not allowed: %s", type, WsInteractionType.CREATE))
            .setOperationOutcome(new OperationOutcomeBuilder().addIssues(new OperationOutcomeIssueBuilder().setType(IssueType.PROCESSING)
                .setDetails(OperationOutcomeType.MSG_OP_NOT_ALLOWED, WsInteractionType.CREATE).build()).build());
    }

    @Override
    public Response delete(String type, String id) throws Exception {
        throw new FhirWsException(String.format("FHIR resource (type=%s) operation not allowed: %s", type, WsInteractionType.DELETE))
            .setOperationOutcome(new OperationOutcomeBuilder().addIssues(new OperationOutcomeIssueBuilder().setType(IssueType.PROCESSING)
                .setDetails(OperationOutcomeType.MSG_OP_NOT_ALLOWED, WsInteractionType.DELETE).build()).build());
    }

    @Override
    public Response update(String type, String id) throws Exception {
        throw new FhirWsException(String.format("FHIR resource (type=%s) operation not allowed: %s", type, WsInteractionType.UPDATE))
            .setOperationOutcome(new OperationOutcomeBuilder().addIssues(new OperationOutcomeIssueBuilder().setType(IssueType.PROCESSING)
                .setDetails(OperationOutcomeType.MSG_OP_NOT_ALLOWED, WsInteractionType.UPDATE).build()).build());
    }

    @Override
    public Response vread(String type, String id, String version) throws Exception {
        FhirResourceWsMetadata<?> resourceMetadata = this.buildResourceMetadata(type);
        Class<? extends Resource> beanClass = resourceMetadata.getResourceMetadata().getBeanClass();
        FhirInteractionWsMetadata interactionMetadata = buildInteractionMetadata(type, resourceMetadata, WsInteractionType.VREAD);

        try {
            Resource resource = this.findBean(beanClass, this.resourceRegistries.get(beanClass), Long.parseLong(id), -1L, Long.parseLong(version));

            if (resource == null) {
                throw new FhirWsException(String.format("FHIR resource (type=%s, id=%s, version=%s) not found.", type, id, version))
                    .setOperationOutcome(new OperationOutcomeBuilder()
                        .addIssues(new OperationOutcomeIssueBuilder().setType(IssueType.NOT_FOUND).setDetails(OperationOutcomeType.MSG_NO_EXIST, id).build())
                        .build())
                    .setResponseStatus(SdcctHttpStatus.NOT_FOUND);
            }

            return Response.ok(resource).build();
        } catch (FhirWsException e) {
            throw e;
        } catch (Exception e) {
            throw new FhirWsException(String.format("Unable to read FHIR resource (type=%s, id=%s, version=%s).", type, id, version), e);
        }
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public Response read(String type, String id) throws Exception {
        FhirResourceWsMetadata<?> resourceMetadata = this.buildResourceMetadata(type);
        Class<? extends Resource> beanClass = resourceMetadata.getResourceMetadata().getBeanClass();
        FhirInteractionWsMetadata interactionMetadata = buildInteractionMetadata(type, resourceMetadata, WsInteractionType.READ);

        try {
            Resource resource = this.findBean(beanClass, this.resourceRegistries.get(beanClass), Long.parseLong(id), -1L, null);

            if (resource == null) {
                throw new FhirWsException(String.format("FHIR resource (type=%s, id=%s) not found.", type, id))
                    .setOperationOutcome(new OperationOutcomeBuilder()
                        .addIssues(new OperationOutcomeIssueBuilder().setType(IssueType.NOT_FOUND).setDetails(OperationOutcomeType.MSG_NO_EXIST, id).build())
                        .build())
                    .setResponseStatus(SdcctHttpStatus.NOT_FOUND);
            }

            return Response.ok(resource).build();
        } catch (FhirWsException e) {
            throw e;
        } catch (Exception e) {
            throw new FhirWsException(String.format("Unable to read FHIR resource (type=%s, id=%s).", type, id), e);
        }
    }

    protected static FhirInteractionWsMetadata buildInteractionMetadata(String type, FhirResourceWsMetadata<?> resourceMetadata,
        WsInteractionType interactionType) throws FhirWsException {
        Map<WsInteractionType, FhirInteractionWsMetadata> interactionMetadatas = resourceMetadata.getInteractionMetadatas();

        if (!interactionMetadatas.containsKey(interactionType)) {
            throw new FhirWsException(String.format("FHIR resource (type=%s) operation not allowed: %s", type, interactionType))
                .setOperationOutcome(new OperationOutcomeBuilder().addIssues(new OperationOutcomeIssueBuilder().setType(IssueType.PROCESSING)
                    .setDetails(OperationOutcomeType.MSG_OP_NOT_ALLOWED, interactionType).build()).build());
        }

        return interactionMetadatas.get(interactionType);
    }

    protected Response search(WsInteractionType interactionType, String type, MultivaluedMap<String, String> params) throws FhirWsException {
        FhirResourceWsMetadata<?> resourceMetadata = this.buildResourceMetadata(type);
        Class<? extends Resource> beanClass = resourceMetadata.getResourceMetadata().getBeanClass();
        FhirInteractionWsMetadata interactionMetadata = buildInteractionMetadata(type, resourceMetadata, interactionType);

        try {
            List<Resource> resources = this.findBeans(this.searchServices.get(beanClass), params);
            Bundle bundle = new BundleImpl().setTotal(new UnsignedIntTypeImpl().setValue(BigInteger.valueOf(resources.size())))
                .setType(new BundleTypeComponentImpl().setValue(BundleType.SEARCHSET));

            resources.forEach(resource -> bundle
                .addEntry(new BundleEntryImpl().setId(resource.getId().getValue()).setResource(new ResourceContainerImpl().setContent(resource))));

            return Response.ok(bundle).build();
        } catch (FhirWsException e) {
            throw e;
        } catch (Exception e) {
            throw new FhirWsException(String.format("Unable to search for FHIR resource (type=%s): params=%s", type, params), e);
        }
    }

    protected FhirResourceWsMetadata<?> buildResourceMetadata(String type) throws FhirWsException {
        if (!this.resourceMetadatas.containsKey(type)) {
            throw new FhirWsException(String.format("Unknown FHIR resource type: %s", type)).setOperationOutcome(new OperationOutcomeBuilder()
                .addIssues(new OperationOutcomeIssueBuilder().setType(IssueType.NOT_SUPPORTED).setDetails(OperationOutcomeType.MSG_NO_EXIST, type).build())
                .build()).setResponseStatus(SdcctHttpStatus.NOT_FOUND);
        }

        return this.resourceMetadatas.get(type);
    }
}
