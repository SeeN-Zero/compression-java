package com.seen.compression.service;

import com.googlecode.pngtastic.core.PngImage;
import com.googlecode.pngtastic.core.PngOptimizer;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class MainService {

    @Value("${directory.path}")
    public String DIRECTORY;

    public File losseyCompression(MultipartFile input) throws IOException {
        System.out.println(DIRECTORY);
        File uncompress = new File(DIRECTORY + "/up/" + input.getOriginalFilename());
        File compress = new File(DIRECTORY + "/down/" + input.getOriginalFilename());
        input.transferTo(uncompress);

        Thumbnails.of(uncompress)
                .scale(1)
                .outputQuality(0.5)
                .toFile(compress);
        uncompress.delete();
        return compress;
    }

    public File losslessCompression(MultipartFile input) throws IOException {
        File uncompress = new File(DIRECTORY + "/up/" + input.getOriginalFilename());
        File compress = new File(DIRECTORY + "/down/" + input.getOriginalFilename());
        input.transferTo(uncompress);
        PngImage inputImage = new PngImage(Files.newInputStream(Paths.get(uncompress.getPath())));

        PngOptimizer optimizer = new PngOptimizer();
        PngImage optimized = optimizer.optimize(inputImage);

        OutputStream output = Files.newOutputStream(Paths.get(compress.getPath()));
        optimized.writeDataOutputStream(output);
        uncompress.delete();
        return compress;
    }

}
