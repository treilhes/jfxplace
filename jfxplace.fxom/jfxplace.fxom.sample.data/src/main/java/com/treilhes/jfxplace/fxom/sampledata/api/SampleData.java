package com.treilhes.jfxplace.fxom.sampledata.api;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javafx.scene.paint.Color;

public interface SampleData {

    public static final String SAMPLE_DATA_KEY = "sampleData";

    boolean canApplyTo(@Nullable Object sceneGraphObject);

    void applyTo(@NonNull Object sceneGraphObject);

    void removeFrom(@NonNull Object sceneGraphObject);


    static final String[] lorem = {
        "Lorem ipsum ", //NOCHECK
        "dolor sit amet, ", //NOCHECK
        "consectetur adipiscing elit. ", //NOCHECK
        "Donec eu justo ", //NOCHECK
        "at tortor porta ", //NOCHECK
        "commodo nec vitae magna. ", //NOCHECK
        "Maecenas tempus ", //NOCHECK
        "hendrerit elementum. ", //NOCHECK
        "Nam sed mi ", //NOCHECK
        "a lorem tincidunt ", //NOCHECK
        "luctus sed non sem. ", //NOCHECK
        "Aliquam erat volutpat. ", //NOCHECK
        "Donec tempus egestas ", //NOCHECK
        "libero a cursus. ", //NOCHECK
        "In lectus nunc, ", //NOCHECK
        "dapibus vel suscipit vel, ", //NOCHECK
        "faucibus eget justo. ", //NOCHECK
        "Aliquam erat volutpat. ", //NOCHECK
        "Nulla facilisi. ", //NOCHECK
        "Donec at enim ipsum, ", //NOCHECK
        "sed facilisis leo. ", //NOCHECK
        "Aliquam tincidunt ", //NOCHECK
        "adipiscing euismod. ", //NOCHECK
        "Sed aliquet eros ", //NOCHECK
        "ut libero congue ", //NOCHECK
        "quis bibendum ", //NOCHECK
        "felis ullamcorper. ", //NOCHECK
        "Vestibulum ipsum ante, ", //NOCHECK
        "semper eu sollicitudin rutrum, ", //NOCHECK
        "consectetur a enim. ", //NOCHECK
        "Ut eget nisl sed turpis ", //NOCHECK
        "egestas viverra ", //NOCHECK
        "ut tristique sem. ", //NOCHECK
        "Nunc in neque nulla. " //NOCHECK
    };

    static final Color[] colors = {
        Color.AZURE, Color.CHARTREUSE, Color.CRIMSON, Color.DARKCYAN
    };

    static final String[] alphabet = {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", //NOCHECK
        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" //NOCHECK
    };

    /*
     * Utilites for subclasses
     */

    static String lorem(int index) {
        return lorem[index % lorem.length];
    }

    static Color color(int index) {
        return colors[index % colors.length];
    }

    static String alphabet(int index) {
        return alphabet[index % alphabet.length];
    }
}