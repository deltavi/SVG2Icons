package com.vincenzodevivo.svg2icons;

import com.vincenzodevivo.svg2icons.swing.MainPanel;
import com.vincenzodevivo.svg2icons.swing.dnd.DropFilesHandler;
import com.vincenzodevivo.svg2icons.transformer.IconSize;
import com.vincenzodevivo.svg2icons.transformer.SvgToPng;

import java.io.File;
import java.util.List;

/**
 * Hello world!
 */
public class SVG2IconsUI {
    public static void main(String[] args) {
        MainPanel dialog = new MainPanel();

        dialog.setDropFilesHandler(new DropFilesHandler() {
            @Override
            public void onDropFiles(List<File> files, List<IconSize> selectedSizeList, Runnable progress) {
                for (File file : files) {
                    for (IconSize size : selectedSizeList) {
                        try {
                            File outFolder = new File("./out/" + size.toString());
                            SvgToPng.convertFile(file, outFolder, size);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            progress.run();
                        }
                    }
                }
            }
        });
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
