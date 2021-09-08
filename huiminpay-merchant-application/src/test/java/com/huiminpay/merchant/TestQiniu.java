package com.huiminpay.merchant;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sun.deploy.net.URLEncoder;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/20 16:13
 * @DESC:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestQiniu {

    @Test
    public void upload(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "TLRptc6WL0DaAZxqBMW8nsDU1axF2mBTFf2XxdjR";
        String secretKey = "J3zQNj74AdpUkyGRWpU0QPJhIzvK4kwRlqCnb8yS";
        String bucket = "huiminpay-yts";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            String filePath = "f://bomb.jpg";
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            byte[] uploadBytes = IOUtils.toByteArray(fileInputStream);
            //byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            //认证
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("==================================="+putRet.key);
                System.out.println("==================================="+putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
    }
    @Test
    public void testDownload() throws UnsupportedEncodingException {
        String fileName = "Fg7w1J230rR64bm2BwvlHqy_GwTi";
        String domainOfBucket = "qy4c9d5xs.hn-bkt.clouddn.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);

        String accessKey = "TLRptc6WL0DaAZxqBMW8nsDU1axF2mBTFf2XxdjR";
        String secretKey = "J3zQNj74AdpUkyGRWpU0QPJhIzvK4kwRlqCnb8yS";
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(finalUrl);

    }
}
