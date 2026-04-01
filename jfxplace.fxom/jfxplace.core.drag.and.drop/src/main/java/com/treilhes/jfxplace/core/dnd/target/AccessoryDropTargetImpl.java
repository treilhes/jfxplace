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
package com.treilhes.jfxplace.core.dnd.target;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstancePrototype;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.api.driver.Driver;
import com.treilhes.jfxplace.core.api.fxom.dnd.AbstractDropTarget;
import com.treilhes.jfxplace.core.api.fxom.dnd.AccessoryDropTarget;
import com.treilhes.jfxplace.core.api.fxom.dnd.DefaultDropTargetFactory;
import com.treilhes.jfxplace.core.api.fxom.dnd.DragSource;
import com.treilhes.jfxplace.core.api.fxom.dnd.DropJobCustomizer;
import com.treilhes.jfxplace.core.api.fxom.dnd.DropTargetFactory;
import com.treilhes.jfxplace.core.api.fxom.editor.selection.SelectionJobsFactory;
import com.treilhes.jfxplace.core.api.fxom.job.base.BatchJob;
import com.treilhes.jfxplace.core.api.fxom.jobs.FxomJobsFactory;
import com.treilhes.jfxplace.core.api.fxom.mask.Accessory;
import com.treilhes.jfxplace.core.api.fxom.mask.FXOMObjectMask;
import com.treilhes.jfxplace.core.api.job.Job;
import com.treilhes.jfxplace.core.fxom.FXOMElement;
import com.treilhes.jfxplace.core.fxom.FXOMInstance;
import com.treilhes.jfxplace.core.fxom.FXOMObject;

/**
 *
 */
@ApplicationInstancePrototype
public final class AccessoryDropTargetImpl extends AbstractDropTarget implements AccessoryDropTarget {

    private static final Logger logger = LoggerFactory.getLogger(AccessoryDropTargetImpl.class);

    private final Driver driver;
    private final FXOMObjectMask.Factory designMaskFactory;
    private final BatchJob.Factory batchJobFactory;
    private final SelectionJobsFactory selectionJobsFactory;
    private final FxomJobsFactory fxomJobsFactory;

    private FXOMElement targetContainer;
    private Accessory accessory;
    private FXOMObject beforeChild;
    private FXOMObjectMask mask;



    protected AccessoryDropTargetImpl(
            Driver driver,
            FXOMObjectMask.Factory designMaskFactory,
            BatchJob.Factory batchJobFactory,
            SelectionJobsFactory selectionJobsFactory,
            FxomJobsFactory fxomJobsFactory) {
        this.driver = driver;
        this.designMaskFactory = designMaskFactory;
        this.batchJobFactory = batchJobFactory;
        this.selectionJobsFactory = selectionJobsFactory;
        this.fxomJobsFactory = fxomJobsFactory;
    }

    protected void setDropTargetParameters(FXOMElement targetContainer, Accessory accessory, FXOMObject beforeChild) {
        assert targetContainer != null;
        this.targetContainer = targetContainer;
        this.accessory = accessory;
        this.beforeChild = beforeChild;
    }

    @Override
    public Accessory getAccessory() {
        return accessory;
    }

    @Override
    public FXOMObject getBeforeChild() {
        return beforeChild;
    }

    /*
     * AbstractDropTarget
     */
    @Override
    public FXOMObject getTargetObject() {
        return targetContainer;
    }

    private FXOMObjectMask getMask() {
        if (mask == null) {
            mask = designMaskFactory.getMask(targetContainer);
        }
        return mask;
    }
    @Override
    public boolean acceptDragSource(DragSource dragSource) {
        assert dragSource != null;
        assert dragSource.getDraggedObjects() != null;

        Accessory<?> targetAccessory = findTargetAccessory(dragSource.getDraggedObjects());

        boolean result = targetAccessory != null; // we found an accessory accepting the drop

        if (!result) {
            return false;
        }

        //if (targetAccessory.isCollection()) {
            final FXOMObject draggedObject0 = dragSource.getDraggedObjects().get(0);
            final boolean sameContainer
                    = targetContainer == draggedObject0.getParentObject();
            final boolean sameIndex
                    = (beforeChild == draggedObject0)
                    || (beforeChild == draggedObject0.getNextSlibing());

            result &= ((sameContainer == false) || (sameIndex == false));
        //}

        if (logger.isDebugEnabled()) {
            logger.debug("Drag source accepted {} with initial accessory {} and resolved accessory {} for objects {}", result,
                    accessory == null ? "null": accessory.getName().getName(),
                    targetAccessory == null ? "null": targetAccessory.getName().getName(),
                    dragSource.getDraggedObjects().stream()
                        .map(o -> {
                            if (FXOMElement.class.isInstance(o)) {
                                Class<?> cls = ((FXOMElement)o).getDeclaredClass();
                                return cls == null ? "null" : cls.getSimpleName();
                            } else {
                                return o.getSceneGraphObject().isEmpty() ? "null" : o.getSceneGraphObject().getObjectClass().getSimpleName();
                            }

                        }).collect(Collectors.toList()));
        }

        return result;
    }

    @Override
    public Job makeDropJob(DragSource dragSource) {
        assert acceptDragSource(dragSource);

        final Accessory targetAccessory = findTargetAccessory(dragSource.getDraggedObjects());

        assert targetAccessory != null;

        final boolean shouldRefreshSceneGraph = true;

        final BatchJob result = batchJobFactory.getJob(shouldRefreshSceneGraph);
        result.setDescription(dragSource.makeDropJobDescription());

        final FXOMObject draggedObject = dragSource.getDraggedObjects().get(0);
        final FXOMObject currentParent = draggedObject.getParentObject();

        // Two steps :
        //  - remove drag source object from its current parent (if any)
        //  - set the drag source object as accessory of the drop target

        if (currentParent == targetContainer && targetAccessory.getName().equals(draggedObject.getParentProperty().getName())) {
            // It's a re-indexing job
            for (FXOMObject draggedObj : dragSource.getDraggedObjects()) {
                result.addSubJob(fxomJobsFactory.reIndexObject(draggedObj, beforeChild));
            }
        } else {
            if (currentParent != null) {
                for (FXOMObject draggedObj : dragSource.getDraggedObjects()) {
                    result.addSubJob(fxomJobsFactory.removeObject(draggedObj));
                }
            }

            int targetIndex = beforeChild == null ? -1 : beforeChild.getIndexInParentProperty();

            for (FXOMObject draggedObj : dragSource.getDraggedObjects()) {

                if (targetIndex == -1) {
                    final Job j = selectionJobsFactory.insertAsAccessory(draggedObj, targetContainer, targetAccessory);
                    result.addSubJob(j);
                } else {
                    final Job j = selectionJobsFactory.insertAsAccessory(draggedObj, targetContainer, targetAccessory, targetIndex++);
                    result.addSubJob(j);
                }

                // allow the driver to customize the drop job for this specific target and dragged object
                if (!targetContainer.getSceneGraphObject().isEmpty() && draggedObject instanceof FXOMInstance fxomInstance) {
                    var targetClass = targetContainer.getSceneGraphObject().getObjectClass();
                    var dropJobCustomizer = driver.make(DropJobCustomizer.class, targetClass);

                    if (dropJobCustomizer != null) {
                        dropJobCustomizer.customize(result, fxomInstance);
                    }
                }
            }
        }




        assert result.isExecutable();

        return result;
    }

    @Override
    public Accessory findTargetAccessory(List<? extends FXOMObject> draggedObject) {

        if (draggedObject.isEmpty()) {
            return null;
        }

        long nonVirtuals = draggedObject.stream().filter(Predicate.not(FXOMObject::isVirtual)).count();
        boolean needCollectionAccessory = nonVirtuals > 1;

        Accessory targetAccessory = accessory;


        if (targetAccessory != null) { // accessory was provided so check it
            if (needCollectionAccessory && !targetAccessory.isCollection()) {
                // we are dragging multiple objects but the required target is not a collection
                return null;
            }
            if (nonVirtuals > 0 && !targetAccessory.isCollection() && getMask().getAccessories(targetAccessory, false).size() >= 1 ) {
                // the target accessory is not a collection and already have an item into and we want to add a non virtual element
                return null;
            }
            if (!getMask().isAcceptingAccessory(targetAccessory, draggedObject)) {
                // one of the dragged object is refused into this accessory
                return null;
            }
        }

        if (targetAccessory == null) { //targeting main accessory
            // check if main accessory is a valid candidate
            if (getMask().getMainAccessory() != null
                    && (!needCollectionAccessory || (needCollectionAccessory && getMask().getMainAccessory().isCollection()))) {
                // check if droped objects are all compatible
                if (getMask().isAcceptingSubComponent(draggedObject)) {
                    targetAccessory = getMask().getMainAccessory();
                }
            }
        }

        if (targetAccessory == null) { //targeting the first valid accessory accepting the drop
            for (Accessory candidate:getMask().getAccessories()) {
                // check if accessory is a valid candidate
                if (!needCollectionAccessory || (needCollectionAccessory && candidate.isCollection())) {
                    // definition is ok, but is there some space left
                    if (candidate.isCollection() || getMask().getAccessories(candidate, false).size() >= 1) {
                        // check if droped objects are all compatible
                        if (getMask().isAcceptingAccessory(candidate ,draggedObject)) {
                            targetAccessory = candidate;
                            break;
                        }
                    }
                }
            }
        }

        return targetAccessory;
    }
    @Override
    public boolean isSelectRequiredAfterDrop() {
        return true;
    }

    /*
     * Objects
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.targetContainer);
        hash = 97 * hash + (this.accessory != null ? this.accessory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccessoryDropTargetImpl other = (AccessoryDropTargetImpl) obj;
        if (!Objects.equals(this.targetContainer, other.targetContainer)) {
            return false;
        }
        if (this.accessory != other.accessory) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AccessoryDropTarget{" + "targetContainer=" + targetContainer + ", accessory=" + accessory + '}'; //NOCHECK
    }


    /**
     * @deprecated Use Dependency Injection to get {@link DefaultDropTargetFactory} instance
     */
    @ApplicationInstanceSingleton
    @Deprecated(forRemoval=true)
    public static class Factory extends DropTargetFactory<AccessoryDropTarget> {
        @Deprecated
        public Factory(EmContext sbContext) {
            super(sbContext);
        }

        @Deprecated
        public AccessoryDropTarget getDropTarget(FXOMElement targetContainer, Accessory accessory, FXOMObject beforeChild) {
            return create(AccessoryDropTargetImpl.class, j -> j.setDropTargetParameters(targetContainer, accessory, beforeChild));
        }
        @Deprecated
        public AccessoryDropTarget getDropTarget(FXOMElement targetContainer, Accessory accessory) {
            return create(AccessoryDropTargetImpl.class, j -> j.setDropTargetParameters(targetContainer, accessory, null));
        }
        @Deprecated
        public AccessoryDropTarget getDropTarget(FXOMElement targetContainer, FXOMObject beforeChild) {
            return create(AccessoryDropTargetImpl.class, j -> j.setDropTargetParameters(targetContainer, null, beforeChild));
        }
        /**
         * Instantiates a new accessory drop target.
         * The target accessory will be discovered at runtime
         * It will be the first accessory accepting the object in the following order:
         * mainAccessory > accessories iteration
         * @param targetContainer the target container
         * @return the drop target
         */
        @Deprecated
        public AccessoryDropTarget getDropTarget(FXOMElement targetContainer) {
            return create(AccessoryDropTargetImpl.class, j -> j.setDropTargetParameters(targetContainer, null, null));
        }
    }
}
