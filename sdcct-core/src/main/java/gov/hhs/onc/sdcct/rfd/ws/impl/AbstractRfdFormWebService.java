package gov.hhs.onc.sdcct.rfd.ws.impl;

import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.form.ws.impl.AbstractFormWebService;
import gov.hhs.onc.sdcct.rfd.ws.RfdFormWebService;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRfdFormWebService<T extends FormService> extends AbstractFormWebService<T> implements RfdFormWebService<T> {
    @Resource
    protected WebServiceContext wsContext;

    @Autowired
    protected SdcctConfiguration config;

    @Autowired
    protected XmlCodec xmlCodec;
}
