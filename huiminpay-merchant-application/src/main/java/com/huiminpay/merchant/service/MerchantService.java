package com.huiminpay.merchant.service;

import com.huiminpay.common.cache.domain.CommonErrorCode;
import com.huiminpay.common.cache.exception.BusinessCast;
import com.huiminpay.common.cache.util.PhoneUtil;
import com.huiminpay.common.cache.util.QiniuUtil;
import com.huiminpay.merchant.api.MerchantServiceApi;
import com.huiminpay.merchant.dto.MerchantDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/19 11:11
 * @DESC:
 */
@Service
@Slf4j
public class MerchantService {
    @Autowired
    private RestTemplate restTemplate;

    @Reference
    private MerchantServiceApi merchantServiceApi;

    //http://localhost:56085/sailing/
    @Value("${sailing.url}")
    private  String url;

    @Value("${oss.qiniuyun.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.qiniuyun.secretKeySecret}")
    private String secretKeySecret;
    @Value("${oss.qiniuyun.bucket}")
    private String bucket;
    @Value("${oss.qiniuyun.domain}")
    private String domain;

    public MerchantRegisterVO register(MerchantRegisterVO merchantRegisterVO){
        if(merchantRegisterVO == null){
            BusinessCast.cast(CommonErrorCode.E_100101);
        }
        if(StringUtils.isEmpty(merchantRegisterVO.getMobile())){
            BusinessCast.cast(CommonErrorCode.E_100112);
        }
        if (!PhoneUtil.isMatches(merchantRegisterVO.getMobile())){
            BusinessCast.cast(CommonErrorCode.E_100109);
        }
        if (StringUtils.isEmpty(merchantRegisterVO.getUsername())){
            BusinessCast.cast(CommonErrorCode.E_100110);
        }
        if(StringUtils.isEmpty(merchantRegisterVO.getPassword())){
            BusinessCast.cast(CommonErrorCode.E_100111);
        }
        //.............
        //远程调用sailing服务校验验证码
        checkMsmCode(merchantRegisterVO.getVerifiykey(),merchantRegisterVO.getVerifiyCode());
        //远程调用商户服务添加商户信息
       /* MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setMobile(merchantRegisterVO.getMobile());
        merchantDTO.setUsername(merchantRegisterVO.getUsername());*/
        MerchantDTO merchantDTO = MerchantRegisterConvert.INSTANCE.vo2dto(merchantRegisterVO);
        merchantDTO.setPassword(merchantRegisterVO.getPassword());
        //........
        merchantServiceApi.registerMerchant(merchantDTO);

        //把dto转换为vo
        MerchantRegisterVO registerVO = MerchantRegisterConvert.INSTANCE.dto2vo(merchantDTO);
        return registerVO;
    }
    ////远程调用sailing服务校验验证码
    private void checkMsmCode(String key,String code){
        // http://localhost:56085/sailing/verify?name=sms&verificationCode=qq&verificationKey=22
        String msmUrl = url+"verify?name=sms&verificationCode="+code+"&verificationKey="+key;
        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(msmUrl,
                    HttpMethod.POST, HttpEntity.EMPTY, Map.class);
            if (responseEntity == null){
                log.error("校验验证码出错");
                throw new RuntimeException("校验验证码出错");

            }
            Map entityBody = responseEntity.getBody();
            if(entityBody == null || entityBody.get("result")==null){
                log.error("校验验证码出错");
                throw new RuntimeException("校验验证码出错");
            }
            boolean result = (boolean)entityBody.get("result");
            if(!result){
                log.error("校验验证码出错");
                throw new RuntimeException("校验验证码出错");
            }
        }catch (Exception e){
            log.error("校验验证码出错:{}",e.getMessage());
            throw new RuntimeException("校验验证码出错");
        }


    }

    public String upload(MultipartFile file) {
        //校验参数合法性
        if(file == null){
            BusinessCast.cast(CommonErrorCode.E_100101);
        }
        String key = UUID.randomUUID().toString();
        //文件原始名称 liying.jpg
        String originalFilename = file.getOriginalFilename();
        // qwreu123414556.jpg
        key = key+originalFilename.substring(originalFilename.lastIndexOf("."));
        System.out.println("================================"+key);
        try {
            byte[] bytes = file.getBytes();
            //把文件信息上传到七牛云
            QiniuUtil.upload(accessKeyId,secretKeySecret,bucket,key,bytes);
            return domain+key;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传证件异常:{}",e.getMessage());
            BusinessCast.cast(CommonErrorCode.UNKNOWN);
        }

        return null;
    }

    public MerchantDTO queryMerchantById(Long merchantId) {
        MerchantDTO merchantDTO = merchantServiceApi.findMerchantById(merchantId);
        return merchantDTO;
    }
}
