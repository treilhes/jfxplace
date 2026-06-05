package com.treilhes.jfxplace.app.debugtools.api.events;

import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.jfxplace.core.api.subjects.SubjectItem;
import com.treilhes.jfxplace.core.api.subjects.SubjectManager;

import io.reactivex.rxjava3.subjects.ReplaySubject;

public interface DebugEvents {

    SubjectItem<EmContext> context();

    void terminate();

    @ApplicationSingleton
    public class DebugEventsImpl implements DebugEvents {

        private final DebugSubjects subjects;
        private SubjectItem<EmContext> context;

        public DebugEventsImpl() {
            subjects = new DebugSubjects();
            context = new SubjectItem<>(subjects.getContext());
        }

        @Override
        public SubjectItem<EmContext> context() {
            return context;
        }

        @Override
        public void terminate() {
            context.onTerminateDetach();
            subjects.terminate();
        }
    }

    public class DebugSubjects extends SubjectManager {

        private ReplaySubject<EmContext> context;

        public DebugSubjects() {
            context = wrap(DebugSubjects.class, "context", ReplaySubject.create(1)); // NOI18N
        }

        public void terminate() {
            context.onComplete();
        }

        public ReplaySubject<EmContext> getContext() {
            return context;
        }

    }

}
