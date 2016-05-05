package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import javax.annotation.Nullable
import org.w3c.dom.Element

class CodeSystemCodegenModel extends AbstractContainerTermCodegenModel {
    private Map<String, ConceptCodegenModel> concepts = new LinkedHashMap<>()

    CodeSystemCodegenModel(@Nullable Element elem, String id, @Nullable String name, @Nullable String uri, @Nullable String oid, @Nullable String version) {
        super(elem, id, name, uri, oid, version)
    }

    void addConcepts(ConceptCodegenModel ... concepts) {
        concepts.each{ this.concepts[it.id] = it }
    }
    
    boolean hasConcepts() {
        return !this.concepts.isEmpty()
    }
    
    Map<String, ConceptCodegenModel> getConcepts() {
        return concepts
    }
    
    void setConcepts(Map<String, ConceptCodegenModel> concepts) {
        this.concepts.clear()
        this.concepts.putAll(concepts)
    }
}
