package io.github.camshaft54.utils;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

// Adapted from https://stackoverflow.com/questions/3804361/how-to-enable-drag-and-drop-inside-jlist
public class DndJList extends JList<String> {
    DefaultListModel<String> myListModel;
    List<String> items;

    public DndJList(List<String> items) {
        this.items = items;
        myListModel = createStringListModel();
        setModel(myListModel);
        MyMouseAdaptor myMouseAdaptor = new MyMouseAdaptor();
        addMouseListener(myMouseAdaptor);
        addMouseMotionListener(myMouseAdaptor);
    }

    private class MyMouseAdaptor extends MouseInputAdapter {
        private boolean mouseDragging = false;
        private int dragSourceIndex;

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                dragSourceIndex = getSelectedIndex();
                mouseDragging = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseDragging = false;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (mouseDragging) {
                int currentIndex = locationToIndex(e.getPoint());
                if (currentIndex != dragSourceIndex) {
                    int dragTargetIndex = getSelectedIndex();
                    String dragElement = myListModel.get(dragSourceIndex);
                    myListModel.remove(dragSourceIndex);
                    myListModel.add(dragTargetIndex, dragElement);
                    dragSourceIndex = currentIndex;
                }
            }
        }
    }

    private DefaultListModel<String> createStringListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(items);
        return listModel;
    }

    // For testing purposes only
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new DndJList(Arrays.asList("Cat", "Dog", "Cow", "Horse", "Pig", "Monkey")));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}