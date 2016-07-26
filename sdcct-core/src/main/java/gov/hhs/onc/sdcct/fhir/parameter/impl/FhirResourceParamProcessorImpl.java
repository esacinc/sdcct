package gov.hhs.onc.sdcct.fhir.parameter.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.beans.StaticValueSetBean;
import gov.hhs.onc.sdcct.beans.StaticValueSetComponentBean;
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
import gov.hhs.onc.sdcct.fhir.Age;
import gov.hhs.onc.sdcct.fhir.BooleanType;
import gov.hhs.onc.sdcct.fhir.CodeType;
import gov.hhs.onc.sdcct.fhir.CodeableConcept;
import gov.hhs.onc.sdcct.fhir.Coding;
import gov.hhs.onc.sdcct.fhir.ContactPoint;
import gov.hhs.onc.sdcct.fhir.ContactPointSystem;
import gov.hhs.onc.sdcct.fhir.Count;
import gov.hhs.onc.sdcct.fhir.DateTimeType;
import gov.hhs.onc.sdcct.fhir.DateType;
import gov.hhs.onc.sdcct.fhir.DecimalType;
import gov.hhs.onc.sdcct.fhir.Distance;
import gov.hhs.onc.sdcct.fhir.Duration;
import gov.hhs.onc.sdcct.fhir.Element;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.IdType;
import gov.hhs.onc.sdcct.fhir.Identifier;
import gov.hhs.onc.sdcct.fhir.InstantType;
import gov.hhs.onc.sdcct.fhir.IntegerType;
import gov.hhs.onc.sdcct.fhir.Money;
import gov.hhs.onc.sdcct.fhir.Period;
import gov.hhs.onc.sdcct.fhir.PositiveIntType;
import gov.hhs.onc.sdcct.fhir.Quantity;
import gov.hhs.onc.sdcct.fhir.Reference;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.SpecialValueType;
import gov.hhs.onc.sdcct.fhir.StringType;
import gov.hhs.onc.sdcct.fhir.Timing;
import gov.hhs.onc.sdcct.fhir.TimingRepeat;
import gov.hhs.onc.sdcct.fhir.UnsignedIntType;
import gov.hhs.onc.sdcct.fhir.UriType;
import gov.hhs.onc.sdcct.fhir.impl.FhirResourceImpl;
import gov.hhs.onc.sdcct.fhir.impl.ResourceImpl;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.parameter.FhirResourceParamProcessor;
import gov.hhs.onc.sdcct.utils.SdcctDateFormatUtils;
import gov.hhs.onc.sdcct.utils.SdcctDateFormatUtils.ParsedDate;
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
        Object itemValue = this.decodeNode(((XdmNode) item));
        ParsedDate parsedValue;
        Date startValue = null, endValue = null;
        Period period = null;

        if (itemValue instanceof DateType) {
            startValue = (parsedValue = SdcctDateFormatUtils.parse(((DateType) itemValue).getValue(), null, Locale.ROOT)).getStartValue();
            endValue = parsedValue.getEndValue();
        } else if (itemValue instanceof DateTimeType) {
            startValue = (parsedValue = SdcctDateFormatUtils.parse(((DateTimeType) itemValue).getValue(), null, Locale.ROOT)).getStartValue();
            endValue = parsedValue.getEndValue();
        } else if (itemValue instanceof InstantType) {
            startValue = endValue = ((InstantType) itemValue).getValue().toGregorianCalendar().getTime();
        } else if (itemValue instanceof Period) {
            period = ((Period) itemValue);
        } else if (itemValue instanceof Timing) {
            Timing timing = ((Timing) itemValue);

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
            startValue = (period.hasStart() ? SdcctDateFormatUtils.parse(period.getStart().getValue(), null, Locale.ROOT).getValue() : null);
            endValue = (period.hasEnd() ? SdcctDateFormatUtils.parse(period.getEnd().getValue(), null, Locale.ROOT).getValue() : null);
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
        Object itemValue = this.decodeNode(((XdmNode) item));
        BigDecimal value = null;

        if (itemValue instanceof DecimalType) {
            value = ((DecimalType) itemValue).getValue();
        } else if (itemValue instanceof IntegerType) {
            value = BigDecimal.valueOf(((IntegerType) itemValue).getValue());
        } else if (itemValue instanceof PositiveIntType) {
            value = new BigDecimal(((PositiveIntType) itemValue).getValue());
        } else if (itemValue instanceof UnsignedIntType) {
            value = new BigDecimal(((UnsignedIntType) itemValue).getValue());
        } else {
            super.buildNumberResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        resourceParams.put(new MultiKey<>(name, value), new NumberResourceParamImpl(entity, name, value));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void buildQuantityResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        Object itemValue = this.decodeNode(((XdmNode) item));
        Quantity quantity = null;
        String units = null;
        URI codeSystemUri = null;
        BigDecimal value = null;

        if (itemValue instanceof Age) {
            quantity = ((Age) itemValue);
        } else if (itemValue instanceof Count) {
            quantity = ((Count) itemValue);
        } else if (itemValue instanceof Distance) {
            quantity = ((Distance) itemValue);
        } else if (itemValue instanceof Duration) {
            quantity = ((Duration) itemValue);
        } else if (itemValue instanceof Money) {
            quantity = ((Money) itemValue);
        } else if (itemValue instanceof Quantity) {
            quantity = ((Quantity) itemValue);
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
        Object itemValue = this.decodeNode(((XdmNode) item));
        String value = null;

        if (itemValue instanceof Reference) {
            value = ((Reference) itemValue).getReference().getValue();
        } else {
            super.buildReferenceResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        resourceParams.put(new MultiKey<>(name, value), new RefResourceParamImpl(entity, name, value));
    }

    @Override
    protected void buildStringResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        Object itemValue = this.decodeNode(((XdmNode) item));
        String value = null;

        if (itemValue instanceof CodeType) {
            value = ((CodeType) itemValue).getValue();
        } else if (itemValue instanceof IdType) {
            value = ((IdType) itemValue).getValue();
        } else if (itemValue instanceof StringType) {
            value = ((StringType) itemValue).getValue();
        } else {
            super.buildStringResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        resourceParams.put(new MultiKey<>(name, value), new StringResourceParamImpl(entity, name, value));
    }

    @Override
    protected void buildTokenResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata resourceParamMetadata, XdmItem item, FhirResource entity) throws Exception {
        Object itemValue = this.decodeNode(((XdmNode) item));
        String codeSystemVersion, displayName, value;
        URI codeSystemUri;
        List<Coding> codings = null;
        boolean built = false;

        if (itemValue instanceof BooleanType) {
            SpecialValueType specialValue = (((BooleanType) itemValue).getValue() ? SpecialValueType.TRUE : SpecialValueType.FALSE);

            resourceParams.put(
                new MultiKey<>(name, (codeSystemUri = specialValue.getCodeSystemUri()), (codeSystemVersion = specialValue.getCodeSystemVersion()),
                    (displayName = specialValue.getName()), (value = specialValue.getId())),
                new TokenResourceParamImpl(entity, false, name, codeSystemUri, codeSystemVersion, displayName, value));

            built = true;
        } else if (itemValue instanceof CodeType) {
            resourceParams.put(new MultiKey<>(name, null, null, null, (value = ((CodeType) itemValue).getValue())),
                new TokenResourceParamImpl(entity, false, name, null, null, null, value));

            built = true;
        } else if (itemValue instanceof CodeableConcept) {
            codings = ((CodeableConcept) itemValue).getCoding();
        } else if (itemValue instanceof Coding) {
            codings = Collections.singletonList(((Coding) itemValue));
        } else if (itemValue instanceof ContactPoint) {
            ContactPoint contactPoint = ((ContactPoint) itemValue);
            boolean contactPointSystemAvailable = contactPoint.hasSystem();
            ContactPointSystem contactPointSystem = (contactPointSystemAvailable ? contactPoint.getSystem().getValue() : null);

            resourceParams.put(
                new MultiKey<>(name, (codeSystemUri = (contactPointSystemAvailable ? contactPointSystem.getCodeSystemUri() : null)),
                    (codeSystemVersion = (contactPointSystemAvailable ? contactPointSystem.getCodeSystemVersion() : null)), null,
                    (value = contactPoint.getValue().getValue())),
                new TokenResourceParamImpl(entity, false, name, codeSystemUri, codeSystemVersion, null, value));

            built = true;
        } else if (itemValue instanceof Identifier) {
            Identifier identifier = ((Identifier) itemValue);

            resourceParams.put(new MultiKey<>(name, (codeSystemUri = (identifier.hasSystem() ? URI.create(identifier.getSystem().getValue()) : null)), null,
                null, (value = identifier.getValue().getValue())), new TokenResourceParamImpl(entity, false, name, codeSystemUri, null, null, value));

            built = true;
        } else if (itemValue instanceof StaticValueSetComponentBean) {
            StaticValueSetBean staticValueSetValue = ((StaticValueSetComponentBean) itemValue).getValue();

            resourceParams.put(
                new MultiKey<>(name, (codeSystemUri = staticValueSetValue.getCodeSystemUri()), (codeSystemVersion = staticValueSetValue.getCodeSystemVersion()),
                    (displayName = staticValueSetValue.getName()), (value = staticValueSetValue.getId())),
                new TokenResourceParamImpl(entity, false, name, codeSystemUri, codeSystemVersion, displayName, value));

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
        Object itemValue = this.decodeNode(((XdmNode) item));
        URI value = null;

        if (itemValue instanceof UriType) {
            value = URI.create(((UriType) itemValue).getValue());
        } else if (itemValue instanceof StringType) {
            value = URI.create(((StringType) itemValue).getValue());
        } else {
            super.buildUriResourceParam(type, resourceParams, name, resourceParamMetadata, item, entity);
        }

        resourceParams.put(new MultiKey<>(name, value), new UriResourceParamImpl(entity, false, name, value));
    }
}
