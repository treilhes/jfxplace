package com.gluonhq.jfxapps.app.tray.app.menu;

import java.awt.Color;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.PopupMenuUI;
import javax.swing.plaf.SeparatorUI;

import dorkbox.systemTray.Entry;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.ui.swing.SwingUIFactory;

public class UiFactoryWrapper implements SwingUIFactory {

    private final SwingUIFactory wrapped;
    private final TrayMouseListener listener;

    public UiFactoryWrapper(SwingUIFactory wrapped, TrayMouseListener listener) {
        super();
        this.wrapped = wrapped;
        this.listener = listener;
    }

    @Override
    public PopupMenuUI getMenuUI(JPopupMenu jPopupMenu, Menu entry) {
        listener.map(jPopupMenu, entry);
        jPopupMenu.addMouseListener(listener);
        return wrapped.getMenuUI(jPopupMenu, entry);
    }

    @Override
    public MenuItemUI getItemUI(JMenuItem jMenuItem, Entry entry) {
        listener.map(jMenuItem, entry);
        jMenuItem.addMouseListener(listener);
        return wrapped.getItemUI(jMenuItem, entry);
    }

    @Override
    public SeparatorUI getSeparatorUI(JSeparator jSeparator) {
        return wrapped.getSeparatorUI(jSeparator);
    }

    @Override
    public String getCheckMarkIcon(Color color, int checkMarkSize, int targetImageSize) {
        return wrapped.getCheckMarkIcon(color, checkMarkSize, targetImageSize);
    }


}
