package com.gluonhq.jfxapps.metadata.plugin.fork;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Forker {

    private final Log log;

    public Forker(Log log) {
        this.log = log;
    }

    public int fork(MavenProject project, List<Artifact> pluginArtifacts, String mainClass, Object params) throws CommandLineException, IOException {
        Jdk jdk = findJdk();
        String classpath = getClasspath(pluginArtifacts);
        return execute(project, jdk, classpath, mainClass, params);
    }

    private Jdk findJdk() {
        String jvmToUse = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        return new Jdk(jvmToUse);
    }

    private String getClasspath(List<Artifact> pluginArtifacts) {
        return pluginArtifacts.stream().map(a -> a.getFile().getAbsolutePath())
                .collect(Collectors.joining(File.pathSeparator));

    }

    private URL[] getRuntimeClasspathElements(MavenProject project) throws DependencyResolutionRequiredException, MalformedURLException {
        List<String> runtimeClasspathElements = project.getRuntimeClasspathElements();
        URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
        for (int i = 0; i < runtimeClasspathElements.size(); i++) {
            String element = runtimeClasspathElements.get(i).replace("\\", "/");
            Path path = Path.of(element);
            if (Files.exists(path) && Files.isDirectory(path) && !element.endsWith("/")) {
                element += "/";
            }
            runtimeUrls[i] = new URL("file://" + element);
        }
        return runtimeUrls;
    }

    private int execute(MavenProject project, Jdk jdk, String classpath, String mainClass, Object params) throws CommandLineException, IOException {

        File baseDir = project.getBasedir();
        File targetDir = new File(project.getBuild().getDirectory());
        File pluginTempDir = new File(targetDir, "fxom-metadata-maven-plugin");
        if (!pluginTempDir.exists()) {
            pluginTempDir.mkdirs();
        }

        File paramsFile = new File(pluginTempDir, "params.json");
        serializeParams(params, paramsFile);

        File javaProcessConfigFile = new File(pluginTempDir, "config.jvm");
        String javaProcessConfigContent = "-cp " + classpath + "\n";
        Files.writeString(javaProcessConfigFile.toPath(), javaProcessConfigContent);

        Commandline cl = new Commandline();
        cl.setWorkingDirectory(pluginTempDir);
        cl.setExecutable(jdk.getJavaBin().getAbsolutePath());

        cl.createArg().setValue("@" + javaProcessConfigFile.getAbsolutePath());
        cl.createArg().setValue(mainClass);
        cl.createArg().setValue(paramsFile.getAbsolutePath());

        log.info("Running command: " + cl.toString() + " within working directory: " + pluginTempDir.getAbsolutePath());

        StreamConsumer outConsumer = log::info;
        StreamConsumer errConsumer = log::error;

        return CommandLineUtils.executeCommandLine(cl, outConsumer, errConsumer);
    }

    private void serializeParams(Object params, File paramsFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(paramsFile, params);
        } catch (Exception e) {
            log.error("Failed to serialize parameters to JSON file: " + paramsFile.getAbsolutePath(), e);
        }
    }
}
