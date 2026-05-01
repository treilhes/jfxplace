package com.treilhes.jfxplace.metadata.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.treilhes.jfxplace.metadata.plugin.params.ConstructorOverride;

public class MetadataParams {
    private List<String> rootClasses = new ArrayList<>();
    private List<String> excludeClasses = new ArrayList<>();
    private List<String> jarFilterPatterns = new ArrayList<>();
    private List<String> includePackages = new ArrayList<>();
    private List<String> excludePackages = new ArrayList<>();
    private List<ConstructorOverride> constructorOverrides = new ArrayList<>();
    private File outputResourceFolder;
    private String componentCustomizationClass;
    private String componentPropertyCustomizationClass;
    private String valuePropertyCustomizationClass;
    private boolean globalReportEanbled;
    private String javafxVersion;
    private List<File> jarFiles = new ArrayList<>();

    public List<String> getRootClasses() {
        return rootClasses;
    }

    public void setRootClasses(List<String> rootClasses) {
        this.rootClasses = rootClasses;
    }

    public List<String> getExcludeClasses() {
        return excludeClasses;
    }

    public void setExcludeClasses(List<String> excludeClasses) {
        this.excludeClasses = excludeClasses;
    }

    public List<String> getJarFilterPatterns() {
        return jarFilterPatterns;
    }

    public void setJarFilterPatterns(List<String> jarFilterPatterns) {
        this.jarFilterPatterns = jarFilterPatterns;
    }

    public List<String> getIncludePackages() {
        return includePackages;
    }

    public void setIncludePackages(List<String> includePackages) {
        this.includePackages = includePackages;
    }

    public List<String> getExcludePackages() {
        return excludePackages;
    }

    public void setExcludePackages(List<String> excludePackages) {
        this.excludePackages = excludePackages;
    }

    public List<ConstructorOverride> getConstructorOverrides() {
        return constructorOverrides;
    }

    public void setConstructorOverrides(List<ConstructorOverride> constructorOverrides) {
        this.constructorOverrides = constructorOverrides;
    }

    public File getOutputResourceFolder() {
        return outputResourceFolder;
    }

    public void setOutputResourceFolder(File outputResourceFolder) {
        this.outputResourceFolder = outputResourceFolder;
    }

    public String getComponentCustomizationClass() {
        return componentCustomizationClass;
    }

    public void setComponentCustomizationClass(String componentCustomizationClass) {
        this.componentCustomizationClass = componentCustomizationClass;
    }

    public String getComponentPropertyCustomizationClass() {
        return componentPropertyCustomizationClass;
    }

    public void setComponentPropertyCustomizationClass(String componentPropertyCustomizationClass) {
        this.componentPropertyCustomizationClass = componentPropertyCustomizationClass;
    }

    public String getValuePropertyCustomizationClass() {
        return valuePropertyCustomizationClass;
    }

    public void setValuePropertyCustomizationClass(String valuePropertyCustomizationClass) {
        this.valuePropertyCustomizationClass = valuePropertyCustomizationClass;
    }

    public boolean isGlobalReportEanbled() {
        return globalReportEanbled;
    }

    public void setGlobalReportEanbled(boolean globalReportEanbled) {
        this.globalReportEanbled = globalReportEanbled;
    }

    public String getJavafxVersion() {
        return javafxVersion;
    }

    public void setJavafxVersion(String javafxVersion) {
        this.javafxVersion = javafxVersion;
    }

    public List<File> getJarFiles() {
        return jarFiles;
    }

    public void setJarFiles(List<File> jarFiles) {
        this.jarFiles = jarFiles;
    }
}