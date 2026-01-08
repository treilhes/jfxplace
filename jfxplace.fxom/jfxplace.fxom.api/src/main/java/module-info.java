
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
import com.treilhes.emc4j.boot.api.loader.extension.Extension;
import com.gluonhq.jfxapps.fxom.api.FxomExtension;

open module jfxplace.fxom.api {

    exports com.gluonhq.jfxapps.core.api.fxom.clipboard;
    exports com.gluonhq.jfxapps.core.api.fxom.content.decoration;
    exports com.gluonhq.jfxapps.core.api.fxom.content.mode;
    exports com.gluonhq.jfxapps.core.api.fxom.content.mode.annotation;
    exports com.gluonhq.jfxapps.core.api.fxom.css;
    exports com.gluonhq.jfxapps.core.api.fxom.dnd;
    exports com.gluonhq.jfxapps.core.api.fxom.editor.selection;
    exports com.gluonhq.jfxapps.core.api.fxom.error;
    exports com.gluonhq.jfxapps.core.api.fxom.gesture;
    exports com.gluonhq.jfxapps.core.api.fxom.jobs;
    exports com.gluonhq.jfxapps.core.api.fxom.job;
    exports com.gluonhq.jfxapps.core.api.fxom.job.base;
    exports com.gluonhq.jfxapps.core.api.fxom.library;
    exports com.gluonhq.jfxapps.core.api.fxom.mask;
    exports com.gluonhq.jfxapps.core.api.fxom.subjects;
    exports com.gluonhq.jfxapps.core.api.fxom.ui.controller.misc;
    exports com.gluonhq.jfxapps.core.api.fxom.ui.controller.selbar;
    exports com.gluonhq.jfxapps.core.api.fxom.ui.controller.ctxmenu;
    exports com.gluonhq.jfxapps.core.api.fxom.ui.controller.ctxmenu.annotation;
    exports com.gluonhq.jfxapps.core.api.fxom.ui.tool;
    exports com.gluonhq.jfxapps.core.api.fxom.util;

    requires transitive jfxplace.javafx.starter;
    requires transitive jfxplace.core.api;
    requires transitive emc4j.boot.api;

//    requires transitive emc4j.boot.loader;
//    requires transitive emc4j.boot.platform;
//    requires transitive emc4j.boot.maven;
    requires transitive emc4j.boot.starter;

    requires transitive jfxplace.core.utils;
    requires transitive jfxplace.core.fxom;
    requires transitive jfxplace.core.metadata;
    // required to allow class access from JavafxThreadClassloader
    requires transitive jfxplace.core.controls;

    requires transitive io.reactivex.rxjava3;
    requires transitive org.reactivestreams;
    requires transitive org.pdfsam.rxjavafx;
    requires jfxplace.javafx.graphics.patch.link;
    requires jfxplace.javafx.fxml.patch.link;

    provides Extension with FxomExtension;
}