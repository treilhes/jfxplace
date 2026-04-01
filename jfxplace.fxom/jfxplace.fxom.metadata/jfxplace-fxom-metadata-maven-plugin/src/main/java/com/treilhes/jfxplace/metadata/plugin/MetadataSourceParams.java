package com.treilhes.jfxplace.metadata.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MetadataSourceParams extends MetadataParams {
    private File sourceFolder;
    private File inputResourceFolder;
    private String targetPackage;
    private String moduleName;
    private List<String> moduleRequires = new ArrayList<>();
    private String parentUuid;
    private String uuid;
    private String extensionName;
    private String metadataPrefix;
    private String templateForComponentCustomization;
    private String templateForComponentPropertyCustomization;
    private String templateForValuePropertyCustomization;
    private String templateForStaticValuePropertyCustomization;
    private String templateForComponentConstructorCustomization;
    private String targetComponentSuperClass;
    private String targetComponentCustomizationClass;
    private String targetComponentPropertyCustomizationClass;
    private String targetValuePropertyCustomizationClass;

    public File getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(File sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public File getInputResourceFolder() {
        return inputResourceFolder;
    }

    public void setInputResourceFolder(File inputResourceFolder) {
        this.inputResourceFolder = inputResourceFolder;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<String> getModuleRequires() {
        return moduleRequires;
    }

    public void setModuleRequires(List<String> moduleRequires) {
        this.moduleRequires = moduleRequires;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public void setMetadataPrefix(String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
    }

    public String getTemplateForComponentCustomization() {
        return templateForComponentCustomization;
    }

    public void setTemplateForComponentCustomization(String templateForComponentCustomization) {
        this.templateForComponentCustomization = templateForComponentCustomization;
    }

    public String getTemplateForComponentPropertyCustomization() {
        return templateForComponentPropertyCustomization;
    }

    public void setTemplateForComponentPropertyCustomization(String templateForComponentPropertyCustomization) {
        this.templateForComponentPropertyCustomization = templateForComponentPropertyCustomization;
    }

    public String getTemplateForValuePropertyCustomization() {
        return templateForValuePropertyCustomization;
    }

    public void setTemplateForValuePropertyCustomization(String templateForValuePropertyCustomization) {
        this.templateForValuePropertyCustomization = templateForValuePropertyCustomization;
    }

    public String getTemplateForStaticValuePropertyCustomization() {
        return templateForStaticValuePropertyCustomization;
    }

    public void setTemplateForStaticValuePropertyCustomization(String templateForStaticValuePropertyCustomization) {
        this.templateForStaticValuePropertyCustomization = templateForStaticValuePropertyCustomization;
    }

    public String getTemplateForComponentConstructorCustomization() {
        return templateForComponentConstructorCustomization;
    }

    public void setTemplateForComponentConstructorCustomization(String templateForComponentConstructorCustomization) {
        this.templateForComponentConstructorCustomization = templateForComponentConstructorCustomization;
    }

    public String getTargetComponentSuperClass() {
        return targetComponentSuperClass;
    }

    public void setTargetComponentSuperClass(String targetComponentSuperClass) {
        this.targetComponentSuperClass = targetComponentSuperClass;
    }

    public String getTargetComponentCustomizationClass() {
        return targetComponentCustomizationClass;
    }

    public void setTargetComponentCustomizationClass(String targetComponentCustomizationClass) {
        this.targetComponentCustomizationClass = targetComponentCustomizationClass;
    }

    public String getTargetComponentPropertyCustomizationClass() {
        return targetComponentPropertyCustomizationClass;
    }

    public void setTargetComponentPropertyCustomizationClass(String targetComponentPropertyCustomizationClass) {
        this.targetComponentPropertyCustomizationClass = targetComponentPropertyCustomizationClass;
    }

    public String getTargetValuePropertyCustomizationClass() {
        return targetValuePropertyCustomizationClass;
    }

    public void setTargetValuePropertyCustomizationClass(String targetValuePropertyCustomizationClass) {
        this.targetValuePropertyCustomizationClass = targetValuePropertyCustomizationClass;
    }
}