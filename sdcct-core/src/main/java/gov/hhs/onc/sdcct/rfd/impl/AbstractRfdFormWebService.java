package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.rfd.RfdFormWebService;
import gov.hhs.onc.sdcct.form.impl.AbstractFormWebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public abstract class AbstractRfdFormWebService<T extends FormService> extends AbstractFormWebService<T> implements RfdFormWebService<T> {
    @Resource
    protected WebServiceContext wsContext;

    @Resource(name = "jaxbMarshaller")
    protected Jaxb2Marshaller jaxbMarshaller;
}
