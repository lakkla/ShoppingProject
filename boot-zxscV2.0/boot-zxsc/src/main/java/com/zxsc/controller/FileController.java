package com.zxsc.controller;

import com.zxsc.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {
    
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.error("文件名不能为空");
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!extension.matches("\\.(jpg|jpeg|png|gif|bmp)")) {
            return Result.error("只支持图片格式：jpg、jpeg、png、gif、bmp");
        }
        
        String filename = UUID.randomUUID().toString() + extension;
        
        String uploadPath = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "images";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                return Result.error("创建上传目录失败");
            }
        }
        
        File dest = new File(uploadDir, filename);
        try {
            file.transferTo(dest);
            String url = "/upload/images/" + filename;
            return Result.success("上传成功", url);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}
