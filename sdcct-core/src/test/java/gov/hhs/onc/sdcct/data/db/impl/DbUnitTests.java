package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.fhir.FhirFormDataService;
import gov.hhs.onc.sdcct.fhir.FhirFormRegistry;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.impl.DateTimeImpl;
import gov.hhs.onc.sdcct.fhir.impl.IdImpl;
import gov.hhs.onc.sdcct.fhir.impl.QuestionnaireImpl;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all" }, groups = { "sdcct.test.unit.data.all", "sdcct.test.unit.data.db" })
public class DbUnitTests extends AbstractSdcctUnitTests {
    @Value("${sdcct.test.data.db.form.1.id}")
    private String testFormId1;

    @Value("${sdcct.test.data.db.form.2.id}")
    private String testFormId2;

    @Value("${sdcct.test.data.db.search.param.1.name}")
    private String testSearchParamName1;

    @Value("${sdcct.test.data.db.search.param.1.value}")
    private String testSearchParamValue1;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirFormRegistry registry;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirFormDataService dataService;

    private long testFormEntityId1;

    @Test(dependsOnMethods = { "testFind", "testSave" })
    public void testRemove() throws Exception {
        this.dataService.removeByNaturalId(this.testFormId2);

        Assert.assertFalse(this.dataService.existsByNaturalId(this.testFormId2), String.format("Removed test form (id=%s) still exists.", this.testFormId2));
    }

    @Test(dependsOnMethods = { "testFind" })
    public void testSave() throws Exception {
        // noinspection ConstantConditions
        Questionnaire testQuestionnaire1 = this.registry.findByNaturalId(this.testFormId1), testQuestionnaire2 =
            new QuestionnaireImpl().setDate(new DateTimeImpl().setValue(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(System.currentTimeMillis())))
                .setGroup(testQuestionnaire1.getGroup()).setStatus(testQuestionnaire1.getStatus()).setVersion(testQuestionnaire1.getVersion());
        testQuestionnaire2.setId(new IdImpl().setValue(this.testFormId2));
        testQuestionnaire2.setMeta(testQuestionnaire1.getMeta());

        this.registry.save(testQuestionnaire2);

        // noinspection ConstantConditions
        Long testFormEntityId2 = this.dataService.findByNaturalId(this.testFormId2).getEntityId();

        // noinspection ConstantConditions
        Assert.assertNotEquals(this.testFormEntityId1, testFormEntityId2,
            String.format("Re-inserted test form (id=%s) entity IDs match: %d == %d", this.testFormId2, this.testFormEntityId1, testFormEntityId2));
    }

    @Test
    public void testFind() throws Exception {
        Assert.assertTrue(this.dataService.existsByNaturalId(this.testFormId1),
            String.format("Test form (id=%s) does not exist by natural ID.", this.testFormId1));

        FhirForm testForm1 = this.dataService.findByNaturalId(this.testFormId1);

        // noinspection ConstantConditions
        this.testFormEntityId1 = testForm1.getEntityId();

        String actualTestFormId1 = testForm1.getId();

        Assert.assertEquals(actualTestFormId1, this.testFormId1, String.format("Test form IDs do not match: %s != %s", actualTestFormId1, this.testFormId1));

        Assert.assertTrue(this.dataService.exists(this.dataService.buildCriteria().addKeyword(DbPropertyNames.CONTENT, this.testFormId1)),
            String.format("Test form (id=%s) does not exist by criteria.", this.testFormId1));
    }
}
