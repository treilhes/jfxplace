/*
 * Copyright (c) 2016, 2024, Gluon and/or its affiliates.
 * Copyright (c) 2021, 2024, Pascal Treilhes and/or its affiliates.
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
package com.treilhes.jfxplace.core.api.selection;

import jakarta.annotation.Nullable;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.Node;

public interface Selection {

    /**
     * Returns the property holding the revision number of this selection.
     * Selection class adds +1 to this number each time the selection changes.
     *
     * @return the property holding the revision number of this selection.
     */
    ReadOnlyIntegerProperty revisionProperty();

    /**
     * Returns the revision number of this selection.
     *
     * @return the revision number of this selection.
     */
    int getRevision();


    Node getCheckedHitNode();


    /**
     * Replaces the selected items by the one from the specified selection group.
     *
     * @param newGroup null or the selection group defining items to be selected
     */
    void select(@Nullable SelectionGroup newGroup);

    void selectNext();

    void selectPrevious();

    void selectAll();

    boolean isSelected(SelectionGroup selectedGroup);

    void toggleSelection(SelectionGroup toggleGroup);

    /**
     * Empties this selection.
     * This routine adds +1 to the revision number.
     *
     */
    void clear();

    /**
     * Returns true if this selection is empty ie its selection group is null.
     * s
     * @return  true if this selection is empty.
     */
    boolean isEmpty();

    /**
     * Returns the group associated to this selection.
     * If this selection is empty, null is returned.
     *
     * @return  the group containing the selected items or null if selection is empty.
     */
    SelectionGroup getGroup();

    /**
     * Returns number of nanoseconds taken to execute selection listeners.
     *
     * @return number of nanoseconds taken to execute selection listeners.
     */
    long getLastListenerInvocationTime();

    /**
     * Begins an update sequence. Subsequent calls to select() and clear()
     * do not trigger any revision incrementation.
     */
    void beginUpdate();

    /**
     * Ends an update sequence. Revision is incremented.
     */
    void endUpdate();

}