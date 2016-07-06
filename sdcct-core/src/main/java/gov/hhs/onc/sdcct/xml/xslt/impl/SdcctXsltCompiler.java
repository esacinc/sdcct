package gov.hhs.onc.sdcct.xml.xslt.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.transform.impl.SdcctProcessor;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlInputFactory;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.xslt.StaticXsltOptions;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.functions.FunctionLibraryList;
import net.sf.saxon.functions.IntegratedFunctionLibrary;
import net.sf.saxon.lib.AugmentedSource;
import net.sf.saxon.lib.ParseOptions;
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

    public SdcctXsltCompiler(SdcctProcessor proc) {
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
        return this.compile(srcDoc.getSource(), staticOpts);
    }

    public SdcctXsltExecutable compile(Source src, @Nullable StaticXsltOptions staticOpts) throws SaxonApiException {
        SdcctProcessor proc = this.getProcessor();
        SdcctConfiguration config = proc.getUnderlyingConfiguration();
        CompilerInfo compilerInfo = this.getUnderlyingCompilerInfo();
        ParseOptions parseOpts = new ParseOptions(config.getParseOptions());

        if (staticOpts != null) {
            if (staticOpts.hasFunctions()) {
                IntegratedFunctionLibrary staticFuncLib = new IntegratedFunctionLibrary();

                staticOpts.getFunctions().forEach(staticFuncLib::registerFunction);

                ((FunctionLibraryList) compilerInfo.getExtensionFunctionLibrary()).addFunctionLibrary(staticFuncLib);
            }

            if (staticOpts.hasVariables()) {
                Map<QName, XdmValue> staticVars = staticOpts.getVariables();

                for (QName staticVarQname : staticVars.keySet()) {
                    compilerInfo.setParameter(staticVarQname.getStructuredQName(), staticVars.get(staticVarQname).getUnderlyingValue());
                }
            }

            // TODO: Build/use document pool instead.
            if (staticOpts.hasPooledDocuments()) {
                compilerInfo.setURIResolver(
                    (href, base) -> staticOpts.getPooledDocuments().stream().filter(pooledDoc -> pooledDoc.getDocumentUri().toString().equals(href)).findFirst()
                        .map(pooledDoc -> new AugmentedSource(pooledDoc.getSource(), parseOpts)).orElse(null));
            }
        }

        if (src instanceof StreamSource) {
            try {
                src = new StAXSource(this.xmlInFactory.createXMLStreamReader(src));
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }
        }

        try {
            return new SdcctXsltExecutable(proc, compilerInfo, staticOpts, Compilation.compileSingletonPackage(config, compilerInfo, src));
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    @Override
    public SdcctProcessor getProcessor() {
        return ((SdcctProcessor) super.getProcessor());
    }
}
