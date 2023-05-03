package com.timeless.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.timeless.domain.entity.User;
import com.timeless.enums.AppHttpCodeEnum;
import com.timeless.exception.SystemException;
import com.timeless.service.UserService;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author timeless
 * @create 2023-04-22 2:20
 */
@Component
public class QQEmailUtils {

    @Value("${spring.mail.username}")
    private String mailUserName;

    @Value("${spring.mail.host}")
    private String mailHostName;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    public void sendCode(String email) {
        if (!StringUtils.hasText(email)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }

        //判断邮箱是否存在
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        User user = userService.getOne(userLambdaQueryWrapper);

        if (ObjectUtils.isEmpty(user)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_IS_NOT_EXIST);
        }
        //验证码
        String code = createSixCode();
        //发邮件
        try {
            SimpleEmail mail = new SimpleEmail();
            mail.setHostName(mailHostName);//发送邮件的服务器
            mail.setAuthentication(mailUserName,mailPassword);//刚刚记录的授权码，是开启SMTP的密码
            mail.setFrom(mailUserName,"梨花不等故人来");  //发送邮件的邮箱和发件人
            mail.setSSLOnConnect(true); //使用安全链接
            mail.addTo(email);//接收的邮箱
            mail.setSubject("timeless博客登录验证码");//设置邮件的主题
            mail.setMsg("尊敬的用户:你好!\n 登录验证码为:" + code+"\n"+"     (有效期为三分钟)");//设置邮件的内容
            mail.send();//发送
        } catch (Exception e){
            throw new SystemException(AppHttpCodeEnum.CODE_SEND_ERROR);
        }
        //code存入redis
        redisCache.setCacheObject("blogLogin:" + email, code, 3, TimeUnit.MINUTES);
    }

    public static String createSixCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

}
