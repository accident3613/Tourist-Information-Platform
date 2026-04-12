package com.example.kastools.controller;

import com.example.kastools.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class File {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String IconUpdate(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, ExecutionException, InterruptedException {
        return fileService.uploadIcon(file, request);
    }

    @GetMapping("/icon")
    public ResponseEntity<Resource> icon(@RequestParam("filePath") String filePath) throws IOException {
        return fileService.getIcon(filePath);
    }

    @GetMapping("/sicon")
    public ResponseEntity<Resource> sicon(@RequestParam("filePath") String filePath) throws IOException {
        return fileService.getSiteIcon(filePath);
    }
}
