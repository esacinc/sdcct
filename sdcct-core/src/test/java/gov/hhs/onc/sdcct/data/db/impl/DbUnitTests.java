package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.SdcctRepository;
import gov.hhs.onc.sdcct.data.search.SearchParamNames;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.impl.IdentifierImpl;
import gov.hhs.onc.sdcct.fhir.impl.QuestionnaireImpl;
import gov.hhs.onc.sdcct.fhir.impl.StringTypeImpl;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all" }, groups = { "sdcct.test.unit.data.all", "sdcct.test.unit.data.db" })
public class DbUnitTests extends AbstractSdcctUnitTests {
    @Value("${sdcct.test.data.db.form.1.identifier}")
    private String testFormIdentifier1;

    @Value("${sdcct.test.data.db.form.2.identifier}")
    private String testFormIdentifier2;

    @Value("${sdcct.test.data.db.search.param.1.name}")
    private String testSearchParamName1;

    @Value("${sdcct.test.data.db.search.param.1.value}")
    private String testSearchParamValue1;

    @Resource(name = "registryResourceFhirQuestionnaire")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirResourceRegistry<Questionnaire> registry;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctRepository<FhirResource> repo;

    private Long testFormId1;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Test(dependsOnMethods = { "testFind", "testSave" })
    public void testRemove() throws Exception {
        this.repo.remove(this.registry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
            root.join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), this.testFormIdentifier2)));

        Assert.assertFalse(
            this.repo.exists(this.registry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
                root.join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), this.testFormIdentifier2))),
            String.format("Removed test form (identifier=%s) still exists.", this.testFormIdentifier2));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Test(dependsOnMethods = { "testFind" })
    public void testSave() throws Exception {
        // noinspection ConstantConditions
        Questionnaire testQuestionnaire1 =
            this.registry.find(this.registry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
                root.join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), this.testFormIdentifier1))), testQuestionnaire2 =
            new QuestionnaireImpl().setItems(testQuestionnaire1.getItems()).setStatus(testQuestionnaire1.getStatus())
                .setVersion(testQuestionnaire1.getVersion());
        testQuestionnaire2.addIdentifiers(new IdentifierImpl().setValue(new StringTypeImpl().setValue(this.testFormIdentifier2)));
        testQuestionnaire2.setMeta(testQuestionnaire1.getMeta());

        this.registry.save(testQuestionnaire2);

        // noinspection ConstantConditions
        Long testFormId2 =
            this.repo.find(
                this.registry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
                    root.join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), this.testFormIdentifier2))).getId();

        // noinspection ConstantConditions
        Assert.assertNotEquals(this.testFormId1, testFormId2,
            String.format("Re-inserted test form (identifier=%s) IDs match: %d == %d", this.testFormIdentifier2, this.testFormId1, testFormId2));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Test
    public void testFind() throws Exception {
        Assert.assertTrue(
            this.repo.exists(this.registry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
                root.join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), this.testFormIdentifier1))),
            String.format("Test form (identifier=%s) does not exist by natural ID.", this.testFormIdentifier1));

        FhirResource testForm1 =
            this.repo.find(this.registry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
                root.join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), this.testFormIdentifier1)));

        // noinspection ConstantConditions
        this.testFormId1 = testForm1.getId();

        String actualTestFormIdentifier1 =
            testForm1.getTokenSearchParams().stream().filter(tokenSearchParam -> tokenSearchParam.getName().equals(SearchParamNames.IDENTIFIER)).findFirst()
                .get().getValue();

        Assert.assertEquals(actualTestFormIdentifier1, this.testFormIdentifier1,
            String.format("Test form identifiers do not match: %s != %s", actualTestFormIdentifier1, this.testFormIdentifier1));
    }
}
