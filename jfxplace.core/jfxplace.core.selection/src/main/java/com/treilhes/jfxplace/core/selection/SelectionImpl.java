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
package com.treilhes.jfxplace.core.selection;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Fallback;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.api.selection.EmptySelectionGroupFactory;
import com.treilhes.jfxplace.core.api.selection.Selection;
import com.treilhes.jfxplace.core.api.selection.SelectionGroup;
import com.treilhes.jfxplace.core.api.subjects.ApplicationInstanceEvents;

import jakarta.annotation.Nullable;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;

/**
 * Selection class represents the selected objects for an editor controller.
 * <p>
 * Selected objects are represented an instance of {@link AbstractSelectionGroup}.
 *
 *
 */
@ApplicationInstanceSingleton
@Fallback
@Qualifier("default")
public class SelectionImpl implements Selection {

    private final SimpleIntegerProperty revision = new SimpleIntegerProperty();

    private final EmptySelectionGroupFactory emptySelectionGroupFactory;

    private SelectionGroup group;
    private boolean lock;
    private long lastListenerInvocationTime;
    private int updateDepth;


    public SelectionImpl(
            ApplicationInstanceEvents documentManager,
            EmptySelectionGroupFactory emptySelectionGroupFactory) {
        super();
        this.emptySelectionGroupFactory = emptySelectionGroupFactory;
        documentManager.closed().subscribe(_ -> clear());
    }

    /**
     * Replaces the selected items by the one from the specified selection group.
     *
     * @param newGroup null or the selection group defining items to be selected
     */
    @Override
    public void select(@Nullable SelectionGroup newGroup) {

        if (lock) {
            // Method is called from a revision property listener
            throw new IllegalStateException("Changing selection from a selection listener is forbidden");
        }

        if (newGroup == null) {
            newGroup = emptySelectionGroupFactory.empty();
        }

        if (Objects.equals(this.group, newGroup) == false) {
            beginUpdate();
            this.group = newGroup;
            endUpdate();
        }
    }

    /**
     * Returns the property holding the revision number of this selection.
     * Selection class adds +1 to this number each time the selection changes.
     *
     * @return the property holding the revision number of this selection.
     */
    @Override
    public ReadOnlyIntegerProperty revisionProperty() {
        return revision;
    }

    /**
     * Returns the revision number of this selection.
     *
     * @return the revision number of this selection.
     */
    @Override
    public int getRevision() {
        return revision.get();
    }

    @Override
    public boolean isSelected(SelectionGroup selectedGroup) {
        if (selectedGroup != null && group != null) {
            return group.isSelected(selectedGroup);
        }
        return false;
    }

    @Override
    public Node getCheckedHitNode() {
        return group == null ? null : group.getCheckedHitNode();
    }

    @Override
    public void toggleSelection(SelectionGroup toggleGroup) {
        assert toggleGroup != null;

        if (group == null || group.getClass() != toggleGroup.getClass()) {
            select(toggleGroup);
        } else {
            var toggledGroup = group.toggle(toggleGroup);
            select(toggledGroup);
        }
    }

    /**
     * Empties this selection.
     * This routine adds +1 to the revision number.
     *
     */
    @Override
    public void clear() {
        if (group != null) {
            beginUpdate();
            group = null;
            endUpdate();
        }
    }

    /**
     * Returns true if this selection is empty ie its selection group is null.
     * s
     * @return  true if this selection is empty.
     */
    @Override
    public boolean isEmpty() {
        return getGroup() == null;
    }

    /**
     * Returns the group associated to this selection.
     * If this selection is empty, null is returned.
     *
     * @return  the group containing the selected items or null if selection is empty.
     */
    @Override
    public SelectionGroup getGroup() {
        return group;
    }

    /**
     * Returns number of nanoseconds taken to execute selection listeners.
     *
     * @return number of nanoseconds taken to execute selection listeners.
     */
    @Override
    public long getLastListenerInvocationTime() {
        return lastListenerInvocationTime;
    }


    /**
     * Begins an update sequence. Subsequent calls to select() and clear()
     * do not trigger any revision incrementation.
     */
    @Override
    public void beginUpdate() {
        updateDepth++;
    }

    /**
     * Ends an update sequence. Revision is incremented.
     */
    @Override
    public void endUpdate() {
        assert updateDepth >= 1;
        updateDepth--;
        if (updateDepth == 0) {
            incrementRevision();
        }
    }

    /*
     * Private
     */

    private void incrementRevision() {
        lock = true;
        final long startTime = System.nanoTime();
        try {
            revision.set(revision.get()+1);
        } finally {
            lock = false;
        }
        lastListenerInvocationTime = System.nanoTime() - startTime;
    }


    @Override
    public void selectNext() {
        if (!isEmpty()) {
            select(this.group.selectNext());
        }
    }

    @Override
    public void selectPrevious() {
        if (!isEmpty()) {
            select(this.group.selectPrevious());
        }
    }

    @Override
    public void selectAll() {
        if (!isEmpty()) {
            select(this.group.selectAll());
        }
    }

}

