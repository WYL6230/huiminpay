package com.huiminpay.common.cache.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;

/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/23 9:46
 * @DESC:
 */
@Slf4j
public class QiniuUtil {

    public static void upload(String accessKey, String secretKey,String bucket,String key,byte[] uploadBytes){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//        String s = JSON.toJSONString(cfg); //{birthday:2019-01-01}
//        Configuration configuration = JSON.parseObject(s, Configuration.class);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {

            //认证
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                System.out.println("==================================="+putRet.key);
//                System.out.println("==================================="+putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                log.error("把文件上传到七牛云异常:{}",r.toString());
                throw new RuntimeException("把文件上传到七牛云异常");
               /* try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }*/
            }
        } catch (Exception ex) {
            //ignore
            log.error("把文件上传到七牛云异常:{}",ex.getMessage());
            throw new RuntimeException("把文件上传到七牛云异常");
        }
    }

    public static String uploadImg(String accessKey, String secretKey,String bucket,byte[] uploadBytes){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {

            //认证
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            String key = null;
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return putRet.key;

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                log.error("把文件上传到七牛云异常:{}",r.toString());
                throw new RuntimeException("把文件上传到七牛云异常");

            }
        } catch (Exception ex) {
            //ignore
            log.error("把文件上传到七牛云异常:{}",ex.getMessage());
            throw new RuntimeException("把文件上传到七牛云异常");
        }
    }
}
