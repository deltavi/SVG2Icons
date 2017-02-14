package com.vincenzodevivo.svg2icons;

import com.vincenzodevivo.svg2icons.swing.MainPanel;
import com.vincenzodevivo.svg2icons.swing.dnd.DropFilesHandler;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Hello world!
 */
public class SVG2IconsUI {
    public static void main(String[] args) {
        MainPanel dialog = new MainPanel();
        dialog.setDropFilesHandler((files, progress) -> {
            for (File file : files) {
                try {
                    System.out.println(file);
                    Thread.sleep(1000);
                    progress.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
