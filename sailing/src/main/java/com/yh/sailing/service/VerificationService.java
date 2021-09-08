package com.yh.sailing.service;

import com.yh.sailing.dto.VerificationInfo;
import com.yh.sailing.handler.AbstractVerificationHandler;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.lang.model.element.VariableElement;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class VerificationService {

    @Autowired
	@Qualifier("verificationHandlerMap")
    private Map<String, AbstractVerificationHandler> verificationHandlerMap;


    /**
     * 生成验证信息
     * @param name 业务名称
     * @param payload 业务携带参数，如手机号 ，邮箱
     * @param effectiveTime 验证信息有效期(秒)
     * @return
     */
    public VerificationInfo generateVerificationInfo(String name, Map<String,Object> payload,  int effectiveTime){
        AbstractVerificationHandler verificationHandler =  getVerificationHandler(name);
        return verificationHandler.generateVerificationInfo(payload, effectiveTime);
    }


    /**
     * 验证信息
     * @param name 业务名称
     * @param verificationKey  验证key
     * @param verificationCode 验证码
     * @return
     */
    public boolean verify(String name, String verificationKey, String verificationCode){
        AbstractVerificationHandler verificationHandler =  getVerificationHandler(name);
        return verificationHandler.verify(verificationKey, verificationCode);
    }




    private AbstractVerificationHandler getVerificationHandler(String name) {
        AbstractVerificationHandler verificationHandler = verificationHandlerMap.get(name);
        if(verificationHandler == null){
            throw new RuntimeException(String.format("No found handler process %s type.", name));
        }
        return verificationHandler;
    }




    private static final String SALT="mldnjava"; //公共的盐值

    private static final int REPEAT=5; //加密次数

    public static String encode(String str) { //加密处理
        String uuid = UUID.randomUUID().toString();
        String temp = str+"-"+uuid;
        //String temp=str+"{"+SALT+"}";//盐值对外不公布 guanshan{mldnjava}
        byte data[]=temp.getBytes();//将字符串变为字节数组
        for(int x=0;x<REPEAT;x++){
            data=Base64.getEncoder().encode(data); //重复加密
        }
        //VmpKd1QxWXlSa2hUYmxaVllsUnNZVlp1Y0ZaTk1XeHpXa1JTYUUxcmNEQlpNR1J2WVRKS1ZsZFVWVDA9
        return new String(data); //返回加密后的内容
    }

    public static String decode(String str) {//VmpKd1QxWXlSa2hUYmxaVllsUnNZVlp1Y0ZaTk1XeHpXa1JTYUUxcmNEQlpNR1J2WVRKS1ZsZFVWVDA9
        byte data[]=str.getBytes();//获取加密内容
        for(int x=0;x<REPEAT;x++) {
            data = Base64.getDecoder().decode(data); //多次解密
        }
//        data = guanshan{mldnjava}
        //String s = new String(data).replaceAll("\\{\\w+\\}", "");
        String saltSource = new String(data);
        //去盐
        String source = saltSource.substring(0, saltSource.indexOf("-"));
        return source; //删除盐值格式 guanshan
    }

    public static void main(String[] args) throws Exception{
        String str= encode("18530036743");
        System.out.println(str);
        System.out.println(decode(str));

    }
   /* public static void main(String[] args) throws Exception{
        String msg="guanshan"; //原始内容

        String encMsg=new String(Base64.getEncoder().encode(msg.getBytes()));//数据加密
        //Z3VhbnNoYW4=
        System.out.println("encMsg==========================="+encMsg); //输出密文

        String oldMsg=new String(Base64.getDecoder().decode(encMsg)); //数据解密

        System.out.println("oldMsg==========================="+oldMsg); //输出明文

    }*/

}
