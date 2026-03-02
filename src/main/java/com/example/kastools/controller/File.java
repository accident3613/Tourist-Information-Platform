package com.example.kastools.controller;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
public class File {
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "Please select a file to upload.";
        }


            // 使用相对路径，会在项目根目录创建 uploads 文件夹
            String uploadDir = "uploads";
        Path uploadPath = Paths.get(uploadDir);
        Path filePath = uploadPath.resolve(file.getOriginalFilename());
// 确保上传目录存在
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
// 保存文件
        file.transferTo(filePath);
        log.info(file.getOriginalFilename()+"  已上传至uploads文件夹");
            return "File uploaded successfully!";

    }

}
