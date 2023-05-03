package com.timeless.controller;

import com.google.gson.Gson;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.LoginUser;
import com.timeless.domain.entity.User;
import com.timeless.domain.entity.UserVx;
import com.timeless.domain.vo.BlogUserLoginVo;
import com.timeless.domain.vo.UserInfoVo;
import com.timeless.service.UserService;
import com.timeless.service.UserVxService;
import com.timeless.utils.BeanCopyUtils;
import com.timeless.utils.HttpClientUtils;
import com.timeless.utils.JwtUtil;
import com.timeless.utils.RedisCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author timeless
 * @create 2023-04-22 17:44
 */

@Controller
@RequestMapping("/api/ucenter/wx")
public class VXLoginController {

    @Value("${vx.open.app_id}")
    private String appId;

    @Value("${vx.open.app_secret}")
    private String appSecret;

    @Autowired
    private UserVxService userVxService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisCache redisCache;


//    @GetMapping("/generateQRCode")
//    public String generateQRCode() {
//        // 微信开放平台授权baseUrl
//        //? %s相当于占位符，可以填充参数
//        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
//                "?appid=%s" +
//                "&redirect_uri=%s" +
//                "&response_type=code" +
//                "&scope=snsapi_login" +
//                "&state=%s" +
//                "#wechat_redirect";
//        //授权码需要传入加密过的URL,必须使用
//        try {
//            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");//url编码
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//        //设置%s参数值
//        String url = String.format(baseUrl, appId, redirectUrl, "timeless");
//
//        //请求微信地址,重定向的方式
//        return "redirect:" + url;
//    }

    //2.获取扫描人信息，添加数据
    @GetMapping("callback")
    @Transactional
    public String callback(String code, String state, HttpSession session) {
        try {
            //拿着code请求 微信的固定地址，得到两个值access_token和openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            String accessTokenUrl = String.format(baseAccessTokenUrl, appId, appSecret, code);
            //请求这个拼接好的地址，得到返回两个值access_token和openid
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //使用gson转换工具Gson
            Gson gson = new Gson();

            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");


            //根据openid，看看哟没有user
            User user = userVxService.getUserByOpenId(openid);
            //没有user，也就是用户是注册
            // TODO 现在暂时认为 sys_user_vx中有记录的话，sys_user中一定有对应的user
            // TODO 因为现在，删除用户不会删除sys_user_vx中的记录，没来得及，之后有时间再做！
            if (user == null) {
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //再次拼接微信地址
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String userInfo = HttpClientUtils.get(userInfoUrl);

                //获取的微信个人信息json信息进行转换
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");//昵称
                String headimgurl = (String) userInfoMap.get("headimgurl");//头像

                //把用户信息注册到用户表中
                User userNew = new User();
                userNew.setUserName(UUID.randomUUID().toString());
                userNew.setNickName(nickname);
                userNew.setAvatar(headimgurl);
                userNew.setSex("2");
                userService.save(userNew);
//                System.out.println(userNew.getId());
                //注册到关联表
                userVxService.save(new UserVx(userNew.getId(), openid));

                //生成jwt存储到redis
                String jwt = JwtUtil.createJWT(userNew.getId().toString());
                redisCache.setCacheObject("blogLogin:" + userNew.getId().toString(), new LoginUser(userNew, null));

//                return ResponseResult.okResult(new BlogUserLoginVo(jwt, BeanCopyUtils.copyBean(userNew, UserInfoVo.class)));
                return "redirect:http://localhost:8093/#/callback?token=" + jwt + "&uid=" + userNew.getId();
            }

            //生成jwt存储到redis
            String jwt = JwtUtil.createJWT(user.getId().toString());
            redisCache.setCacheObject("blogLogin:" + user.getId().toString(), new LoginUser(user, null));

//            return ResponseResult.okResult(new BlogUserLoginVo(jwt, BeanCopyUtils.copyBean(user, UserInfoVo.class)));
            //贼离谱，必须加上#/，不然前端会自己在后面加上#/，导致获取不到token
            return "redirect:http://localhost:8093/#/callback?token=" + jwt + "&uid=" + user.getId();
        } catch (Exception e) {
            throw new RuntimeException("登陆失败");
        }
    }


}
