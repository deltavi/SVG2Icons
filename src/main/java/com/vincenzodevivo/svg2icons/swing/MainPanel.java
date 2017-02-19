package com.vincenzodevivo.svg2icons.swing;

import com.vincenzodevivo.jdutils.file.FlatFileList;
import com.vincenzodevivo.svg2icons.swing.dnd.DropFilesHandler;
import com.vincenzodevivo.svg2icons.transformer.IconSize;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainPanel extends JFrame {
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
    private JSpinner spinnerCustomSize;
    private JCheckBox checkBoxCustom;
    private JToolBar toolBar;
    private JLabel dropLabel;
    private DropFilesHandler dropFilesHandler;

    @SuppressWarnings("unchecked")
    public MainPanel(String extension) {

        setContentPane(contentPane);
        //setModal(true);
        spinnerCustomSize.setModel(new SpinnerNumberModel(256, 1, 1000, 1));
        dropPanel.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    AtomicInteger count = new AtomicInteger();
                    if (dropFilesHandler != null) {
                        FlatFileList flatFileList = new FlatFileList(extension);
                        flatFileList.addFiles(droppedFiles);
                        final List<File> fileList = flatFileList.getList();
                        List<IconSize> selectedSizeList = getSelectedSizeList();

                        progressBar.setStringPainted(true);
                        progressBar.setMinimum(0);
                        int totalOutputFiles = fileList.size() * selectedSizeList.size();
                        progressBar.setMaximum(totalOutputFiles);
                        if (fileList.size() > 0 && selectedSizeList.size() > 0) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dropLabel.setText("Generating: 0/" + totalOutputFiles + " file/s");
                                    dropFilesHandler.onDropFiles(fileList, selectedSizeList, new Runnable() {
                                        @Override
                                        public void run() {
                                            SwingUtilities.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar.setValue(count.incrementAndGet());
                                                    if (count.get() != totalOutputFiles) {
                                                        dropLabel.setText("Generating: " + count.get() + "/" + totalOutputFiles + " file/s");
                                                    } else {
                                                        dropLabel.setText("Drag and drop SVGs here");
                                                    }
                                                }
                                            });
                                        }
                                    });

                                }
                            }).start();
                        } else {
                            progressBar.setMaximum(1);
                            progressBar.setValue(0);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public List<IconSize> getSelectedSizeList() {
        List<IconSize> selectedSizeList = new ArrayList<>();
        Component[] components = toolBar.getComponents();
        for (Component component : components) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    if (checkBox.equals(checkBoxCustom)) {
                        try {
                            spinnerCustomSize.commitEdit();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Object value = spinnerCustomSize.getModel().getValue();
                        selectedSizeList.add(new IconSize(value, value));
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
