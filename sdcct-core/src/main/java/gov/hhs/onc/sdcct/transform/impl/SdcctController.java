package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.saxon.Controller;
import net.sf.saxon.expr.instruct.Executable;
import net.sf.saxon.trans.XPathException;

public class SdcctController extends Controller {
    private Map<Object, Object> contextData = new HashMap<>();

    public SdcctController(SdcctConfiguration config) {
        super(config);
    }

    public SdcctController(SdcctConfiguration config, Executable exec) {
        super(config, exec);
    }

    public void registerDocuments(List<XdmDocument> pooledDocs) throws XPathException {
        for (XdmDocument pooledDoc : pooledDocs) {
            this.registerDocument(pooledDoc.getUnderlyingNode().getTreeInfo(), pooledDoc.getUri());
        }
    }

    @Override
    public SdcctConfiguration getConfiguration() {
        return ((SdcctConfiguration) super.getConfiguration());
    }

    public Map<Object, Object> getContextData() {
        return this.contextData;
    }
}
