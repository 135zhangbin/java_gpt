package com.openai.chatgtp.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author mhmdz
 * Created By Zeeshan on 12-11-2023
 * @project chatgtp
 */

@Configuration
public class OpenAIChatGtpConfig {

    @Value("${openai.chatgtp.api.key}")
    private String openaiApiKey;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        // 设置代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        requestFactory.setProxy(proxy);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
