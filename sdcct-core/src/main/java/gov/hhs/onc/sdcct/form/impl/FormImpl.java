package gov.hhs.onc.sdcct.form.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.impl.AbstractNamedBean;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.PackageType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignPkgTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.FormPackageModulesImpl;
import gov.hhs.onc.sdcct.sdc.impl.FormPackageTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.PackageModulesTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.PackageTypeImpl;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class FormImpl extends AbstractNamedBean implements Form {
    @Autowired
    private Jaxb2Marshaller jaxbMarshaller;

    private ResourceSource formDesignSrc;
    private ResourceSource questionnaireSrc;
    private PackageType pkg;
    private Questionnaire questionnaire;

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void afterPropertiesSet() throws Exception {
        if (this.isSetFormDesignSource()) {
            this.pkg =
                new PackageTypeImpl().setPackageModules(new PackageModulesTypeImpl().setMainFormPackage(new FormPackageTypeImpl()
                    .setFormPackageModules(new FormPackageModulesImpl().setFormDesignPkg(new FormDesignPkgTypeImpl()
                        .setFormDesignTemplate(((JAXBElement<FormDesignType>) this.jaxbMarshaller.unmarshal(this.formDesignSrc)).getValue())))));
        }

        if (this.isSetQuestionnaireSource()) {
            this.questionnaire = ((JAXBElement<Questionnaire>) this.jaxbMarshaller.unmarshal(this.questionnaireSrc)).getValue();
        }
    }

    @Override
    public boolean isSetFormDesignSource() {
        return (this.formDesignSrc != null);
    }

    @Nullable
    @Override
    public ResourceSource getFormDesignSource() {
        return this.formDesignSrc;
    }

    @Override
    public void setFormDesignSource(@Nullable ResourceSource formDesignSrc) {
        this.formDesignSrc = formDesignSrc;
    }

    @Override
    public boolean isSetPackage() {
        return (this.pkg != null);
    }

    @Nullable
    @Override
    public PackageType getPackage() {
        return this.pkg;
    }

    @Override
    public boolean isSetQuestionnaire() {
        return (this.questionnaire != null);
    }

    @Nullable
    @Override
    public Questionnaire getQuestionnaire() {
        return this.questionnaire;
    }

    public void setQuestionnaire(@Nullable Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    @Override
    public boolean isSetQuestionnaireSource() {
        return (this.questionnaireSrc != null);
    }

    @Nullable
    @Override
    public ResourceSource getQuestionnaireSource() {
        return this.questionnaireSrc;
    }

    @Override
    public void setQuestionnaireSource(@Nullable ResourceSource questionnaireSrc) {
        this.questionnaireSrc = questionnaireSrc;
    }
}
