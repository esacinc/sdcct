package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.form.impl.AbstractFormWebService;
import gov.hhs.onc.sdcct.rfd.RfdFormWebService;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRfdFormWebService<T extends FormService> extends AbstractFormWebService<T> implements RfdFormWebService<T> {
    @Resource
    protected WebServiceContext wsContext;

    @Autowired
    protected XmlCodec xmlCodec;
}
