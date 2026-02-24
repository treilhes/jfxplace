/*
 * Copyright (c) 2016, 2025, Gluon and/or its affiliates.
 * Copyright (c) 2021, 2025, Pascal Treilhes and/or its affiliates.
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation and Gluon nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.jfxapps.core.selection;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.gluonhq.jfxapps.core.api.fxom.editor.selection.DefaultSelectionGroupFactory;
import com.gluonhq.jfxapps.core.api.fxom.editor.selection.FxomSelection;
import com.gluonhq.jfxapps.core.api.fxom.editor.selection.FxomSelectionGroup;
import com.gluonhq.jfxapps.core.api.fxom.editor.selection.ObjectSelectionGroup;
import com.gluonhq.jfxapps.core.api.fxom.editor.selection.TargetSelection;
import com.gluonhq.jfxapps.core.api.fxom.subjects.FxomEvents;
import com.gluonhq.jfxapps.core.api.selection.Selection;
import com.gluonhq.jfxapps.core.api.selection.SelectionGroup;
import com.gluonhq.jfxapps.core.fxom.FXOMDocument;
import com.gluonhq.jfxapps.core.fxom.FXOMObject;
import com.gluonhq.jfxapps.core.fxom.collector.FxCollector;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;

/**
 * Selection class represents the selected objects for an editor controller.
 * <p>
 * Selected objects are represented an instance of {@link AbstractSelectionGroup}.
 *
 *
 */
@ApplicationInstanceSingleton
public class FxomSelectionImpl implements FxomSelection {

    private final DefaultSelectionGroupFactory defaultSelectionGroupFactory;
    //private final SimpleIntegerProperty revision = new SimpleIntegerProperty();
    private final Selection selection;
    private final TargetSelection targetSelection;

    //private SelectionGroup group;
    //private boolean lock;
    //private long lastListenerInvocationTime;
    //private int updateDepth;


    public FxomSelectionImpl(
            FxomEvents documentManager,
            Selection selection,
            TargetSelection targetSelection,
            DefaultSelectionGroupFactory defaultSelectionGroupFactory) {
        super();
        this.selection = selection;
        this.targetSelection = targetSelection;
        this.defaultSelectionGroupFactory = defaultSelectionGroupFactory;
        documentManager.fxomDocument().subscribe(fxom -> selection.clear());
    }
    /**
     * Replaces the selected items by the specified fxom object.
     * This routine adds +1 to the revision number.
     *
     * @param fxomObject the object to be selected
     */
    @Override
    public void select(FXOMObject fxomObject) {
        assert fxomObject != null;

        select(fxomObject, null);
    }

    /**
     * Replaces the selected items by the specified fxom object and hit node.
     * This routine adds +1 to the revision number.
     *
     * @param fxomObject the object to be selected
     * @param hitNode null or the node hit by the mouse during selection
     */
    @Override
    public void select(FXOMObject fxomObject, Node hitNode) {
        selection.select(defaultSelectionGroupFactory.getGroup(fxomObject, hitNode));
    }

    /**
     * Replaces the selected items by the specified fxom objects.
     * This routine adds +1 to the revision number.
     *
     * @param fxomObjects the objects to be selected
     */
    @Override
    public void select(Collection<FXOMObject> fxomObjects) {
        assert fxomObjects != null;

        final FXOMObject hitObject;
        if (fxomObjects.isEmpty()) {
            hitObject = null;
        } else {
            hitObject = fxomObjects.iterator().next();
        }

        select(fxomObjects, hitObject, null);
    }

    /**
     * Replaces the selected items by the specified fxom objects.
     * This routine adds +1 to the revision number.
     *
     * @param fxomObjects the objects to be selected
     * @param hitObject the object hit by the mouse during selection
     * @param hitNode null or the node hit by the mouse during selection
     */
    @Override
    public void select(Collection<FXOMObject> fxomObjects, FXOMObject hitObject, Node hitNode) {

        assert fxomObjects != null;

        final SelectionGroup newGroup;
        if (fxomObjects.isEmpty()) {
            newGroup = null;
        } else {
            newGroup = defaultSelectionGroupFactory.getGroup(fxomObjects, hitObject, hitNode);
        }
        selection.select(newGroup);
    }
//
//    /**
//     * Replaces the selected items by the specified column/row.
//     * This routine adds +1 to the revision number.
//     *
//     * @param gridPaneObject fxom object of the gridpane holding the column/row
//     * @param feature column/row
//     * @param featureIndex index of the column/row to be selected
//     */
//    public void select(FXOMInstance gridPaneObject, Type feature, int featureIndex) {
//
//        assert gridPaneObject != null;
//        assert gridPaneObject.getSceneGraphObject().isInstanceOf(GridPane.class);
//
//        select(gridSelectionGroupFactory.getGroup(gridPaneObject, feature, featureIndex));
//    }


    @Override
    public boolean isSelected(FXOMObject fxomObject) {
        var group = selection.getGroup();
        if (fxomObject != null && group != null && group instanceof FxomSelectionGroup fsg) {
            return fsg.getItems().contains(fxomObject);
        }
        return false;
    }


//    /**
//     * Returns true if the specified column/row is part of the selection.
//     * Conditions must be met:
//     * 1) this selection should an GridSelectionGroup
//     * 2) GridSelectionGroup.type matches feature
//     * 3) GridSelectionGroup.indexes contains featureIndex
//     *
//     * @param gridPaneObject fxom object of the gridpane holding the column/row
//     * @param feature column/row
//     * @param featureIndex index of the column/row to be checked
//     * @return  true if this foxm object is selected.
//     */
//    public boolean isSelected(FXOMInstance gridPaneObject, Type feature, int featureIndex) {
//        final boolean result;
//
//        assert gridPaneObject != null;
//        assert gridPaneObject.getSceneGraphObject().isInstanceOf(GridPane.class);
//
//        if (group instanceof GridSelectionGroup) {
//            final GridSelectionGroup gsg = (GridSelectionGroup) group;
//            result = (gsg.getType() == feature)
//                    && (gsg.getIndexes().contains(featureIndex));
//        } else {
//            result = false;
//        }
//
//        return result;
//    }

    //
//  /**
//   * Returns true if the specified fxom object is part of this selection.
//   * Conditions must be met:
//   * 1) this selection should an ObjectSelectionGroup
//   * 2) the fxom object should belong to this group.
//   *
//   * @param fxomObject an fxom object
//   *
//   * @return  true if this foxm object is selected.
//   */
//  public boolean isSelected(FXOMObject fxomObject) {
//      final boolean result;
//
//      assert fxomObject != null;
//
//      if (group instanceof ObjectSelectionGroup) {
//          final ObjectSelectionGroup osg = (ObjectSelectionGroup) group;
//          result = osg.getItems().contains(fxomObject);
//      } else {
//          result = false;
//      }
//
//      return result;
//  }

    /**
     * Update the hit object and hit point of the current selection.
     *
     * @param hitObject the object hit by the mouse during selection
     * @param hitNode null or the node hit by the mouse during selection
     */
    @Override
    public void updateHitObject(FXOMObject hitObject, Node hitNode) {
        if (isSelected(hitObject)) {
            assert selection.getGroup() instanceof ObjectSelectionGroup;
            final ObjectSelectionGroup osg = (ObjectSelectionGroup) selection.getGroup();
            select(osg.getItems(), hitObject, hitNode);
        } else {
            select(hitObject, hitNode);
        }
    }



    @Override
    public FXOMObject getHitItem() {
        if (selection.getGroup() instanceof FxomSelectionGroup fsg) {
            return fsg.getHitItem();
        }
        return null;
    }

//    /**
//     * Adds/removes the specified column/row to/from the selected items.
//     * This routine adds +1 to the revision number.
//     *
//     * @param gridPaneObject fxom object of the gridpane holding the column/row
//     * @param feature column/row
//     * @param featureIndex index of the column/row to be selected
//     */
//    public void toggleSelection(FXOMInstance gridPaneObject, Type feature, int featureIndex) {
//
//        assert gridPaneObject != null;
//        assert gridPaneObject.getSceneGraphObject().isInstanceOf(GridPane.class);
//
//        final AbstractSelectionGroup newGroup;
//        if (group instanceof GridSelectionGroup) {
//            final GridSelectionGroup gsg = (GridSelectionGroup) group;
//            if (gsg.getType() == feature) {
//                final Set<Integer> indexes = gsg.getIndexes();
//                if (indexes.contains(featureIndex)) {
//                    if (indexes.size() == 1) {
//                        // featureIndex is the last selected index
//                        // GridSelectionGroup -> ObjectSelectionGroup
//                        newGroup = objectSelectionGroupFactory.getGroup(gridPaneObject, null);
//                    } else {
//                        final Set<Integer> newIndexes = new HashSet<>();
//                        newIndexes.addAll(indexes);
//                        newIndexes.remove(featureIndex);
//                        newGroup = gridSelectionGroupFactory.getGroup(gridPaneObject, feature, newIndexes);
//                    }
//                } else {
//                    final Set<Integer> newIndexes = new HashSet<>();
//                    newIndexes.addAll(indexes);
//                    newIndexes.add(featureIndex);
//                    newGroup = gridSelectionGroupFactory.getGroup(gridPaneObject, feature, newIndexes);
//                }
//            } else {
//                newGroup = gridSelectionGroupFactory.getGroup(gridPaneObject, feature, featureIndex);
//            }
//        } else {
//            newGroup = gridSelectionGroupFactory.getGroup(gridPaneObject, feature, featureIndex);
//        }
//
//        select(newGroup);
//    }
//
    /**
     * Adds/removes the specified object from the selected items.
     * This routine adds +1 to the revision number.
     *
     * @param fxomObject the object to be added/removed
     */
    @Override
    public void toggleSelection(FXOMObject fxomObject) {
        selection.toggleSelection(defaultSelectionGroupFactory.getGroup(fxomObject, null));
    }
//
//    /**
//     * Adds/removes the specified object from the selected items.
//     * This routine adds +1 to the revision number.
//     *
//     * @param fxomObject the object to be added/removed
//     * @param hitNode null or the node hit by the mouse during selection
//     */
//    public void toggleSelection(FXOMObject fxomObject, Node hitNode) {
//
//        assert fxomObject != null;
//
//        final ObjectSelectionGroup newGroup;
//        if (group instanceof ObjectSelectionGroup) {
//            final ObjectSelectionGroup osg = (ObjectSelectionGroup) group;
//            final Set<FXOMObject> currentItems = osg.getItems();
//            if (currentItems.contains(fxomObject)) {
//                if (currentItems.size() == 1) {
//                    // fxomObject is selected and is the last item
//                    newGroup = null;
//                } else {
//                    final Set<FXOMObject> newItems = new HashSet<>();
//                    newItems.addAll(currentItems);
//                    newItems.remove(fxomObject);
//                    final FXOMObject newHitItem = newItems.iterator().next();
//                    newGroup = objectSelectionGroupFactory.getGroup(newItems, newHitItem, null);
//                }
//            } else {
//                final Set<FXOMObject> newItems = new HashSet<>();
//                newItems.addAll(currentItems);
//                newItems.add(fxomObject);
//                newGroup = objectSelectionGroupFactory.getGroup(newItems, fxomObject, hitNode);
//            }
//        } else {
//            newGroup = objectSelectionGroupFactory.getGroup(fxomObject, hitNode);
//        }
//
//        select(newGroup);
//    }





    /**
     * Returns null or the first selected ancestor of the specified fxom object.
     *
     * @param fxomObject an fxom object
     * @return null or the first selected ancestor of the specified fxom object.
     */
    @Override
    public FXOMObject lookupSelectedAncestor(FXOMObject fxomObject) {
        assert fxomObject != null;

        FXOMObject result = null;
        FXOMObject parent = fxomObject.getParentObject();

        while ((parent != null) && (result == null)) {
            if (isSelected(parent)) {
                result = parent;
            }
            parent = parent.getParentObject();
        }

        return result;
    }


    /**
     * Returns the common ancestor of the selected items or null if selection
     * is empty or root object is selected.
     *
     * @return
     */
    @Override
    public FXOMObject getAncestor() {

        final var group = selection.getGroup();
        if (group != null && group instanceof FxomSelectionGroup fsg) {
            return fsg.getAncestor();
        }

        return null;
    }

    /**
     * Returns true if the selected objects are all connected to the
     * specified documents.
     *
     * @param fxomDocument an fxom document (not null)
     * @return true if the selected objects are all connected to the
     * specified documents.
     */
    @Override
    public boolean isValid(FXOMDocument fxomDocument) {
        assert fxomDocument != null;

        final var group = selection.getGroup();
        if (group != null && group instanceof FxomSelectionGroup fsg) {
            return fsg.isValid(fxomDocument);
        }

        return true;
    }

    /**
     * Check if the current selection objects are all instances of a {@link Node},
     * @return true if the current selection objects are all instances of a {@link Node},
     * false otherwise.
     */
    @Override
    public boolean isSelectionNode() {
        return isSelectionOfType(Node.class);
    }

    /**
     * Check if the current selection objects are all instances of a {@link Control}
     * @return true if the current selection objects are all instances of a {@link Control},
     * false otherwise.
     */
    @Override
    public boolean isSelectionControl() {
        return isSelectionOfType(Control.class);
    }

    /**s
     * Check if the current selection objects are all instances of the provided type,
     * @param the required type of selected objects
     * @return true if the current selection objects are all instances of the provided type,
     * false otherwise.
     */
    @Override
    public boolean isSelectionOfType(Class<?> type) {
        final var group = selection.getGroup();

        if (group != null && group instanceof FxomSelectionGroup fsg) {
            for (FXOMObject fxomObject : fsg.getItems()) {
                final boolean isClass = type.isAssignableFrom(fxomObject.getSceneGraphObject().getObjectClass());
                if (isClass == false) {
                    return false;
                }
            }
            return true;
        }

        return false;

    }

    @Override
    public Map<String, FXOMObject> collectSelectedFxIds() {

        final var group = selection.getGroup();
        if (group != null && group instanceof FxomSelectionGroup fsg) {
            return fsg.collect(FxCollector.fxIdsUniqueMap());
        }

        return Collections.emptyMap();
    }
    @Override
    public boolean isEmpty() {
        return selection.isEmpty() ||
                !(selection.getGroup() instanceof FxomSelectionGroup) ||
                (selection.getGroup() instanceof FxomSelectionGroup fsg && fsg.getItems().isEmpty());
    }
    @Override
    public FxomSelectionGroup getGroup() {

        final var group = selection.getGroup();
        if (group != null && group instanceof FxomSelectionGroup fsg) {
            return fsg;
        }
        return null;
    }
    @Override
    public Selection getSelection() {
        return selection;
    }
    @Override
    public ReadOnlyIntegerProperty revisionProperty() {
        return selection.revisionProperty();
    }
    @Override
    public int getRevision() {
        return selection.getRevision();
    }
    @Override
    public Node getCheckedHitNode() {
        return selection.getCheckedHitNode();
    }
    @Override
    public void select(SelectionGroup newGroup) {
        selection.select(newGroup);
    }
    @Override
    public void selectNext() {
        selection.selectNext();
    }
    @Override
    public void selectPrevious() {
        selection.selectPrevious();
    }
    @Override
    public void selectAll() {
        selection.selectAll();
    }
    @Override
    public boolean isSelected(SelectionGroup selectedGroup) {
        return selection.isSelected(selectedGroup);
    }
    @Override
    public void toggleSelection(SelectionGroup toggleGroup) {
        toggleSelection(toggleGroup);
    }
    @Override
    public void clear() {
        clear();
    }
    @Override
    public long getLastListenerInvocationTime() {
        return selection.getLastListenerInvocationTime();
    }
    @Override
    public void beginUpdate() {
        selection.beginUpdate();
    }
    @Override
    public void endUpdate() {
        selection.endUpdate();
    }

}

