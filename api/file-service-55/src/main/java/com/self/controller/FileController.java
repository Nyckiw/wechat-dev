package com.self.controller;

import com.self.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/31
 */
@RestController
@RequestMapping("file")
public class FileController {
    @PostMapping("uploadFace")
    public GraceJSONResult uploadFace(@RequestParam MultipartFile file,
                                      String userId,
                                      HttpServletRequest request) throws Exception {
        String filename = file.getOriginalFilename();//文件原始路径
        String suffixName = filename.substring(filename.lastIndexOf("."));//从最后一个.开始截取
        String newFileName = userId + suffixName;//文件的新名称

        //设置文件存储路径 存放任意指定路径
        String rootPath = "F:\\自研\\SpringCloud+Netty集群实战千万级 IM系统\\temp" + File.separator;//File.separator为不同系统的斜杆
        String filePath = rootPath + File.separator + "face" + File.separator + newFileName;
        File newFile = new File(filePath);
        //判断目标文件是否存在
        if (!newFile.getParentFile().exists()) {
            //如果目标文件所在目录不存在  则创建父级目录
            newFile.getParentFile().mkdirs();
        }
        //将内存中数据写入磁盘
        file.transferTo(newFile);
        return GraceJSONResult.ok();

    }
}
