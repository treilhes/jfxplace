package com.treilhes.jfxplace.core.document.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.jfxplace.core.document.preference.WildcardImportsPreference;
import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.core.fxom.collector.DeclaredClassCollector;
import com.treilhes.jfxplace.core.fxom.glue.GlueDocument;
import com.treilhes.jfxplace.core.fxom.glue.GlueInstruction;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPreSerialization;

@ApplicationSingleton
public class WildcardImportsPreSerializationStep implements FXOMPreSerialization {

    private static final String IMPORT = "import"; // NOI18N

    private WildcardImportsPreference wildcardImportsPreference;

    public WildcardImportsPreSerializationStep(WildcardImportsPreference wildcardImportsPreference) {
        this.wildcardImportsPreference = wildcardImportsPreference;
    }

    @Override
    public FXOMDocument preSerializationTransform(FXOMDocument document, Map<Class<?>, Object> processContext) {
        updateImportInstructions(document);
        return document;
    }


    private void updateImportInstructions(FXOMDocument fxomDocument) {
        assert fxomDocument.getFxomRoot() != null;

        // gets list of the imports to be added to the FXML document.
        List<GlueInstruction> importList = getHeaderIncludes(fxomDocument);

        // synchronizes the glue with the list of glue instructions
        synchronizeHeader(fxomDocument.getGlue(), importList);
    }

    private List<GlueInstruction> getHeaderIncludes(FXOMDocument fxomDocument) {
        // TODO: When wildcardImport is true, add package name only when no of classes
        // which belong to the same package exceed 3

        // constructs the set of classes to be imported. No duplicates allowed.
        final Set<String> imports = new TreeSet<>(); // Sorted

        boolean wildcard = wildcardImportsPreference.getValue();

        // gets list of declared classes, declared classes are the ones directly used as
        // a Node.
        // Example: <Button/> ; classname = javafx.scene.control.Button
        fxomDocument.getFxomRoot().collect(DeclaredClassCollector.all())
                .stream()
                .map(dc -> wildcard ? dc.getPackageName() + ".*" : dc.getCanonicalName())
                .forEach(imports::add);

        return createGlueInstructionsForImports(fxomDocument, imports);
    }

    private void synchronizeHeader(GlueDocument glue, List<GlueInstruction> importList) {
        synchronized (this) {
            // find out where the first import instruction is located
            final int firstImportIndex;
            List<GlueInstruction> existingImports = glue.collectInstructions(IMPORT);
            if (existingImports.isEmpty()) {
                firstImportIndex = 0;
            } else {
                GlueInstruction firstImport = existingImports.get(0);
                firstImportIndex = glue.getContent().indexOf(firstImport);
            }

            // remove previously defined imports and leave all other things (like comments
            // and such) intact
            glue.getContent().removeIf(glueAuxiliary -> glueAuxiliary instanceof GlueInstruction gi
                    && IMPORT.equals(gi.getTarget()));

            // insert the import instructions at the first import index
            glue.getContent().addAll(firstImportIndex, importList);
        }
    }

    // Creates a List of glue instruction for all imported classes.
    private List<GlueInstruction> createGlueInstructionsForImports(FXOMDocument fxomDocument, Set<String> imports) {
        List<GlueInstruction> importsList = new ArrayList<>();
        imports.forEach(name -> {
            final GlueInstruction instruction = new GlueInstruction(fxomDocument.getGlue(), IMPORT, name);
            importsList.add(instruction);
        });
        return importsList;
    }
}
