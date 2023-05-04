package com.timeless.chatGpt;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * @author timeless
 * @create 2023-04-20 3:29
 */
public class ChatGptUtils {
//    public static void main(String[] args) throws IOException {
//        System.out.println(getBlank("你是?"));
//    }

    public static String getBlank(String text) throws IOException {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 33210));
        //发送http请求
        OkHttpClient client = new OkHttpClient();
        client.setProxy(proxy);
        client.setConnectTimeout(10000, TimeUnit.SECONDS);
        client.setReadTimeout(10000, TimeUnit.SECONDS);
        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        String json = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": " +
                "[{\"role\": \"user\", \"content\": " + "\"" +
                text.replaceAll("\"", " ")
                        .replaceAll("\\\\", " ") + "\"" + "}], \"temperature\": 0.7 }";
        Object obj = gson.fromJson(json, Object.class);
        String jsonString = gson.toJson(obj);
        RequestBody body = RequestBody.create(mediaType, jsonString);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer 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") // Replace with your actual API key
                .build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();
        MyObject myObject = new Gson().fromJson(responseData, MyObject.class);
        return myObject.getChoices()[0].getMessage().getContent();
    }

}

class MyObject {
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private Choice[] choices;

    public Choice[] getChoices() {
        return choices;
    }
    // getter and setter methods
}

class Usage {
    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;

    // getter and setter methods
}

class Choice {
    private Message message;
    private String finish_reason;
    private int index;

    public Message getMessage() {
        return message;
    }
// getter and setter methods
}

class Message {
    private String role;
    private String content;

    public String getContent() {
        return content;
    }
// getter and setter methods
}
