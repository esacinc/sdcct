package gov.hhs.onc.sdcct.fhir.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceProcessor;
import gov.hhs.onc.sdcct.data.metadata.MetadataService;
import gov.hhs.onc.sdcct.data.metadata.SearchParamMetadata;
import gov.hhs.onc.sdcct.data.search.DateSearchPeriod;
import gov.hhs.onc.sdcct.data.search.SearchParam;
import gov.hhs.onc.sdcct.data.search.impl.DateSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.DateSearchPeriodImpl;
import gov.hhs.onc.sdcct.data.search.impl.NumberSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.QuantitySearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.RefSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.StringSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.TokenSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.UriSearchParamImpl;
import gov.hhs.onc.sdcct.fhir.Coding;
import gov.hhs.onc.sdcct.fhir.ContactPoint;
import gov.hhs.onc.sdcct.fhir.ContactPointSystem;
import gov.hhs.onc.sdcct.fhir.DatatypeType;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.Element;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.FhirResourceProcessor;
import gov.hhs.onc.sdcct.fhir.Identifier;
import gov.hhs.onc.sdcct.fhir.Period;
import gov.hhs.onc.sdcct.fhir.Quantity;
import gov.hhs.onc.sdcct.fhir.ResourceType;
import gov.hhs.onc.sdcct.fhir.SpecialValueType;
import gov.hhs.onc.sdcct.fhir.Timing;
import gov.hhs.onc.sdcct.fhir.TimingRepeat;
import gov.hhs.onc.sdcct.utils.SdcctDateFormatUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.lang3.ArrayUtils;

public class FhirResourceProcessorImpl extends
    AbstractResourceProcessor<ResourceType, DomainResource, FhirResource, FhirResourceMetadata<? extends DomainResource>> implements FhirResourceProcessor {
    public FhirResourceProcessorImpl() {
        super(DomainResource.class, FhirResource.class, MetadataService::getFhirResourceMetadatas);
    }

    @Override
    protected void buildCompositeSearchParam(FhirResource resource, ResourceType type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        // TODO: implement
    }

    @Override
    protected void buildDateSearchParam(FhirResource resource, ResourceType type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        String valueType = metadata.getValueType();
        Date startValue = null, endValue = null;
        Period period = null;

        if (valueType.equals(DatatypeType.DATE.getId())) {
            startValue = endValue = SdcctDateFormatUtils.parse(this.decodeNode(((XdmNode) item), DateTypeImpl.class).getValue(), null, Locale.ROOT);
        } else if (valueType.equals(DatatypeType.DATE_TIME.getId())) {
            startValue = endValue = SdcctDateFormatUtils.parse(this.decodeNode(((XdmNode) item), DateTimeTypeImpl.class).getValue(), null, Locale.ROOT);
        } else if (valueType.equals(DatatypeType.INSTANT.getId())) {
            startValue = endValue = this.decodeNode(((XdmNode) item), InstantTypeImpl.class).getValue().toGregorianCalendar().getTime();
        } else if (valueType.equals(DatatypeType.PERIOD.getId())) {
            period = this.decodeNode(((XdmNode) item), PeriodImpl.class);
        } else if (valueType.equals(DatatypeType.TIMING.getId())) {
            Timing timing = this.decodeNode(((XdmNode) item), TimingImpl.class);

            if (timing.hasRepeat()) {
                TimingRepeat timingRepeat = timing.getRepeat();

                if (timingRepeat.hasBounds()) {
                    Element timingRepeatBounds = timingRepeat.getBounds();

                    if (timingRepeatBounds instanceof Period) {
                        period = ((Period) timingRepeatBounds);
                    }
                }
            }
        }

        if (period != null) {
            startValue = (period.hasStart() ? SdcctDateFormatUtils.parse(period.getStart().getValue(), null, Locale.ROOT) : null);
            endValue = (period.hasEnd() ? SdcctDateFormatUtils.parse(period.getEnd().getValue(), null, Locale.ROOT) : null);
        }

        if ((startValue == null) && (endValue == null)) {
            super.buildDateSearchParam(resource, type, searchParams, name, metadata, item);
        }

        DateSearchPeriod value = new DateSearchPeriodImpl(startValue, endValue);

        searchParams.put(new MultiKey<>(name, value), new DateSearchParamImpl(resource, name, value));
    }

    @Override
    protected void buildNumberSearchParam(FhirResource resource, ResourceType type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        String valueType = metadata.getValueType();
        BigDecimal value = null;

        if (valueType.equals(DatatypeType.DECIMAL.getId())) {
            value = this.decodeNode(((XdmNode) item), DecimalTypeImpl.class).getValue();
        } else if (valueType.equals(DatatypeType.INTEGER.getId())) {
            value = BigDecimal.valueOf(this.decodeNode(((XdmNode) item), IntegerTypeImpl.class).getValue());
        } else if (valueType.equals(DatatypeType.POSITIVE_INT.getId())) {
            value = new BigDecimal(this.decodeNode(((XdmNode) item), PositiveIntTypeImpl.class).getValue());
        } else if (valueType.equals(DatatypeType.UNSIGNED_INT.getId())) {
            value = new BigDecimal(this.decodeNode(((XdmNode) item), UnsignedIntTypeImpl.class).getValue());
        } else {
            super.buildNumberSearchParam(resource, type, searchParams, name, metadata, item);
        }

        searchParams.put(new MultiKey<>(name, value), new NumberSearchParamImpl(resource, name, value));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void buildQuantitySearchParam(FhirResource resource, ResourceType type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        Quantity quantity = null;
        String valueType = metadata.getValueType(), units = null;
        URI codeSystemUri = null;
        BigDecimal value = null;

        if (valueType.equals(DatatypeType.AGE.getId())) {
            quantity = this.decodeNode(((XdmNode) item), AgeImpl.class);
        } else if (valueType.equals(DatatypeType.COUNT.getId())) {
            quantity = this.decodeNode(((XdmNode) item), CountImpl.class);
        } else if (valueType.equals(DatatypeType.DISTANCE.getId())) {
            quantity = this.decodeNode(((XdmNode) item), DistanceImpl.class);
        } else if (valueType.equals(DatatypeType.DURATION.getId())) {
            quantity = this.decodeNode(((XdmNode) item), DurationImpl.class);
        } else if (valueType.equals(DatatypeType.MONEY.getId())) {
            quantity = this.decodeNode(((XdmNode) item), MoneyImpl.class);
        } else if (valueType.equals(DatatypeType.SIMPLE_QUANTITY.getId())) {
            quantity = this.decodeNode(((XdmNode) item), SimpleQuantityImpl.class);
        } else if (valueType.equals(DatatypeType.QUANTITY.getId())) {
            quantity = this.decodeNode(((XdmNode) item), QuantityImpl.class);
        }

        if (quantity != null) {
            codeSystemUri = (quantity.hasSystem() ? URI.create(quantity.getSystem().getValue()) : null);
            units = (quantity.hasCode() ? quantity.getCode().getValue() : null);
            value = quantity.getValue().getValue();
        } else {
            super.buildReferenceSearchParam(resource, type, searchParams, name, metadata, item);
        }

        searchParams.put(new MultiKey<>(ArrayUtils.toArray(name, codeSystemUri, null, units, null, value)), new QuantitySearchParamImpl(resource, name,
            codeSystemUri, null, units, null, value));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void buildReferenceSearchParam(FhirResource resource, ResourceType type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        String valueType = metadata.getValueType(), value = null;

        if (valueType.equals(DatatypeType.REFERENCE.getId())) {
            value = this.decodeNode(((XdmNode) item), ReferenceImpl.class).getReference().getValue();
        } else {
            super.buildReferenceSearchParam(resource, type, searchParams, name, metadata, item);
        }

        searchParams.put(new MultiKey<>(name, value), new RefSearchParamImpl(resource, name, value));
    }

    @Override
    protected void buildStringSearchParam(FhirResource resource, ResourceType type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        String valueType = metadata.getValueType(), value = null;

        if (valueType.equals(DatatypeType.CODE.getId())) {
            value = this.decodeNode(((XdmNode) item), CodeTypeImpl.class).getValue();
        } else if (valueType.equals(DatatypeType.ID.getId())) {
            value = this.decodeNode(((XdmNode) item), IdTypeImpl.class).getValue();
        } else if (valueType.equals(DatatypeType.STRING.getId())) {
            value = this.decodeNode(((XdmNode) item), StringTypeImpl.class).getValue();
        } else {
            super.buildStringSearchParam(resource, type, searchParams, name, metadata, item);
        }

        searchParams.put(new MultiKey<>(name, value), new StringSearchParamImpl(resource, name, value));
    }

    @Override
    protected void buildTokenSearchParam(FhirResource resource, ResourceType type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        String valueType = metadata.getValueType(), codeSystemVersion, displayName, value;
        URI codeSystemUri;
        List<Coding> codings = null;
        boolean built = false;

        if (valueType.equals(DatatypeType.BOOLEAN.getId())) {
            SpecialValueType specialValue =
                (this.decodeNode(((XdmNode) item), BooleanTypeImpl.class).getValue() ? SpecialValueType.TRUE : SpecialValueType.FALSE);

            searchParams.put(new MultiKey<>(name, (codeSystemUri = specialValue.getCodeSystemUri()), (codeSystemVersion = specialValue.getCodeSystemVersion()),
                (displayName = specialValue.getName()), (value = specialValue.getId())), new TokenSearchParamImpl(resource, name, codeSystemUri,
                codeSystemVersion, displayName, value));

            built = true;
        } else if (valueType.equals(DatatypeType.CODE.getId())) {
            searchParams.put(new MultiKey<>(name, null, null, null, (value = this.decodeNode(((XdmNode) item), CodeTypeImpl.class).getValue())),
                new TokenSearchParamImpl(resource, name, null, null, null, value));

            built = true;
        } else if (valueType.equals(DatatypeType.CODEABLE_CONCEPT.getId())) {
            codings = this.decodeNode(((XdmNode) item), CodeableConceptImpl.class).getCoding();
        } else if (valueType.equals(DatatypeType.CODING.getId())) {
            codings = Collections.singletonList(this.decodeNode(((XdmNode) item), CodingImpl.class));
        } else if (valueType.equals(DatatypeType.CONTACT_POINT.getId())) {
            ContactPoint contactPoint = this.decodeNode(((XdmNode) item), ContactPointImpl.class);
            boolean contactPointSystemAvailable = contactPoint.hasSystem();
            ContactPointSystem contactPointSystem = (contactPointSystemAvailable ? contactPoint.getSystem().getValue() : null);

            searchParams.put(new MultiKey<>(name, (codeSystemUri = (contactPointSystemAvailable ? contactPointSystem.getCodeSystemUri() : null)),
                (codeSystemVersion = (contactPointSystemAvailable ? contactPointSystem.getCodeSystemVersion() : null)), null, (value =
                    contactPoint.getValue().getValue())), new TokenSearchParamImpl(resource, name, codeSystemUri, codeSystemVersion, null, value));

            built = true;
        } else if (valueType.equals(DatatypeType.IDENTIFIER.getId())) {
            Identifier identifier = this.decodeNode(((XdmNode) item), IdentifierImpl.class);

            searchParams.put(new MultiKey<>(name, (codeSystemUri = (identifier.hasSystem() ? URI.create(identifier.getSystem().getValue()) : null)), null,
                null, (value = identifier.getValue().getValue())), new TokenSearchParamImpl(resource, name, codeSystemUri, null, null, value));

            built = true;
        }

        if (!built && !CollectionUtils.isEmpty(codings)) {
            for (Coding coding : codings) {
                searchParams.put(new MultiKey<>(name, (codeSystemUri = (coding.hasSystem() ? URI.create(coding.getSystem().getValue()) : null)),
                    (codeSystemVersion = (coding.hasVersion() ? coding.getVersion().getValue() : null)), (displayName =
                        (coding.hasDisplay() ? coding.getDisplay().getValue() : null)), (value = coding.getCode().getValue())), new TokenSearchParamImpl(
                    resource, name, codeSystemUri, codeSystemVersion, displayName, value));
            }

            built = true;
        }

        if (!built) {
            super.buildTokenSearchParam(resource, type, searchParams, name, metadata, item);
        }
    }

    @Override
    protected void buildUriSearchParam(FhirResource resource, ResourceType type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        String valueType = metadata.getValueType();
        URI value = null;

        if (valueType.equals(DatatypeType.URI.getId())) {
            value = URI.create(this.decodeNode(((XdmNode) item), UriTypeImpl.class).getValue());
        } else if (valueType.equals(DatatypeType.STRING.getId())) {
            value = URI.create(this.decodeNode(((XdmNode) item), StringTypeImpl.class).getValue());
        } else {
            super.buildUriSearchParam(resource, type, searchParams, name, metadata, item);
        }

        searchParams.put(new MultiKey<>(name, value), new UriSearchParamImpl(resource, name, value));
    }
}
