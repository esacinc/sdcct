package gov.hhs.onc.sdcct.xml.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.xml.StaticXmlTransformOptions;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nullable;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

public abstract class AbstractStaticXmlTransformOptions<T extends StaticXmlTransformOptions<T>> extends AbstractXmlTransformOptions<T>
    implements StaticXmlTransformOptions<T> {
    protected List<ExtensionFunctionDefinition> funcs = new ArrayList<>();
    protected Map<String, String> namespaces = new TreeMap<>();
    protected List<XdmDocument> pooledDocs = new ArrayList<>();

    private final static long serialVersionUID = 0L;

    protected AbstractStaticXmlTransformOptions() {
        super();
    }

    @Override
    protected void mergeInternal(T mergeOpts) {
        super.mergeInternal(mergeOpts);

        this.funcs.addAll(mergeOpts.getFunctions());
        this.namespaces.putAll(mergeOpts.getNamespaces());
        this.pooledDocs.addAll(mergeOpts.getPooledDocuments());
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T addFunction(ExtensionFunctionDefinition func) {
        this.funcs.add(func);

        return ((T) this);
    }

    @Override
    public boolean hasFunctions() {
        return !this.funcs.isEmpty();
    }

    @Override
    public List<ExtensionFunctionDefinition> getFunctions() {
        return this.funcs;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setFunctions(@Nullable List<ExtensionFunctionDefinition> funcs) {
        this.funcs.clear();

        if (!CollectionUtils.isEmpty(funcs)) {
            this.funcs.addAll(funcs);
        }

        return ((T) this);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T addNamespace(String prefix, String uri) {
        this.namespaces.put(prefix, uri);

        return ((T) this);
    }

    @Override
    public boolean hasNamespaces() {
        return !this.namespaces.isEmpty();
    }

    @Override
    public Map<String, String> getNamespaces() {
        return this.namespaces;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setNamespaces(@Nullable Map<String, String> namespaces) {
        this.namespaces.clear();

        if (!MapUtils.isEmpty(namespaces)) {
            this.namespaces.putAll(namespaces);
        }

        return ((T) this);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T addPooledDocument(XdmDocument pooledDoc) {
        this.pooledDocs.add(pooledDoc);

        return ((T) this);
    }

    @Override
    public boolean hasPooledDocuments() {
        return !this.pooledDocs.isEmpty();
    }

    @Override
    public List<XdmDocument> getPooledDocuments() {
        return this.pooledDocs;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setPooledDocuments(@Nullable List<XdmDocument> pooledDocs) {
        this.pooledDocs.clear();

        if (!CollectionUtils.isEmpty(pooledDocs)) {
            this.pooledDocs.addAll(pooledDocs);
        }

        return ((T) this);
    }
}
