package gov.hhs.onc.sdcct.form;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.sdc.PackageType;
import javax.annotation.Nullable;
import org.springframework.beans.factory.InitializingBean;
import org.w3c.dom.Element;

public interface Form extends IdentifiedBean, InitializingBean, NamedBean {
    public boolean isSetFormDesignSource();

    @Nullable
    public ResourceSource getFormDesignSource();

    public void setFormDesignSource(@Nullable ResourceSource formDesignSrc);

    public boolean isSetPackage();

    @Nullable
    public PackageType getPackage();

    public boolean isSetPackageElement();

    @Nullable
    public Element getPackageElement();

    public boolean isSetQuestionnaire();

    @Nullable
    public Questionnaire getQuestionnaire();

    public boolean isSetQuestionnaireSource();

    @Nullable
    public ResourceSource getQuestionnaireSource();

    public void setQuestionnaireSource(@Nullable ResourceSource questionnaireSrc);
}
