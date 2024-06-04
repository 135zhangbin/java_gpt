package com.openai.chatgtp;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.plexpt.chatgpt.util.Proxys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

//https://github.com/PlexPt/chatgpt-java
@SpringBootTest
class ChatgtpApplicationTests {

    @Test
    void contextLoads() {
        completions();
        chat();
    }

    /**
     * v1/completions 下的模型调用方法
     */
    public static void completions(){
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Content-Type","application/json");

        JSONObject json = new JSONObject();
        //选择模型
        json.set("model","text-davinci-003");
        //添加我们需要输入的内容
        json.set("prompt","推荐一部电影");
        json.set("temperature",0);
        json.set("max_tokens",2048);
        json.set("top_p",1);
        json.set("frequency_penalty",0.0);
        json.set("presence_penalty",0.0);
        try{
            Proxy proxy = Proxys.http("127.0.0.1", 7890); // 端口号不知道怎么来的私聊，说出来审核不通过
            HttpResponse response =  HttpRequest.post("https://api.openai.com/v1/completions") // text-davinci-003
                    .headerMap(headers, false)
                    .bearerAuth("sess-PKriXawyVSw1PmlTGD77B6H8OPmI3cuicqunata1") // 填写自己的 chatgpt API Keys
                    .setProxy(proxy)
                    .body(String.valueOf(json))
                    .timeout(600000)
                    .execute();
            System.out.println(response.body());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * v1/chat/completions 下的模型调用方法
     */
    public static void chat(){
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Content-Type","application/json");

        JSONObject json = new JSONObject();
        //选择模型
        json.set("model","gpt-3.5-turbo");
        //添加我们需要输入的内容
        JSONObject msg = new JSONObject();
        msg.set("role", "user");
        msg.set("content", "推荐1本余华的书");
        JSONArray array = new JSONArray();
        array.add(msg);
        json.set("messages", array);
        json.set("temperature",0);
        json.set("max_tokens",2048);
        json.set("top_p",1);
        json.set("frequency_penalty",0.0);
        json.set("presence_penalty",0.0);
        try{
            Proxy proxy = Proxys.http("127.0.0.1", 7890);  // 端口号不知道怎么来的私聊，说出来审核不通过
            HttpResponse response = HttpRequest.post("https://api.openai.com/v1/chat/completions") //gpt-3.5-turbo
                    .headerMap(headers, false)
                    .bearerAuth("sess-PKriXawyVSw1PmlTGD77B6H8OPmI3cuicqunata1") // 填写自己的 chatgpt API Keys
                    .setProxy(proxy)
                    .body(String.valueOf(json))
                    .timeout(600000)
                    .execute();
            System.out.println(response.body());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
