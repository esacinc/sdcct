package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.criteria.utils.SdcctCriterionUtils;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamNames;
import gov.hhs.onc.sdcct.fhir.form.FhirForm;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.QuestionnaireStatus;
import gov.hhs.onc.sdcct.fhir.impl.IdentifierImpl;
import gov.hhs.onc.sdcct.fhir.impl.QuestionnaireImpl;
import gov.hhs.onc.sdcct.fhir.impl.StringTypeImpl;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.transform.all", "sdcct.test.unit.validate.all" },
    groups = { "sdcct.test.unit.data.all", "sdcct.test.unit.data.db" })
public class DbUnitTests extends AbstractSdcctUnitTests {
    private final static String NEW_TEST_FORM_IDENTIFIER = "fhir_test_new";

    @Resource(name = "formFhira")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirForm testForm;

    @Resource(name = "resourceRegistryFhirQuestionnaire")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirResourceRegistry<Questionnaire> registry;

    private String testFormIdentifier;
    private Long testFormEntityId;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Test(dependsOnMethods = { "testFind", "testSave" })
    public void testRemove() throws Exception {
        this.registry.remove(
            this.registry.buildCriteria(SdcctCriterionUtils.matchParam(DbPropertyNames.TOKEN_PARAMS, ResourceParamNames.IDENTIFIER, NEW_TEST_FORM_IDENTIFIER)));

        Assert.assertFalse(
            this.registry.exists(this.registry.buildCriteria(SdcctCriterionUtils.<FhirResource> matchDeleted().not(),
                SdcctCriterionUtils.matchParam(DbPropertyNames.TOKEN_PARAMS, ResourceParamNames.IDENTIFIER, NEW_TEST_FORM_IDENTIFIER))),
            String.format("New test form (identifier=%s) still exists.", NEW_TEST_FORM_IDENTIFIER));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Test(dependsOnMethods = { "testFind" })
    public void testSave() throws Exception {
        // noinspection ConstantConditions
        Questionnaire testFormBean =
            this.registry.findBean(this.registry
                .buildCriteria(SdcctCriterionUtils.matchParam(DbPropertyNames.TOKEN_PARAMS, ResourceParamNames.IDENTIFIER, this.testFormIdentifier))),
            newTestFormBean =
                new QuestionnaireImpl().setItems(testFormBean.getItems()).setStatus(testFormBean.getStatus()).setVersion(testFormBean.getVersion());
        newTestFormBean.addIdentifiers(new IdentifierImpl().setValue(new StringTypeImpl().setValue(NEW_TEST_FORM_IDENTIFIER)));

        FhirResource newTestFormEntity = this.registry.saveBean(newTestFormBean);
        Long newTestFormEntityId = newTestFormEntity.getEntityId(), newTestFormVersion = newTestFormEntity.getVersion();

        // noinspection ConstantConditions
        Assert.assertNotEquals(this.testFormEntityId, newTestFormEntityId,
            String.format("New test form (identifier=%s) entity ID matches that of existing test form (identifier=%s): {%s} == {%s}", NEW_TEST_FORM_IDENTIFIER,
                this.testFormIdentifier, newTestFormEntityId, this.testFormEntityId));

        newTestFormBean.getStatus().setValue(QuestionnaireStatus.RETIRED);

        FhirResource newTestFormEntityUpdated = this.registry.saveBean(newTestFormBean, newTestFormEntity);
        Long newTestFormUpdatedEntityId = newTestFormEntityUpdated.getEntityId(), newTestFormUpdatedVersion = newTestFormEntityUpdated.getVersion();

        Assert.assertNotEquals(newTestFormEntityId, newTestFormUpdatedEntityId,
            String.format("Updated new test form (identifier=%s) entity ID matches that of existing new test form (identifier=%s): {%s} == {%s}",
                NEW_TEST_FORM_IDENTIFIER, NEW_TEST_FORM_IDENTIFIER, newTestFormUpdatedEntityId, newTestFormEntityId));

        Assert.assertNotEquals(newTestFormVersion, newTestFormUpdatedVersion,
            String.format("Updated new test form (identifier=%s) version matches that of existing new test form (identifier=%s): {%s} == {%s}",
                NEW_TEST_FORM_IDENTIFIER, NEW_TEST_FORM_IDENTIFIER, newTestFormVersion, newTestFormUpdatedVersion));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Test
    public void testFind() throws Exception {
        Assert.assertTrue(
            this.registry.exists(this.registry.buildCriteria(SdcctCriterionUtils.matchParam(DbPropertyNames.TOKEN_PARAMS, ResourceParamNames.IDENTIFIER,
                (this.testFormIdentifier = this.testForm.getIdentifier())))),
            String.format("Test form (identifier=%s) does not exist.", this.testFormIdentifier));

        FhirResource testForm = this.registry.find(
            this.registry.buildCriteria(SdcctCriterionUtils.matchParam(DbPropertyNames.TOKEN_PARAMS, ResourceParamNames.IDENTIFIER, this.testFormIdentifier)));

        // noinspection ConstantConditions
        this.testFormEntityId = testForm.getEntityId();

        String actualTestFormIdentifier = testForm.getTokenParams().stream()
            .filter(tokenSearchParam -> tokenSearchParam.getName().equals(ResourceParamNames.IDENTIFIER)).findFirst().get().getValue();

        Assert.assertEquals(actualTestFormIdentifier, this.testFormIdentifier,
            String.format("Test form identifiers do not match: %s != %s", actualTestFormIdentifier, this.testFormIdentifier));
    }
}
