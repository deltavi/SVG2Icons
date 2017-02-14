package com.vincenzodevivo.svg2icons.swing;

import com.vincenzodevivo.svg2icons.swing.dnd.DropFilesHandler;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainPanel extends JDialog {
    private JPanel contentPane;
    private JPanel dropPanel;
    private JProgressBar progressBar;
    private DropFilesHandler dropFilesHandler;


    public MainPanel() {
        setContentPane(contentPane);
        setModal(true);
        dropPanel.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>)
                            evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    AtomicInteger count = new AtomicInteger();
                    if (dropFilesHandler != null) {
                        progressBar.setStringPainted(true);
                        progressBar.setMinimum(0);
                        progressBar.setMaximum(droppedFiles.size());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                dropFilesHandler.onDropFiles(droppedFiles, () -> {
                                    SwingUtilities.invokeLater(() -> progressBar.setValue(count.incrementAndGet()));
                                });
                            }
                        }).start();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

            }
        });
    }

    public DropFilesHandler getDropFilesHandler() {
        return dropFilesHandler;
    }

    public void setDropFilesHandler(DropFilesHandler dropFilesHandler) {
        this.dropFilesHandler = dropFilesHandler;
    }
}
