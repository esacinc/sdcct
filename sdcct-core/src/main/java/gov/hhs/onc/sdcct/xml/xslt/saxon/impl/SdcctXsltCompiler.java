package gov.hhs.onc.sdcct.xml.xslt.saxon.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonProcessor;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctPullEventSource;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import gov.hhs.onc.sdcct.xml.SdcctXmlResolver;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlInputFactory;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.xslt.StaticXsltOptions;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.evpull.StaxToEventBridge;
import net.sf.saxon.functions.FunctionLibraryList;
import net.sf.saxon.functions.IntegratedFunctionLibrary;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.style.Compilation;
import net.sf.saxon.trans.CompilerInfo;
import net.sf.saxon.trans.XPathException;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctXsltCompiler extends XsltCompiler {
    public final static String BEAN_NAME = "xsltCompiler";

    public final static String COMPILE_METHOD_NAME = "compile";

    @Autowired
    private SdcctXmlInputFactory xmlInFactory;

    public SdcctXsltCompiler(SdcctSaxonProcessor proc) {
        super(proc);
    }

    public SdcctXsltExecutable compile(XdmDocument srcDoc) throws SaxonApiException {
        return this.compile(srcDoc, null);
    }

    @Override
    public SdcctXsltExecutable compile(Source src) throws SaxonApiException {
        return this.compile(src, null);
    }

    public SdcctXsltExecutable compile(XdmDocument srcDoc, @Nullable StaticXsltOptions staticOpts) throws SaxonApiException {
        return this.compile(srcDoc.getUnderlyingNode(), staticOpts);
    }

    @SuppressWarnings({ CompilerWarnings.DEPRECATION })
    public SdcctXsltExecutable compile(Source src, @Nullable StaticXsltOptions staticOpts) throws SaxonApiException {
        SdcctSaxonProcessor proc = this.getProcessor();
        SdcctSaxonConfiguration config = proc.getUnderlyingConfiguration();
        CompilerInfo compilerInfo = this.getUnderlyingCompilerInfo();

        if (staticOpts != null) {
            if (staticOpts.hasFunctions()) {
                IntegratedFunctionLibrary staticFuncLib = new IntegratedFunctionLibrary();

                staticOpts.getFunctions().forEach(staticFuncLib::registerFunction);

                FunctionLibraryList funcLibs = ((FunctionLibraryList) compilerInfo.getExtensionFunctionLibrary());

                if (funcLibs == null) {
                    compilerInfo.setExtensionFunctionLibrary((funcLibs = new FunctionLibraryList()));
                }

                funcLibs.addFunctionLibrary(staticFuncLib);
            }

            if (staticOpts.hasVariables()) {
                Map<QName, XdmValue> staticVars = staticOpts.getVariables();

                for (QName staticVarQname : staticVars.keySet()) {
                    compilerInfo.setParameter(staticVarQname.getStructuredQName(), staticVars.get(staticVarQname).getUnderlyingValue());
                }
            }

            if (staticOpts.hasPooledDocuments()) {
                SdcctXmlResolver xmlResolver = ((SdcctXmlResolver) compilerInfo.getURIResolver()).clone();

                // noinspection ConstantConditions
                staticOpts.getPooledDocuments().forEach(
                    pooledDoc -> xmlResolver.addStaticSource(null, null, pooledDoc.getDocumentUri().getUri(), ((ByteArraySource) pooledDoc.getSource())));

                compilerInfo.setURIResolver(xmlResolver);
            }
        }

        if (src instanceof StreamSource) {
            StaxToEventBridge srcEventIterator = new StaxToEventBridge();
            srcEventIterator.setPipelineConfiguration(config.makePipelineConfiguration());

            try {
                srcEventIterator.setXMLStreamReader(this.xmlInFactory.createXMLStreamReader(src));
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }

            src = new SdcctPullEventSource(SdcctTransformUtils.getPublicId(src), src.getSystemId(), srcEventIterator);
        }

        try {
            return new SdcctXsltExecutable(proc, compilerInfo, staticOpts, Compilation.compileSingletonPackage(config, compilerInfo, src));
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    @Override
    public SdcctSaxonProcessor getProcessor() {
        return ((SdcctSaxonProcessor) super.getProcessor());
    }
}
