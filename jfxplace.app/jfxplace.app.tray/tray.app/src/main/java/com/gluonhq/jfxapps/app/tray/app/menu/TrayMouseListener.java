package com.gluonhq.jfxapps.app.tray.app.menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class TrayMouseListener implements MouseListener {

    private final BiConsumer<MouseEvent, Object> consumer;
    private final Map<Object, Object> map = new HashMap<Object, Object>();

    public TrayMouseListener(BiConsumer<MouseEvent, Object> consumer) {
        super();
        this.consumer = consumer;
    }

    public void map(Object source, Object target) {
        //System.out.println("Register " + source + " with " + target);
        map.put(source, target);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        var o = map.get(e.getSource());
        consumer.accept(e, o);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        var o = map.get(e.getSource());
        consumer.accept(e, o);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        var o = map.get(e.getSource());
        consumer.accept(e, o);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        var o = map.get(e.getSource());
        consumer.accept(e, o);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        var o = map.get(e.getSource());
        consumer.accept(e, o);
    }

}
