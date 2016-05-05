package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import javax.annotation.Nullable
import org.apache.commons.collections4.keyvalue.MultiKey
import org.w3c.dom.Element

class ValueSetCodegenModel extends AbstractContainerTermCodegenModel {
    private Map<String, CodeSystemFilterCodegenModel> codeSystemExcludes = new LinkedHashMap<>()
    private Map<String, CodeSystemFilterCodegenModel> codeSystemIncludes = new LinkedHashMap<>()
    private Set<String> valueSetImports = new LinkedHashSet<>()
    private Map<MultiKey<String>, ConceptCodegenModel> concepts = new LinkedHashMap<>()
    private boolean conceptsBuilt

    ValueSetCodegenModel(@Nullable Element elem, String id, @Nullable String name, @Nullable String uri, @Nullable String oid, @Nullable String version) {
        super(elem, id, name, uri, oid, version)
    }

    void buildConcepts(Map<String, ValueSetCodegenModel> valueSets, Map<String, CodeSystemCodegenModel> codeSystems) {
        ValueSetCodegenModel importedValueSet
        
        this.valueSetImports.each{
            importedValueSet = valueSets[it]
            
            if (!importedValueSet.areConceptsBuilt()) {
                importedValueSet.buildConcepts(valueSets, codeSystems)
            }
            
            importedValueSet.concepts.each{
                this.concepts.put(it.key, new ConceptCodegenModel(importedValueSet, it.value))
            }
        }
        
        buildCodeSystemFilterConcepts(codeSystems, this.codeSystemIncludes, true).each{ (this.concepts[it.key] = it.value) }
        
        buildCodeSystemFilterConcepts(codeSystems, this.codeSystemExcludes, false).keySet().each{ this.concepts.remove(it) }
        
        this.conceptsBuilt = true
    }
    
    private static Map<MultiKey<String>, ConceptCodegenModel> buildCodeSystemFilterConcepts(Map<String, CodeSystemCodegenModel> codeSystems,
        Map<String, CodeSystemFilterCodegenModel> codeSystemFilters, boolean defaultAddAll) {
        String filteredCodeSystemUri
        CodeSystemCodegenModel filteredCodeSystem
        CodeSystemFilterCodegenModel codeSystemFilter
        Map<String, ConceptCodegenModel> filteredCodeSystemConcepts
        Map<MultiKey<String>, ConceptCodegenModel> concepts = new LinkedHashMap<>()
        
        codeSystemFilters.each{
            if ((filteredCodeSystem = codeSystems[(filteredCodeSystemUri = it.key)]) == null) {
                return
            }
            
            codeSystemFilter = it.value
            filteredCodeSystemConcepts = filteredCodeSystem.concepts
            
            it.value.concepts.each{ concepts[new MultiKey<>(filteredCodeSystemUri, it)] = new ConceptCodegenModel(codeSystemFilter.valueSet,
                filteredCodeSystemConcepts[it]) }
        }
        
        if (defaultAddAll) {
            codeSystemFilters.each{
                if (!(codeSystemFilter = it.value).hasConcepts() && codeSystems.containsKey((filteredCodeSystemUri = it.key))) {
                    codeSystems[filteredCodeSystemUri].concepts.each{
                        concepts[new MultiKey<>(filteredCodeSystemUri, it.key)] = new ConceptCodegenModel(codeSystemFilter.valueSet, it.value)
                    }
                }
            }
        }
        
        return concepts
    }
    
    void addCodeSystemExcludes(CodeSystemFilterCodegenModel ... codeSystemExcludes) {
        codeSystemExcludes.each{ this.codeSystemExcludes[it.uri] = it }
    }
    
    boolean hasCodeSystemExcludes() {
        return !this.codeSystemExcludes.isEmpty()
    }
    
    Map<String, CodeSystemFilterCodegenModel> getCodeSystemExcludes() {
        return codeSystemExcludes
    }
    
    void setCodeSystemExcludes(Map<String, CodeSystemFilterCodegenModel> codeSystemExcludes) {
        this.codeSystemExcludes.clear()
        this.codeSystemExcludes.putAll(codeSystemExcludes)
    }
    
    void addCodeSystemIncludes(CodeSystemFilterCodegenModel ... codeSystemIncludes) {
        codeSystemIncludes.each{ this.codeSystemIncludes[it.uri] = it }
    }
    
    boolean hasCodeSystemIncludes() {
        return !this.codeSystemIncludes.isEmpty()
    }
    
    Map<String, CodeSystemFilterCodegenModel> getCodeSystemIncludes() {
        return codeSystemIncludes
    }
    
    void setCodeSystemIncludes(Map<String, CodeSystemFilterCodegenModel> codeSystemIncludes) {
        this.codeSystemIncludes.clear()
        this.codeSystemIncludes.putAll(codeSystemIncludes)
    }
    
    boolean areConceptsBuilt() {
        return this.conceptsBuilt
    }
    
    boolean hasConcepts() {
        return !this.concepts.isEmpty()
    }
    
    Map<MultiKey<String>, ConceptCodegenModel> getConcepts() {
        return concepts
    }
    
    void addValueSetImports(String ... valueSetImports) {
        valueSetImports.each{ this.valueSetImports.add(it) }
    }
    
    boolean hasValueSetImports() {
        return !this.valueSetImports.isEmpty()
    }
    
    Set<String> getValueSetImports() {
        return valueSetImports
    }
    
    void setValueSetImports(Set<String> valueSetImports) {
        this.valueSetImports.clear()
        this.valueSetImports.addAll(valueSetImports)
    }
}
