package com.self.controller;


import com.self.api.feign.UserInfoMicroServiceFeign;
import com.self.config.MinIOConfig;
import com.self.config.MinIOUtils;
import com.self.grace.result.GraceJSONResult;
import com.self.utils.JsonUtils;
import io.minio.ObjectWriteResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vo.UserVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static com.self.grace.result.ResponseStatusEnum.FILE_UPLOAD_FAILD;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/31
 */

@RestController
@RequestMapping("file")
public class FileController {
    private final MinIOConfig minIOConfig;
    @Resource
    private UserInfoMicroServiceFeign userInfoMicroServiceFeign;

    public FileController(MinIOConfig minIOConfig) {
        this.minIOConfig = minIOConfig;
    }

    @PostMapping("uploadFace1")
    public GraceJSONResult uploadFace1(@RequestParam MultipartFile file,
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

    @PostMapping("uploadFace")
    public GraceJSONResult uploadFace(@RequestParam MultipartFile file,
                                      String userId,
                                      HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return GraceJSONResult.errorCustom(FILE_UPLOAD_FAILD);
        }
        String filename = file.getOriginalFilename();//获取文件原始名称
        if (StringUtils.isBlank(filename)) {
            return GraceJSONResult.errorCustom(FILE_UPLOAD_FAILD);
        }
        filename = "face" + "_" + userId + "_" + filename;
        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                filename,
                file.getInputStream());
        String fileHost = MinIOUtils.getBasisUrl()
                + filename;
//        return GraceJSONResult.ok(fileHost);
        /**
         * 微服务远程调用更新用户头像到数据库 openfeign
         * 如果前端没有保存按钮可以这么做 如果有保存提交按钮 则在前端触发
         * 此处不需要进行微服务调用 让前端触发保存提交到后台保存
         */
        GraceJSONResult jsonResult = userInfoMicroServiceFeign.updateFace(userId, fileHost);
        Object data = jsonResult.getData();
        String json = JsonUtils.objectToJson(data);
        UserVO userVO = JsonUtils.jsonToPojo(json, UserVO.class);
        return GraceJSONResult.ok(userVO);
    }

}
