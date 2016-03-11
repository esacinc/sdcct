package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.tools.xjc.BadCommandLineException
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.Plugin
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.beans.NamedBean
import gov.hhs.onc.sdcct.build.xml.jaxb.CodegenException
import gov.hhs.onc.sdcct.utils.SdcctStringUtils
import javax.xml.namespace.QName
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXException

abstract class AbstractCodegenPlugin extends Plugin implements NamedBean {
    final static String OPT_NAME_PREFIX = "X"
    
    protected Log log
    protected MavenProject project
    protected Map<String, String> bindingVars
    protected String name
    protected String optName
    protected String opt
    protected Set<QName> tagQnames
    protected List<String> tagNsUris

    protected AbstractCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, String name, QName ... tagQnames) {
        this.log = log
        this.project = project
        this.bindingVars = bindingVars
        this.name = name
        this.opt = (SdcctStringUtils.HYPHEN + (this.optName = (OPT_NAME_PREFIX + this.name)))
        this.tagNsUris = (this.tagQnames = tagQnames as LinkedHashSet<QName>).collect{ it.namespaceURI }
    }

    @Override
    boolean run(Outline outline, Options opts, ErrorHandler errorHandler) throws SAXException {
        try {
            this.runInternal(outline, opts, errorHandler)
        } catch (Exception e) {
            throw new CodegenException("Unable to run codegen plugin (class=${this.class.name}, name=${this.name}).", e)
        }
        
        return true
    }
    
    @Override
    void postProcessModel(Model model, ErrorHandler errorHandler) {
        try {
            this.postProcessModelInternal(model, model.options, errorHandler)
        } catch (Exception e) {
            throw new CodegenException("Unable to post-process model using codegen plugin (class=${this.class.name}, name=${this.name}).", e)
        }
    }

    @Override
    void onActivated(Options opts) throws BadCommandLineException {
        try {
            this.onActivatedInternal(opts)
        } catch (Exception e) {
            throw new CodegenException("Unable to activate codegen plugin (class=${this.class.name}, name=${this.name}).", e)
        }
    }

    protected void runInternal(Outline outline, Options opts, ErrorHandler errorHandler) throws Exception {
    }
    
    protected void postProcessModelInternal(Model model, Options opts, ErrorHandler errorHandler) throws Exception {
    }
    
    protected void onActivatedInternal(Options opts) throws Exception {
    }
    
    @Override
    boolean isCustomizationTagName(String tagNsUri, String tagLocalName) {
        return this.tagQnames.any{ ((it.namespaceURI == tagNsUri) && (it.localPart == tagLocalName)) }
    }
    
    @Override
    List<String> getCustomizationURIs() {
        return this.tagNsUris
    }

    @Override
    String getName() {
        return this.name
    }

    String getOption() {
        return this.opt
    }
    
    @Override
    String getOptionName() {
        return this.optName
    }

    @Override
    String getUsage() {
        return this.opt
    }
}
