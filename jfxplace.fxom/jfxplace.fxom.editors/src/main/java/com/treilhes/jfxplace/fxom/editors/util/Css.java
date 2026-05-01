package com.treilhes.jfxplace.fxom.editors.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.treilhes.jfxplace.core.metadata.property.ValuePropertyMetadata;

import javafx.beans.property.ReadOnlyProperty;
import javafx.css.CssMetaData;
import javafx.css.Rule;
import javafx.css.Style;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;

public class Css {

    public static String getBeanPropertyName(StyleableProperty<?> val) {
        String property = null;
        if (val instanceof ReadOnlyProperty) {
            property = ((ReadOnlyProperty<?>) val).getName();
        }
        return property;
    }


    // If this property is ruled by CSS, return a CssPropAuthorInfo. Otherwise
    // returns null.
    public static CssPropAuthorInfo getCssInfo(Map<StyleableProperty, List<Style>> cssState, ValuePropertyMetadata prop) {
        CssPropAuthorInfo info = null;
        if (cssState != null) {
            info = getCssInfoFromState(cssState, prop);
        }
        return info;
    }


    private static CssPropAuthorInfo getCssInfoFromState(Map<StyleableProperty, List<Style>> cssState, ValuePropertyMetadata prop) {
        @SuppressWarnings("rawtypes")
        Map<StyleableProperty, List<Style>> map = cssState;
        for (@SuppressWarnings("rawtypes")
        Map.Entry<StyleableProperty, List<Style>> entry : map.entrySet()) {// NOI18N
            StyleableProperty<?> beanProp = entry.getKey();
            List<Style> styles = new ArrayList<>(entry.getValue());
            String name = getBeanPropertyName(beanProp);
            if (!name.equals(prop.getName().getName())) {
                continue;
            }
            if (name.equals(prop.getName().getName())) {
                // If the value has an origin of Author or Inline
                // then we have a property ruled by CSS, otherwise return null
                // This is in sync because the map is not empty
                StyleOrigin origin = beanProp.getStyleOrigin();
                if (origin == null || origin.equals(StyleOrigin.USER) || origin.equals(StyleOrigin.USER_AGENT)) {
                    return null;
                }
                CssMetaData<?, ?> styleable = beanProp.getCssMetaData();
                // Lookup the Author style
                CssPropAuthorInfo info = null;
                for (Style style : styles) {
                    Rule rule = style.getDeclaration().getRule();
                    assert rule != null;
                    // StyleOrigin can be null when the value is set to its initial value.
                    StyleOrigin o = rule.getOrigin();
                    if (o == null) {
                        return null;
                    }
                    if ((o.equals(StyleOrigin.AUTHOR) && (!isThemeStyle(style)))
                            || o.equals(StyleOrigin.INLINE)) {
                        if (info == null) {
                            info = new CssPropAuthorInfo(prop, beanProp, styleable);
                        }
                        info.getStyles().add(style);
                    }
                }
                return info;
            }
        }
        return null;
    }


    /**
     * Check if the input style is from a theme stylesheet (caspian or modena).
     *
     * @param style style to be checked
     * @return true if the style is from a theme css.
     */
    public static boolean isThemeStyle(Style style) {
        return isThemeRule(style.getDeclaration().getRule());
    }


//  TODO why those methods, they are breaking theme encapsulation
    public static boolean isThemeRule(Rule rule) {
        String stylePath = rule.getStylesheet().getUrl();
        assert stylePath != null;

        // FIXME temp fix to remove, worst: gluon is not event present

        List<String> themeUrls = new ArrayList<>();

        //TODO uncomment when solution provided
//        new DefaultThemesList().themes().forEach(t -> {
//            try {
//                themeUrls.add(t.newInstance().getUserAgentStylesheet());
//            } catch (InstantiationException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        });

        for (String themeUrl : themeUrls) {
            if (stylePath.endsWith(themeUrl)) {
                return true;
            }
        }
        return false;
    }
}
