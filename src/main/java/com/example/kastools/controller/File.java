package com.example.kastools.controller;
import ch.qos.logback.classic.Logger;
import com.example.kastools.mapper.UsrMap;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
public class File {
    @Autowired
    Jwt jwt;
    @Autowired
    UsrMap usrMap;
    @PostMapping("/upload")
    public String IconUpdate(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        if (file.isEmpty()) {
            return "Please select a file to upload.";
        }
String token=request.getHeader("token");
String username= jwt.getusn(token);
            // 使用相对路径
            String uploadDir = "src/main/resources/static/icon/";
        Path uploadPath = Paths.get(uploadDir);
        // 获取文件扩展名
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

// 生成UUID文件名
        String uuidFileName = UUID.randomUUID().toString() + fileExtension;

        Path filePath = uploadPath.resolve(uuidFileName);
        String path = uuidFileName;
// 确保上传目录存在
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
// 保存文件
        file.transferTo(filePath);
        log.info(file.getOriginalFilename()+"  已上传至icon文件夹");
        usrMap.upimap(path,username);
            return "File uploaded successfully!";

    }
    @GetMapping("/icon")
    public ResponseEntity<Resource> icon(@RequestParam("filePath") String filePath) throws IOException {
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

        Resource resource = new FileSystemResource(file);

        // 对文件名进行URL编码，防止中文文件名乱码
        String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString())
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

}
