package com.timeless.controller;

import com.timeless.chatGpt.ChatGptUtils;
import com.timeless.domain.ResponseResult;
import com.timeless.domain.dto.ChatGptDto;
import com.timeless.enums.AppHttpCodeEnum;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author timeless
 * @create 2023-04-20 3:32
 */
@RestController
@RequestMapping("/chatGpt")
public class ChatGptController {

    @PostMapping("/getAnswer")
    public ResponseResult getAnswer(@RequestBody ChatGptDto question) {
        try {
//            System.out.println(question.getQuestion());
            String answer = ChatGptUtils.getBlank(question.getQuestion());
            return ResponseResult.okResult(answer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/hitokoto")
    public ResponseResult getHitokoto() {
        try {
            URL url = new URL("https://api.uixsj.cn/hitokoto/get?type=social");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return ResponseResult.okResult(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

}
