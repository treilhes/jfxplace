package com.gluonhq.jfxapps.core.library.manager;

import com.gluonhq.jfxapps.core.api.fxom.library.LibraryArtifact;
import com.treilhes.emc4j.boot.api.maven.ResolvedArtifact;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-01T17:55:29+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class LibraryMappersImpl implements LibraryMappers {

    @Override
    public LibraryArtifact map(ResolvedArtifact resolvedArtifact) {
        if ( resolvedArtifact == null ) {
            return null;
        }

        LibraryArtifact libraryArtifact = new LibraryArtifact();

        libraryArtifact.setGroupId( resolvedArtifact.getGroupId() );
        libraryArtifact.setArtifactId( resolvedArtifact.getArtifactId() );
        libraryArtifact.setVersion( resolvedArtifact.getVersion() );

        libraryArtifact.setJarList( resolvedArtifact.toPaths() );

        return libraryArtifact;
    }
}
