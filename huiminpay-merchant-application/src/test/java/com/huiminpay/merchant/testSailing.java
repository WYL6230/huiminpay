package com.huiminpay.merchant;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/18 15:36
 * @DESC:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class testSailing {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testSailing(){
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile","15690643216");
        ResponseEntity<Map> mapResponseEntity = restTemplate.postForEntity("http://localhost:56085/sailing/generate?effectiveTime=100&name=sms",
                map, Map.class);
        Map body = mapResponseEntity.getBody();
        System.out.println("=============================="+body);
    }

    /**
     * 获取验证码内容
     */
    @Test
    public void testGetSmsCode() {
        String url = "http://127.0.0.1:56085/sailing/generate?effectiveTime=60&name=sms";
        String phone = "13081936214";
        //请求体
        Map<String, Object > body = new HashMap();
        body.put("mobile", phone);
        //请求头
        HttpHeaders httpHeaders = new HttpHeaders();
       // httpHeaders.put("content-type","application/json");
        //设置数据格式为json
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //封装请求参数
        HttpEntity entity = new HttpEntity(body, httpHeaders);
        //HttpEntity httpEntity = new HttpEntity(body,httpHeaders);
        ResponseEntity<Map> forEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        Map responseMap = forEntity.getBody();
        log.info("获取验证码：【{}】", responseMap);
        //取出body中的result数据
        if (responseMap != null || responseMap.get("result") != null) {
            Map resultMap = (Map) responseMap.get("result");
            String value = resultMap.get("key").toString();
            System.out.println(value);
        }
    }
}
