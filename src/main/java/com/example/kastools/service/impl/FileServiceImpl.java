package com.example.kastools.service.impl;

import com.example.kastools.mapper.UsrMap;
import com.example.kastools.service.FileService;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private Jwt jwt;

    @Autowired
    private UsrMap usrMap;

    @Override
    public String uploadIcon(MultipartFile file, HttpServletRequest request) throws IOException, ExecutionException, InterruptedException {
        if (file.isEmpty()) {
            return "请选择图片";
        }
        String token = request.getHeader("token");
        String username = jwt.getusn(token);

        return asyncUpload(username, file).get();
    }

    @Override
    public ResponseEntity<Resource> getIcon(String filePath) throws IOException {
        // 构建完整的文件路径，指向 static/icon/ 目录
        String basePath = "src/main/resources/static/icon/";
        java.io.File file = new java.io.File(basePath + filePath);

        // 如果上面的路径找不到，尝试使用绝对路径
        if (!file.exists()) {
            file = new java.io.File(filePath);
        }

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);  //加载文件

        // 对文件名进行URL编码，防止中文文件名乱码
        String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString())
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

    @Override
    public ResponseEntity<Resource> getSiteIcon(String filePath) throws IOException {
        // 构建完整的文件路径，指向 static/icon/ 目录
        String basePath = "src/main/resources/static/sicon/";
        java.io.File file = new java.io.File(basePath + filePath);

        // 如果上面的路径找不到，尝试使用绝对路径
        if (!file.exists()) {
            file = new java.io.File(filePath);
        }

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        // 对文件名进行URL编码，防止中文文件名乱码
        String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString())
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

    @Override
    @Async("pool")
    public CompletableFuture<String> asyncUpload(String username, MultipartFile file) throws IOException {
        // 使用相对路径
        String uploadDir = "src/main/resources/static/icon/";
        Path uploadPath = Paths.get(uploadDir);
        // 获取文件扩展名
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 生成UUID文件名
        String uuidFileName = UUID.randomUUID() + fileExtension;

        Path filePath = uploadPath.resolve(uuidFileName);
        String path = uuidFileName;
        // 确保上传目录存在
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // 保存文件
        file.transferTo(filePath);
        log.info(file.getOriginalFilename() + "  已上传至icon文件夹");
        usrMap.upimap(path, username);
        return CompletableFuture.completedFuture("File uploaded successfully!");
    }
}
