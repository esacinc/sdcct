package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import javax.annotation.Nullable
import org.w3c.dom.Element

class CodeSystemFilterCodegenModel extends AbstractTermCodegenModel {
    private ValueSetCodegenModel valueSet
    private boolean include
    private Set<String> concepts = new LinkedHashSet<>()

    CodeSystemFilterCodegenModel(ValueSetCodegenModel valueSet, @Nullable Element elem, String uri, boolean include, String ... concepts) {
        super(elem, uri, null, uri)
        
        this.valueSet = valueSet
        this.include = include
        
        this.addConcepts(concepts)
    }

    void addConcepts(String ... concepts) {
        concepts.each{ this.concepts.add(it) }
    }
    
    boolean hasConcepts() {
        return !this.concepts.isEmpty()
    }
    
    Set<String> getConcepts() {
        return this.concepts
    }
    
    void setConcepts(Set<String> concepts) {
        this.concepts.clear()
        this.concepts.addAll(concepts)
    }
    
    ValueSetCodegenModel getValueSet() {
        return this.valueSet
    }
}
