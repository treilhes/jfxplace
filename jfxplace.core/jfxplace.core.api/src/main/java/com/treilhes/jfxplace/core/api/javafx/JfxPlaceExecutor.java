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
package com.treilhes.jfxplace.core.api.javafx;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

import com.treilhes.emc4j.boot.api.context.EmContext;

import javafx.application.Platform;


public final class JfxPlaceExecutor {

    private EmContext context;

    public JfxPlaceExecutor(EmContext context) {
        super();
        this.context = context;
    }

    /**
     * Execute the runnable later on the fx thread
     * @param scopedDocument the document scope uuid
     * @param runnable the code to run
     */
    public void runOnFxThread(Runnable runnable) {
        var wrapped = wrap(runnable);

        if (Platform.isFxApplicationThread()) {
            wrapped.run();
        } else {
            Platform.runLater(wrapped::run);
        }

    }

    public <T> FutureTask<T> callOnFxThread(Callable<T> callable) {
        var wrapped = wrap(callable);

        final FutureTask<T> task = new FutureTask<>(wrapped);
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task::run);
        }

        return task;
    }

    public <T> T run(Supplier<T> runnable) {
        var wrapped = wrap(runnable);
        return wrapped.get();
    }

    /**
     * Execute the runnable on a dedicated thread ensuring an unchanging scope
     *
     * @param scopedDocument the document scope
     * @param runnable       the code to run
     */
    public void runOnThread(Runnable runnable) {
        var wrapped = wrap(runnable);
        Thread t = new Thread(wrapped);
        t.setDaemon(true);
        t.start();
    }

    private Runnable wrap(Runnable runnable) {
        return () -> {
            var bckLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(context.getBeanClassLoader());
            try {
                runnable.run();
            } finally {
                Thread.currentThread().setContextClassLoader(bckLoader);
            }
        };
    }

    private <T> Callable<T> wrap(Callable<T> callable) {
        return () -> {
            var bckLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(context.getBeanClassLoader());
            try {
                return callable.call();
            } finally {
                Thread.currentThread().setContextClassLoader(bckLoader);
            }
        };
    }

    private <T> Supplier<T> wrap(Supplier<T> supplier) {
        return () -> {
            var bckLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(context.getBeanClassLoader());
            try {
                return supplier.get();
            } finally {
                Thread.currentThread().setContextClassLoader(bckLoader);
            }
        };
    }


    public static void ensureFxThread(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

}
