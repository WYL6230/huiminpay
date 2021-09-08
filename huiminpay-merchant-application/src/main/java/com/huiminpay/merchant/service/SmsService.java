package com.huiminpay.merchant.service;

import com.huiminpay.common.cache.domain.CommonErrorCode;
import com.huiminpay.common.cache.exception.BusinessCast;
import com.huiminpay.common.cache.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/19 9:40
 * @DESC:
 */
@Service
@Slf4j
public class SmsService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${sailing.effectiveTime}")
    private  Integer effectiveTime;
    @Value("${sailing.url}")
    private  String url;

    //  http://localhost:56085/sailing/generate?effectiveTime=100&name=sms
    public String sendMsmCode(String moblie) throws BusinessException
    {

        //创建请求体
        Map<String, String> body = new HashMap<>();
        body.put("mobile",moblie);
        //创建请求头对象
        HttpHeaders httpHeaders = new HttpHeaders();
        //Content-Type: application/json
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity httpEntity = new HttpEntity(body,httpHeaders);
        // http://localhost:56085/sailing/
        // generate?name=sms
        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url+"generate?name=sms&effectiveTime=" + effectiveTime,
                    httpEntity, Map.class);

            Map entityBody = responseEntity.getBody();
            if(entityBody == null){
                BusinessCast.cast(CommonErrorCode.E_100103);
                //throw new BusinessException(CommonErrorCode.E_100103);
                //throw new RuntimeException("远程调用验证码服务获取验证码异常");
            }
            if(entityBody.get("result") == null ){
                BusinessCast.cast(CommonErrorCode.E_100103);
                //throw new RuntimeException("远程调用验证码服务获取验证码异常");
            }
            Map result = (Map) entityBody.get("result");
            String key = result.get("key").toString();
            return key;
        }catch (Exception e){
            log.error("远程调用验证码服务获取验证码异常:{}",e.getMessage());
            BusinessCast.cast(CommonErrorCode.E_100103);
            return null;
        }

    }
}
