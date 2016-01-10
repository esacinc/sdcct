package gov.hhs.onc.sdcct.form.impl;

import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.form.FormWebService;
import org.apache.cxf.Bus;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractFormWebService<T extends FormService> implements FormWebService<T> {
    @Autowired
    protected Bus bus;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected T service;
}
