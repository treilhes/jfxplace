package com.treilhes.jfxplace.metadata.plugin.fork;

import java.io.File;

public class Jdk {

    private final File javaBin;
    private final File jdkHome;

    public Jdk(String javaBinPath) {
        this.javaBin = new File(javaBinPath);
        this.jdkHome = toJdkHomeFromJvmExec(javaBinPath);
    }

    public File getJavaBin() {
        return javaBin;
    }

    public File getJdkHome() {
        return jdkHome;
    }

    /**
     * If {@code jvmExecutable} is <code>/jdk/bin/java</code> (since jdk9) or <code>/jdk/jre/bin/java</code>
     * (prior to jdk9) then the absolute path to JDK home is returned <code>/jdk</code>.
     * <br>
     * Null is returned if {@code jvmExecutable} is incorrect.
     *
     * @param jvmExecutable    /jdk/bin/java* or /jdk/jre/bin/java*
     * @return path to jdk directory; or <code>null</code> if wrong path or directory layout of JDK installation
     */
    private static File toJdkHomeFromJvmExec(String jvmExecutable) {
        File bin = new File(jvmExecutable).getAbsoluteFile().getParentFile();
        if ("bin".equals(bin.getName())) {
            File parent = bin.getParentFile();
            if ("jre".equals(parent.getName())) {
                File jdk = parent.getParentFile();
                return new File(jdk, "bin").isDirectory() ? jdk : null;
            }
            return parent;
        }
        return null;
    }
}

