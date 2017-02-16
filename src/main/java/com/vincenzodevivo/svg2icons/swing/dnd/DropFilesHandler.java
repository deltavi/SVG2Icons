package com.vincenzodevivo.svg2icons.swing.dnd;

import com.vincenzodevivo.svg2icons.transformer.IconSize;

import java.io.File;
import java.util.List;

/**
 * Created by Vincenzo De Vivo on 14/02/2017.
 */
public interface DropFilesHandler {
    void onDropFiles(List<File> files, List<IconSize> selectedSizeList, Runnable runnable);
}
