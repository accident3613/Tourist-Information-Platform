package com.example.kastools.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface FileService {

    String uploadIcon(MultipartFile file, HttpServletRequest request) throws IOException, ExecutionException, InterruptedException;

    ResponseEntity<Resource> getIcon(String filePath) throws IOException;

    ResponseEntity<Resource> getSiteIcon(String filePath) throws IOException;

    CompletableFuture<String> asyncUpload(String username, MultipartFile file) throws IOException;

    String uploadSiteIcon(MultipartFile file) throws IOException;
}
