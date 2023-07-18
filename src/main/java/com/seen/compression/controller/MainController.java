package com.seen.compression.controller;

import com.seen.compression.service.MainService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
@AllArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @PostMapping("/upload-image-lossey")
    public ResponseEntity<byte[]> uploadImageLossey(@RequestParam("image") MultipartFile file) throws IOException {
        File compressed = mainService.losseyCompression(file);
        byte[] fileReturn = Files.readAllBytes(compressed.toPath());
        compressed.delete();
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-Disposition", "attachment; filename=" + file.getOriginalFilename())
                .body(fileReturn);
    }

    @PostMapping("/upload-image-lossless")
    public ResponseEntity<byte[]> uploadImageLossless(@RequestParam("image") MultipartFile file) throws IOException {
        File compressed = mainService.losslessCompression(file);
        byte[] fileReturn = Files.readAllBytes(compressed.toPath());
        compressed.delete();
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-Disposition", "attachment; filename=" + file.getOriginalFilename())
                .body(fileReturn);
    }
}
