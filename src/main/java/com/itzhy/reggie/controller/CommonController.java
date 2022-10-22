package com.itzhy.reggie.controller;

import com.itzhy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
}
