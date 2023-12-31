package com.wechat.controller;

import com.wechat.result.Response;
import com.wechat.result.ResultCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @date: 2023/07/11
 * @description:
 */
@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 小程序api上传图片，name要保值一致，比如现在name=file，那前后端都要命名成file
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "上传图片")
    @PostMapping("/upload")
    public Object uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        // 检查文件是否为空
        if (file.isEmpty()) {
            // 处理文件为空的情况
            return Response.failure(ResultCode.NOT_FOUND);
        }

        // 生成文件存储路径
        String storagePath;
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            storagePath = "D:/images/" + LocalDate.now();
        } else {
            storagePath = "/images/" + LocalDate.now();
        }
        File storageDirectory = new File(storagePath);
        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        // 指定存储路径
        String filePath = storagePath + "/" + fileName;
        // 将文件保存到指定路径
        File dest = new File(filePath);
        FileCopyUtils.copy(file.getBytes(), dest);

        String thumbnailPath = storagePath + "/" + System.currentTimeMillis() + "_tn.jpg";
        try {
            BufferedImage originalImage = ImageIO.read(new File(filePath));
            BufferedImage thumbnail = generateThumbnail(originalImage, 100, 100);
            saveThumbnail(thumbnail, thumbnailPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("original", filePath);
        map.put("thumbnail", thumbnailPath);
        return Response.success(map);
    }

    /**
     * 生成缩略图
     * @param originalImage
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage generateThumbnail(BufferedImage originalImage, int width, int height) {
        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = thumbnail.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return thumbnail;
    }

    public static void saveThumbnail(BufferedImage thumbnail, String outputPath) throws IOException {
        String format = "jpg"; // You can choose other image formats like "PNG" or "GIF" if needed
        ImageIO.write(thumbnail, format, new File(outputPath));
    }

    @ApiOperation(value = "获取图片")
    @GetMapping("/images/{date}/{filename:.+}")
    public ResponseEntity images(@PathVariable String date, @PathVariable String filename) {
        // 创建文件资源
        Resource resource = new FileSystemResource("/images/" + "/" + date + "/" + filename);
        // 检查文件是否存在
        if (!resource.exists()) {
            // 处理文件不存在的情况，返回404错误
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename());
        headers.setContentType(MediaType.IMAGE_JPEG); // 设置Content-Type为image/jpeg
        // 返回图片资源和其他字段
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @ApiOperation(value = "获取文件")
    @GetMapping("/images/checkIn/{date}/{filename:.+}")
    public ResponseEntity file(@PathVariable String date, @PathVariable String filename) throws Exception {

        // 创建文件资源
        Resource resource = new FileSystemResource("/images/checkIn" + "/" + date + "/" + URLEncoder.encode(filename, "UTF-8"));
        // 检查文件是否存在
        if (!resource.exists()) {
            // 处理文件不存在的情况，返回404错误
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 设置Content-Type为image/jpeg
        // 返回图片资源和其他字段
        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
