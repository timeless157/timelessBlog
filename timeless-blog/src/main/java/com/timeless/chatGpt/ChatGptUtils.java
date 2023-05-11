package com.timeless.chatGpt;

import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpHost proxy = new HttpHost("127.0.0.1", 33210);
        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();
        HttpPost httpPost = new HttpPost("https://api.openai.com/v1/chat/completions");
        httpPost.setConfig(config);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer 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"); // Replace with your actual API key
        Gson gson = new Gson();
        String json = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": "
                + "[{\"role\": \"user\", \"content\": "
                + "\"" + text.replaceAll("\"", "\\\\\"")
                .replaceAll("'", "\\\\'") + "\"}], \"temperature\": 0.7 }";
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String responseData = EntityUtils.toString(response.getEntity());
        MyObject myObject = gson.fromJson(responseData, MyObject.class);
        return myObject.getChoices()[0].getMessage().getContent();
    }


//    public static String getBlank(String text) throws IOException {
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 33210));
//        //发送http请求
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .proxy(proxy)
//                .connectTimeout(10000,TimeUnit.SECONDS)
//                .readTimeout(10000,TimeUnit.SECONDS)
//                .build();
//        MediaType mediaType = MediaType.parse("application/json");
//        Gson gson = new Gson();
//        String json = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": " +
//                "[{\"role\": \"user\", \"content\": " + "\"" +
//                text.replaceAll("\"", " ")
//                        .replaceAll("\\\\", " ") + "\"" + "}], \"temperature\": 0.7 }";
//        Object obj = gson.fromJson(json, Object.class);
//        String jsonString = gson.toJson(obj);
//        RequestBody body = RequestBody.create(mediaType, jsonString);
//        Request request = new Request.Builder()
//                .url("https://api.openai.com/v1/chat/completions")
//                .post(body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "Bearer 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") // Replace with your actual API key
//                .build();
//        Response response = client.newCall(request).execute();
//        String responseData = response.body().string();
//        MyObject myObject = new Gson().fromJson(responseData, MyObject.class);
//        return myObject.getChoices()[0].getMessage().getContent();
//    }

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
