package com.treilhes.jfxplace.core.fxom.pipeline.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jspecify.annotations.NonNull;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.core.fxom.collector.DeclaredClassCollector;
import com.treilhes.jfxplace.core.fxom.glue.GlueDocument;
import com.treilhes.jfxplace.core.fxom.glue.GlueInstruction;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPreSerialization;

@ApplicationSingleton
public class UpdateImportsPreSerializationStep implements FXOMPreSerialization {

    private static final String IMPORT = "import"; // NOI18N

    @Override
    public FXOMDocument preSerializationTransform(FXOMDocument document, @NonNull Map<Class<?>, Object> processContext) {
        if (document.getFxomRoot() != null) {
            updateImportInstructions(document);
        }
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

        // gets list of declared classes, declared classes are the ones directly used as
        // a Node.
        // Example: <Button/> ; classname = javafx.scene.control.Button
        fxomDocument.getFxomRoot().collect(DeclaredClassCollector.all())
                .stream()
                .map(Class::getCanonicalName)
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
