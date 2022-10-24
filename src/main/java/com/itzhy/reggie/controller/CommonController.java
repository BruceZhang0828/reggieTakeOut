package com.itzhy.reggie.controller;

import com.itzhy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author zhy
 * @description 通用的类
 * @date 2022/10/22 17:03
 **/
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.file-path}")
    private String BASE_PATH;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 截取文件后缀
        int i = originalFilename.lastIndexOf(".");
        String fix = originalFilename.substring(i);
        // 新文件名
        String fileName = UUID.randomUUID().toString() + fix;
        File dir = new File(BASE_PATH);
        if (!dir.exists()) {
            // 目录不存在，创建目录
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(BASE_PATH + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    // 文件下载
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        // 文件读取流
        try {
            FileInputStream input = new FileInputStream(new File(BASE_PATH + name));
            // 文件输出流
            ServletOutputStream output = response.getOutputStream();
            // 设置
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = input.read(bytes))!= -1) {
                output.write(bytes,0,len);
                output.flush();
            }
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
