package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import javax.annotation.Nullable
import org.w3c.dom.Element

class ConceptCodegenModel extends AbstractTermCodegenModel {
    private ValueSetCodegenModel valueSet
    private CodeSystemCodegenModel codeSystem
    
    ConceptCodegenModel(@Nullable ValueSetCodegenModel valueSet, ConceptCodegenModel concept) {
        this(valueSet, concept.codeSystem, concept.element, concept.id, concept.name)
    }
    
    ConceptCodegenModel(CodeSystemCodegenModel codeSystem, @Nullable Element elem, String id, @Nullable String name) {
        this(null, codeSystem, elem, id, name)
    }
    
    ConceptCodegenModel(@Nullable ValueSetCodegenModel valueSet, CodeSystemCodegenModel codeSystem, @Nullable Element elem, String id, @Nullable String name) {
        super(elem, id, name, null)
        
        this.valueSet = valueSet
        this.codeSystem = codeSystem
    }

    CodeSystemCodegenModel getCodeSystem() {
        return this.codeSystem
    }
    
    boolean hasValueSet() {
        return (this.valueSet != null)
    }
    
    @Nullable
    ValueSetCodegenModel getValueSet() {
        return this.valueSet
    }
}
