package com.vincenzodevivo.svg2icons.transformer;

import com.vincenzodevivo.jdutils.file.FileUtils;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Vincenzo De Vivo on 16/02/2017.
 */
public class SvgToPng {
    public static void convertFile(File input, File folderOut, IconSize size) throws IOException, TranscoderException {
        TranscoderInput ti = new TranscoderInput(input.toURI().toString());
        PNGTranscoder t = new PNGTranscoder();

        t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, size.getWidth());
        t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, size.getHeight());

        folderOut.mkdirs();

        String fileName = FileUtils.changeExtension(input.getName(), size.toString() + ".png");
        File outputFile = new File(folderOut, fileName);
        outputFile.createNewFile();
        try (FileOutputStream outStram = new FileOutputStream(outputFile)) {
            t.transcode(ti, new TranscoderOutput(outStram));
        }
        System.out.println("Output file: " + outputFile);
    }
}
