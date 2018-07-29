package com.ul;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Test {

    private static final LinkedBlockingQueue<String> q = new LinkedBlockingQueue();

    @org.junit.Test
    public void test0(){
        //System.out.println(DateFormat.getDateTimeInstance(0,0).format(new Date()));
        //System.out.println(DateFormat.getDateTimeInstance(1,1).format(new Date()));
        //System.out.println(DateFormat.getDateTimeInstance(2,2,Locale.UK).format(new Date()));
        //System.out.println(DateFormat.getDateTimeInstance(3,3).format(new Date()));
        Arrays.stream(Locale.getAvailableLocales())
                .forEach(l->{
                    System.out.println(DateFormat.getDateTimeInstance(2,2,l).format(new Date()));
                });
    }

    @org.junit.Test
    public void test() throws InterruptedException {
        new Thread(()->{
            while (true){
                String str = null;
                try {
                    str = q.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (str.startsWith("Sat")){
                        System.out.println("Date:"+str);

                }
            }
        }).start();
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setInterceptors(Collections.singletonList(new ActionTrackInterceptor()));
        String url = "http://127.0.0.1:8080/";
        restTemplate.getForObject(url, String.class);
    }

    static class ActionTrackInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {

            // 保证请求继续被执行
            //ByteArrayOutputStream buf = new ByteArrayOutputStream();
            ClientHttpResponse resp = execution.execute(request, body);
           // BufferedInputStream bis = new BufferedInputStream(resp.getBody());
//            int result = bis.read();
//            while(result != -1) {
//                buf.write((byte) result);
//                result = bis.read();
//                System.out.println(buf.toString());
//            }
            new BufferedReader(new InputStreamReader(resp.getBody()))
                    .lines().forEach(q::add);

            return resp;
        }
    }
}
