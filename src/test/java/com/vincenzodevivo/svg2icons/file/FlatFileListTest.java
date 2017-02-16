package com.vincenzodevivo.svg2icons.file;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vincenzo De Vivo on 15/02/2017.
 */
public class FlatFileListTest {
    @Test
    public void addFiles() throws Exception {
        FlatFileList fileList = new FlatFileList("java");
        fileList.addFiles(new File("."));
        List<File> list = fileList.getList();
        assertTrue("list.size() < 0", list.size() > 0);
        for (File file : list) {
            System.out.println(file);
        }
    }
}