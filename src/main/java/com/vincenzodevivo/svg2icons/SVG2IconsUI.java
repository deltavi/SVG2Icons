package com.vincenzodevivo.svg2icons;

import com.vincenzodevivo.svg2icons.swing.MainPanel;
import com.vincenzodevivo.svg2icons.swing.dnd.DropFilesHandler;
import com.vincenzodevivo.svg2icons.transformer.IconSize;
import com.vincenzodevivo.svg2icons.transformer.SvgToPng;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class SVG2IconsUI {
    public static void main(String[] args) {
        MainPanel dialog = new MainPanel("svg");
        String[] icons = new String[]{
                "main-icon.16x16.png",
                "main-icon.32x32.png",
                "main-icon.48x48.png",
                "main-icon.64x64.png"
        };
        List<Image> images = new ArrayList<>();
        URL resource;
        for (String icon : icons) {
            resource = dialog.getClass().getResource("/icons/"+icon);
            images.add(Toolkit.getDefaultToolkit().getImage(resource));
        }

        dialog.setIconImages(images);
        String outputDir = "./out/";
        dialog.setDropFilesHandler(new DropFilesHandler() {
            @Override
            public void onDropFiles(List<File> files, List<IconSize> selectedSizeList, Runnable progress) {
                for (File file : files) {
                    for (IconSize size : selectedSizeList) {
                        try {
                            File outFolder = new File(outputDir + size.toString());
                            SvgToPng.convertFile(file, outFolder, size);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            progress.run();
                        }
                    }
                }
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(new File(outputDir));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialog.pack();
        Dimension size = new Dimension(600, 400);
        dialog.setSize(size);
        dialog.setMinimumSize(size);
        dialog.setTitle("SVG2Icons v.1.0");
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
