package com.max.reggle.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpResponse;
import com.max.reggle.utli.R;
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

/**
 * @author 麦家宝
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
       //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取结尾.jpg or .xxx
        Object s = originalFilename.substring(originalFilename.lastIndexOf("."));

        //有命名重复的可能性，所以通过uuid来设置
        String filename = UUID.randomUUID().toString() + s;
        System.out.println(basePath + filename);
//        System.out.println(filename + "." + split[1]);
        //创建文件夹
        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  R.success(filename);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流，通过输入流读取文件内容
        String filepath = basePath + name;
        try {
            ServletOutputStream outputStream = response.getOutputStream();

            FileInputStream inputStream = new FileInputStream(new File(filepath));
            response.setContentType("image/jpg");
            byte[] bytes = new byte[1024];
            int len = 0;
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            while ((len = inputStream.read(bytes)) != -1){

                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //关闭输入流
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
