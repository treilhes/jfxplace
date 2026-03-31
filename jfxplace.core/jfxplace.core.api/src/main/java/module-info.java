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
import com.treilhes.jfxplace.core.api.JfxplaceCoreApiExtension;

open module jfxplace.core.api {

    exports com.treilhes.jfxplace.core.api;
    exports com.treilhes.jfxplace.core.api.action;
    exports com.treilhes.jfxplace.core.api.action.editor;

    exports com.treilhes.jfxplace.core.api.application;
    exports com.treilhes.jfxplace.core.api.application.annotation;

    exports com.treilhes.jfxplace.core.api.ctxmenu;
    exports com.treilhes.jfxplace.core.api.ctxmenu.annotation;

    exports com.treilhes.jfxplace.core.api.lifecycle;

    exports com.treilhes.jfxplace.core.api.ui.controller.alert;

    exports com.treilhes.jfxplace.core.api.ui.controller.dock;
    exports com.treilhes.jfxplace.core.api.ui.controller.dock.annotation;
    exports com.treilhes.jfxplace.core.api.editor.images;
    exports com.treilhes.jfxplace.core.api.factory;
    exports com.treilhes.jfxplace.core.api.fs;
    exports com.treilhes.jfxplace.core.api.fs.content;
    exports com.treilhes.jfxplace.core.api.fs.watcher;
    exports com.treilhes.jfxplace.core.api.glossary;
    exports com.treilhes.jfxplace.core.api.guide;
    exports com.treilhes.jfxplace.core.api.i18n;

    exports com.treilhes.jfxplace.core.api.maven;
    exports com.treilhes.jfxplace.core.api.ui;
    exports com.treilhes.jfxplace.core.api.ui.controller;
    exports com.treilhes.jfxplace.core.api.ui.controller.dock.type;
    exports com.treilhes.jfxplace.core.api.ui.controller.menu;
    exports com.treilhes.jfxplace.core.api.ui.controller.menu.annotation;
    exports com.treilhes.jfxplace.core.api.ui.controller.misc;

    exports com.treilhes.jfxplace.core.api.ui.dialog;

    exports com.treilhes.jfxplace.core.api.preference;

    exports com.treilhes.jfxplace.core.api.selection;

    exports com.treilhes.jfxplace.core.api.settings;
    exports com.treilhes.jfxplace.core.api.shortcut;
    exports com.treilhes.jfxplace.core.api.shortcut.annotation;
    exports com.treilhes.jfxplace.core.api.subjects;

    exports com.treilhes.jfxplace.core.api.task;
    exports com.treilhes.jfxplace.core.api.tooltheme;

    exports com.treilhes.jfxplace.core.api.javafx;
    exports com.treilhes.jfxplace.core.api.job;
    exports com.treilhes.jfxplace.core.api.job.base;
    exports com.treilhes.jfxplace.core.api.util;



    requires transitive jfxplace.javafx.starter;

    requires transitive emc4j.boot.api;

//    requires transitive emc4j.boot.loader;
//    requires transitive emc4j.boot.platform;
//    requires transitive emc4j.boot.maven;
    requires transitive emc4j.boot.starter;

    requires transitive jfxplace.core.utils;
    //requires transitive jfxplace.core.fxom;
    //requires transitive jfxplace.core.metadata;
    // required to allow class access from JavafxThreadClassloader
    requires transitive jfxplace.core.controls;

    requires transitive io.reactivex.rxjava3;
    requires transitive org.reactivestreams;
    requires transitive org.pdfsam.rxjavafx;
    //requires jfxplace.javafx.fxml.patch.link;

    provides Extension with JfxplaceCoreApiExtension;
}