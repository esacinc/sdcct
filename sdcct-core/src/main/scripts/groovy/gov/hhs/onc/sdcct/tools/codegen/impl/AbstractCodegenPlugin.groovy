package gov.hhs.onc.sdcct.tools.codegen.impl

import com.sun.tools.ws.wscompile.ErrorReceiver
import com.sun.tools.ws.wscompile.WsimportOptions
import com.sun.tools.xjc.BadCommandLineException
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.Plugin
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.tools.codegen.CodegenPlugin
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.xml.namespace.QName
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXException

abstract class AbstractCodegenPlugin implements CodegenPlugin {
    protected class XjcPluginAdapter extends Plugin {
        @Override
        boolean run(Outline outline, Options opts, ErrorHandler errorHandler) throws SAXException {
            return true
        }

        @Override
        void postProcessModel(Model model, ErrorHandler errorHandler) {
        }

        @Override
        boolean isCustomizationTagName(String nsUri, String localName) {
            return (AbstractCodegenPlugin.this.elems.containsKey(nsUri) && (AbstractCodegenPlugin.this.elems.get(nsUri).contains(localName)))
        }

        @Override
        List<String> getCustomizationURIs() {
            return AbstractCodegenPlugin.this.elems.keySet().stream().collect(Collectors.toList())
        }

        @Override
        int parseArgument(Options opts, String[] args, int argIndex) throws BadCommandLineException, IOException {
            return super.parseArgument(opts, args, argIndex)
        }

        @Override
        void onActivated(Options opts) throws BadCommandLineException {
        }

        @Override
        String getOptionName() {
            return AbstractCodegenPlugin.this.optionName
        }

        @Override
        String getUsage() {
            return AbstractCodegenPlugin.this.usage
        }
    }

    protected class WsimportPluginAdapter extends com.sun.tools.ws.wscompile.Plugin {
        @Override
        boolean run(com.sun.tools.ws.processor.model.Model wsdlModel, WsimportOptions wsimportOpts, ErrorReceiver errorReceiver) throws SAXException {
            return true
        }

        @Override
        int parseArgument(com.sun.tools.ws.wscompile.Options opts, String[] args, int argIndex) throws com.sun.tools.ws.wscompile.BadCommandLineException,
            IOException {
            return super.parseArgument(opts, args, argIndex)
        }

        @Override
        void onActivated(com.sun.tools.ws.wscompile.Options opts) throws com.sun.tools.ws.wscompile.BadCommandLineException {
        }

        @Override
        String getOptionName() {
            return AbstractCodegenPlugin.this.optionName
        }

        @Override
        String getUsage() {
            return AbstractCodegenPlugin.this.usage
        }
    }

    protected final static String OPT_PREFIX = "-"

    protected CodegenErrorReceiver errorReceiver
    protected String optName
    protected String opt
    protected Map<String, Set<String>> elems
    protected XjcPluginAdapter xjcPlugin = new XjcPluginAdapter()
    protected WsimportPluginAdapter wsimportPlugin = new WsimportPluginAdapter()

    protected AbstractCodegenPlugin(CodegenErrorReceiver errorReceiver, String optName, QName... elemQnames) {
        this.errorReceiver = errorReceiver
        this.opt = (OPT_PREFIX + (this.optName = optName))
        this.elems = Stream.of(elemQnames).collect(Collectors.groupingBy({ it.namespaceURI }, {
            new LinkedHashMap<String, Set<String>>(elemQnames.length)
        }, Collectors.mapping({ it.localPart }, Collectors.toCollection({ new LinkedHashSet<String>() }))))
    }

    @Override
    com.sun.tools.ws.wscompile.Plugin forWsimport() {
        return this.wsimportPlugin
    }

    @Override
    Plugin forXjc() {
        return this.xjcPlugin
    }
    
    protected void process(com.sun.tools.ws.processor.model.Model wsdlModel) throws Exception {
    }
    
    protected void processModel(Model model) throws Exception {
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
