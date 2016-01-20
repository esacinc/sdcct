package gov.hhs.onc.sdcct.form.impl;

import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.impl.QuestionnaireImpl;
import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.sdc.PackageType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignPkgTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.FormPackageModulesImpl;
import gov.hhs.onc.sdcct.sdc.impl.FormPackageTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.PackageModulesTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.PackageTypeImpl;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import javax.annotation.Nullable;
import javax.xml.transform.dom.DOMResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FormImpl implements Form {
    @Autowired
    private XmlCodec xmlCodec;

    private String id;
    private String name;
    private ResourceSource formDesignSrc;
    private ResourceSource questionnaireSrc;
    private PackageType pkg;
    private Element pkgElem;
    private Questionnaire questionnaire;

    public FormImpl(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.isSetFormDesignSource()) {
            this.pkgElem = ((Document) this.xmlCodec.encode(
                (this.pkg = new PackageTypeImpl().setPackageModules(new PackageModulesTypeImpl()
                    .setMainFormPackage(new FormPackageTypeImpl().setFormPackageModules(new FormPackageModulesImpl().setFormDesignPkg(
                        new FormDesignPkgTypeImpl().setFormDesignTemplate(this.xmlCodec.decode(this.formDesignSrc, FormDesignTypeImpl.class))))))),
                new DOMResult()).getNode()).getDocumentElement();
        }

        if (this.isSetQuestionnaireSource()) {
            this.questionnaire = this.xmlCodec.decode(this.questionnaireSrc, QuestionnaireImpl.class);
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
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
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
    public boolean isSetPackageElement() {
        return (this.pkgElem != null);
    }

    @Nullable
    @Override
    public Element getPackageElement() {
        return this.pkgElem;
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
