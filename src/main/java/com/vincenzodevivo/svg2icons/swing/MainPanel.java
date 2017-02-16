package com.vincenzodevivo.svg2icons.swing;

import com.vincenzodevivo.svg2icons.file.FlatFileList;
import com.vincenzodevivo.svg2icons.swing.dnd.DropFilesHandler;
import com.vincenzodevivo.svg2icons.transformer.IconSize;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainPanel extends JDialog {
    private JPanel contentPane;
    private JPanel dropPanel;
    private JProgressBar progressBar;
    private JCheckBox checkBox16x16;
    private JCheckBox checkBox24x24;
    private JCheckBox checkBox32x32;
    private JCheckBox checkBox48x48;
    private JCheckBox checkBox64x64;
    private JCheckBox checkBox128x128;
    private JCheckBox checkBox20x20;
    private JSpinner spinnerHeight;
    private JSpinner spinnerWidth;
    private JCheckBox checkBoxCustom;
    private JToolBar toolBar;
    private DropFilesHandler dropFilesHandler;


    public MainPanel() {
        setContentPane(contentPane);
        setModal(true);
        spinnerWidth.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        spinnerHeight.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        dropPanel.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    AtomicInteger count = new AtomicInteger();
                    if (dropFilesHandler != null) {
                        FlatFileList fileList = new FlatFileList("svg");
                        for (File file : droppedFiles) {
                            fileList.addFiles(file);
                        }
                        progressBar.setStringPainted(true);
                        progressBar.setMinimum(0);
                        List<IconSize> selectedSizeList = getSelectedSizeList();
                        progressBar.setMaximum(fileList.getList().size() * selectedSizeList.size());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                dropFilesHandler.onDropFiles(fileList.getList(), selectedSizeList, new Runnable() {
                                    @Override
                                    public void run() {
                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setValue(count.incrementAndGet());
                                            }
                                        });
                                    }
                                });
                            }
                        }).start();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public List<IconSize> getSelectedSizeList() {
        List<IconSize> selectedSizeList = new ArrayList<>();
        Component[] components = toolBar.getComponents();
        for (Component component : components) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    if (checkBox.equals(checkBoxCustom)) {
                        selectedSizeList.add(new IconSize(spinnerWidth.getValue(), spinnerHeight.getValue()));
                    } else {
                        selectedSizeList.add(new IconSize(checkBox.getText()));
                    }
                }
            }
        }
        return selectedSizeList;
    }

    public DropFilesHandler getDropFilesHandler() {
        return dropFilesHandler;
    }

    public void setDropFilesHandler(DropFilesHandler dropFilesHandler) {
        this.dropFilesHandler = dropFilesHandler;
    }
}
