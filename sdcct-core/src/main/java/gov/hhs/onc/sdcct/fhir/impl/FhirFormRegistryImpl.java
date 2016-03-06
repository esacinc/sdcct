package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.search.SearchParamNames;
import gov.hhs.onc.sdcct.data.search.impl.DateSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.StringSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.TokenSearchParamImpl;
import gov.hhs.onc.sdcct.fhir.Coding;
import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.fhir.FhirFormDao;
import gov.hhs.onc.sdcct.fhir.FhirFormDataService;
import gov.hhs.onc.sdcct.fhir.FhirFormRegistry;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.QuestionnaireGroup;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

@Component("registryFormFhir")
public class FhirFormRegistryImpl extends AbstractFhirResourceRegistry<Questionnaire, FhirForm, FhirFormDao, FhirFormDataService> implements FhirFormRegistry {
    private final static String QUESTIONNAIRE_STATUS_CODE_SYSTEM_URI_STR = "http://hl7.org/fhir/ValueSet/questionnaire-status";

    public FhirFormRegistryImpl() {
        super(Questionnaire.class, QuestionnaireImpl.class, FhirForm.class, FhirFormImpl.class, FhirFormImpl::new);
    }

    protected static List<Coding> buildQuestionnaireGroupConcepts(List<Coding> groupConcepts, QuestionnaireGroup group) {
        groupConcepts.addAll(group.getConcept());

        group.getGroup().stream().forEach(nestedGroup -> buildQuestionnaireGroupConcepts(groupConcepts, nestedGroup));

        return groupConcepts;
    }

    @Override
    protected FhirForm buildSearchParams(Questionnaire bean, FhirForm entity, long entityId) throws Exception {
        super.buildSearchParams(bean, entity, entityId);

        QuestionnaireGroup group = bean.getGroup();

        buildQuestionnaireGroupConcepts(new ArrayList<>(), group).stream().forEach(
            groupConcept -> entity.addTokenSearchParams(new TokenSearchParamImpl(entityId, SearchParamNames.CODE, groupConcept.getSystem().getValue(),
                groupConcept.getCode().getValue())));

        if (bean.hasDate()) {
            entity.addDateSearchParams(new DateSearchParamImpl(entityId, SearchParamNames.DATE, DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.parse(bean
                .getDate().getValue())));
        }

        if (bean.hasIdentifier()) {
            bean.getIdentifier()
                .stream()
                .forEach(
                    identifier -> entity.addStringSearchParams(new StringSearchParamImpl(entityId, SearchParamNames.IDENTIFIER, identifier.getValue()
                        .getValue())));
        }

        if (bean.hasPublisher()) {
            entity.addStringSearchParams(new StringSearchParamImpl(entityId, SearchParamNames.PUBLISHER, bean.getPublisher().getValue()));
        }

        if (bean.hasStatus()) {
            entity.addTokenSearchParams(new TokenSearchParamImpl(entityId, SearchParamNames.STATUS, QUESTIONNAIRE_STATUS_CODE_SYSTEM_URI_STR, bean.getStatus()
                .getValue().value()));
        }

        if (group.hasTitle()) {
            entity.addStringSearchParams(new StringSearchParamImpl(entityId, SearchParamNames.TITLE, group.getTitle().getValue()));
        }

        if (bean.hasVersion()) {
            entity.addStringSearchParams(new StringSearchParamImpl(entityId, SearchParamNames.VERSION, bean.getVersion().getValue()));
        }

        return entity;
    }

    @Override
    protected FhirForm encode(Questionnaire bean) throws Exception {
        FhirForm entity = super.encode(bean);
        entity.setId(bean.getId().getValue());

        return entity;
    }
}
