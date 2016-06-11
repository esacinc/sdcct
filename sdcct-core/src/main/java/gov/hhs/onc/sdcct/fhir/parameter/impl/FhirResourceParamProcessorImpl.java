package gov.hhs.onc.sdcct.fhir.parameter.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamMetadata;
import gov.hhs.onc.sdcct.data.parameter.DatePeriod;
import gov.hhs.onc.sdcct.data.parameter.ResourceParam;
import gov.hhs.onc.sdcct.data.parameter.impl.AbstractResourceParamProcessor;
import gov.hhs.onc.sdcct.data.parameter.impl.DatePeriodImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.DateResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.NumberResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.QuantityResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.RefResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.StringResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.TokenResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.UriResourceParamImpl;
import gov.hhs.onc.sdcct.fhir.Coding;
import gov.hhs.onc.sdcct.fhir.ContactPoint;
import gov.hhs.onc.sdcct.fhir.ContactPointSystem;
import gov.hhs.onc.sdcct.fhir.DatatypeType;
import gov.hhs.onc.sdcct.fhir.Element;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.Identifier;
import gov.hhs.onc.sdcct.fhir.Period;
import gov.hhs.onc.sdcct.fhir.Quantity;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.SpecialValueType;
import gov.hhs.onc.sdcct.fhir.Timing;
import gov.hhs.onc.sdcct.fhir.TimingRepeat;
import gov.hhs.onc.sdcct.fhir.impl.AgeImpl;
import gov.hhs.onc.sdcct.fhir.impl.BooleanTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodeTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodeableConceptImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodingImpl;
import gov.hhs.onc.sdcct.fhir.impl.ContactPointImpl;
import gov.hhs.onc.sdcct.fhir.impl.CountImpl;
import gov.hhs.onc.sdcct.fhir.impl.DateTimeTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.DateTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.DecimalTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.DistanceImpl;
import gov.hhs.onc.sdcct.fhir.impl.DurationImpl;
import gov.hhs.onc.sdcct.fhir.impl.FhirResourceImpl;
import gov.hhs.onc.sdcct.fhir.impl.IdTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.IdentifierImpl;
import gov.hhs.onc.sdcct.fhir.impl.InstantTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.IntegerTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.MoneyImpl;
import gov.hhs.onc.sdcct.fhir.impl.PeriodImpl;
import gov.hhs.onc.sdcct.fhir.impl.PositiveIntTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.QuantityImpl;
import gov.hhs.onc.sdcct.fhir.impl.ReferenceImpl;
import gov.hhs.onc.sdcct.fhir.impl.ResourceImpl;
import gov.hhs.onc.sdcct.fhir.impl.SimpleQuantityImpl;
import gov.hhs.onc.sdcct.fhir.impl.StringTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.TimingImpl;
import gov.hhs.onc.sdcct.fhir.impl.UnsignedIntTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.UriTypeImpl;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.parameter.FhirResourceParamProcessor;
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
import org.springframework.stereotype.Component;

@Component("resourceParamProcFhir")
public class FhirResourceParamProcessorImpl extends AbstractResourceParamProcessor<Resource, FhirResourceMetadata<?>, FhirResource>
    implements FhirResourceParamProcessor {
    public FhirResourceParamProcessorImpl() {
        super(SpecificationType.FHIR, Resource.class, ResourceImpl.class, FhirResource.class, FhirResourceImpl.class);
    }

    @Override
    protected void buildCompositeResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata metadata, XdmItem item, FhirResource entity) throws Exception {
        // TODO: implement
    }

    @Override
    protected void buildDateResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        String valueType = resourceParamMetadata.getValueType();
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
            super.buildDateResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        DatePeriod value = new DatePeriodImpl(startValue, endValue);

        resourceParams.put(new MultiKey<>(name, value), new DateResourceParamImpl(entity, name, value));
    }

    @Override
    protected void buildNumberResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        String valueType = resourceParamMetadata.getValueType();
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
            super.buildNumberResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        resourceParams.put(new MultiKey<>(name, value), new NumberResourceParamImpl(entity, name, value));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void buildQuantityResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        Quantity quantity = null;
        String valueType = resourceParamMetadata.getValueType(), units = null;
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
            super.buildReferenceResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        resourceParams.put(new MultiKey<>(ArrayUtils.toArray(name, codeSystemUri, null, units, null, value)),
            new QuantityResourceParamImpl(entity, name, codeSystemUri, null, units, null, value));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void buildReferenceResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        String valueType = resourceParamMetadata.getValueType(), value = null;

        if (valueType.equals(DatatypeType.REFERENCE.getId())) {
            value = this.decodeNode(((XdmNode) item), ReferenceImpl.class).getReference().getValue();
        } else {
            super.buildReferenceResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        resourceParams.put(new MultiKey<>(name, value), new RefResourceParamImpl(entity, name, value));
    }

    @Override
    protected void buildStringResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        String valueType = resourceParamMetadata.getValueType(), value = null;

        if (valueType.equals(DatatypeType.CODE.getId())) {
            value = this.decodeNode(((XdmNode) item), CodeTypeImpl.class).getValue();
        } else if (valueType.equals(DatatypeType.ID.getId())) {
            value = this.decodeNode(((XdmNode) item), IdTypeImpl.class).getValue();
        } else if (valueType.equals(DatatypeType.STRING.getId())) {
            value = this.decodeNode(((XdmNode) item), StringTypeImpl.class).getValue();
        } else {
            super.buildStringResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        resourceParams.put(new MultiKey<>(name, value), new StringResourceParamImpl(entity, name, value));
    }

    @Override
    protected void buildTokenResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        String valueType = resourceParamMetadata.getValueType(), codeSystemVersion, displayName, value;
        URI codeSystemUri;
        List<Coding> codings = null;
        boolean built = false;

        if (valueType.equals(DatatypeType.BOOLEAN.getId())) {
            SpecialValueType specialValue =
                (this.decodeNode(((XdmNode) item), BooleanTypeImpl.class).getValue() ? SpecialValueType.TRUE : SpecialValueType.FALSE);

            resourceParams.put(
                new MultiKey<>(name, (codeSystemUri = specialValue.getCodeSystemUri()), (codeSystemVersion = specialValue.getCodeSystemVersion()),
                    (displayName = specialValue.getName()), (value = specialValue.getId())),
                new TokenResourceParamImpl(entity, false, name, codeSystemUri, codeSystemVersion, displayName, value));

            built = true;
        } else if (valueType.equals(DatatypeType.CODE.getId())) {
            resourceParams.put(new MultiKey<>(name, null, null, null, (value = this.decodeNode(((XdmNode) item), CodeTypeImpl.class).getValue())),
                new TokenResourceParamImpl(entity, false, name, null, null, null, value));

            built = true;
        } else if (valueType.equals(DatatypeType.CODEABLE_CONCEPT.getId())) {
            codings = this.decodeNode(((XdmNode) item), CodeableConceptImpl.class).getCoding();
        } else if (valueType.equals(DatatypeType.CODING.getId())) {
            codings = Collections.singletonList(this.decodeNode(((XdmNode) item), CodingImpl.class));
        } else if (valueType.equals(DatatypeType.CONTACT_POINT.getId())) {
            ContactPoint contactPoint = this.decodeNode(((XdmNode) item), ContactPointImpl.class);
            boolean contactPointSystemAvailable = contactPoint.hasSystem();
            ContactPointSystem contactPointSystem = (contactPointSystemAvailable ? contactPoint.getSystem().getValue() : null);

            resourceParams.put(
                new MultiKey<>(name, (codeSystemUri = (contactPointSystemAvailable ? contactPointSystem.getCodeSystemUri() : null)),
                    (codeSystemVersion = (contactPointSystemAvailable ? contactPointSystem.getCodeSystemVersion() : null)), null,
                    (value = contactPoint.getValue().getValue())),
                new TokenResourceParamImpl(entity, false, name, codeSystemUri, codeSystemVersion, null, value));

            built = true;
        } else if (valueType.equals(DatatypeType.IDENTIFIER.getId())) {
            Identifier identifier = this.decodeNode(((XdmNode) item), IdentifierImpl.class);

            resourceParams.put(new MultiKey<>(name, (codeSystemUri = (identifier.hasSystem() ? URI.create(identifier.getSystem().getValue()) : null)), null,
                null, (value = identifier.getValue().getValue())), new TokenResourceParamImpl(entity, false, name, codeSystemUri, null, null, value));

            built = true;
        }

        if (!built && !CollectionUtils.isEmpty(codings)) {
            for (Coding coding : codings) {
                resourceParams.put(
                    new MultiKey<>(name, (codeSystemUri = (coding.hasSystem() ? URI.create(coding.getSystem().getValue()) : null)),
                        (codeSystemVersion = (coding.hasVersion() ? coding.getVersion().getValue() : null)),
                        (displayName = (coding.hasDisplay() ? coding.getDisplay().getValue() : null)), (value = coding.getCode().getValue())),
                    new TokenResourceParamImpl(entity, false, name, codeSystemUri, codeSystemVersion, displayName, value));
            }

            built = true;
        }

        if (!built) {
            super.buildTokenResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }
    }

    @Override
    protected void buildUriResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        String valueType = resourceParamMetadata.getValueType();
        URI value = null;

        if (valueType.equals(DatatypeType.URI.getId())) {
            value = URI.create(this.decodeNode(((XdmNode) item), UriTypeImpl.class).getValue());
        } else if (valueType.equals(DatatypeType.STRING.getId())) {
            value = URI.create(this.decodeNode(((XdmNode) item), StringTypeImpl.class).getValue());
        } else {
            super.buildUriResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        resourceParams.put(new MultiKey<>(name, value), new UriResourceParamImpl(entity, false, name, value));
    }
}
