package com.ul.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/")
public class TestC {

    @RequestMapping("/")
    public String test(HttpServletResponse response, HttpServletRequest request){
        OutputStream bos = null;
        try {
            String data1 =" www.imiansha.com爱面纱网站上线了\n\n";
            String data2 = " www.imiansha.com爱面纱网站正在招兵买马\n\n";
            //声明浏览器在连接断开之后进行再次连接之前的等待时间 10秒
            String retry = "retry:"+10000+"\n\n";
            //事件的标识符
            String id="id:100\n\n";
            //最后一次接收到的事件的标识符
            String last = request.getHeader("Last-Event-ID");
            bos = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("text/event-stream");//记得要设置哦
            bos.write(data1.getBytes());
            bos.write(data2.getBytes());
            bos.write(retry.getBytes());
            bos.write(id.getBytes());
            bos.flush();
            while (true){
                Thread.sleep(3000);
                bos.write(new Date().toString().getBytes());
                bos.write("\n".getBytes());
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping("t")
    public String test1(HttpServletRequest request){
        request.getParameterMap().forEach((k,v)->{
            System.out.print(k+":");
            Arrays.stream(v).forEach(System.out::print);
            System.out.println();
        });

        return "success";
    }

    @RequestMapping(value="/push",produces="text/event-stream")
    void doEvent(HttpServletRequest req, HttpServletResponse resp)
    {
        resp.setContentType("text/event-stream");
        resp.setHeader("expires", "-1");
        resp.setHeader("cache-control", "no-cache");
        resp.setHeader("Access-Control-Allow-Origin","*");

        int i = 0;
        int r = 100; //半径
        int v = 50;  //走一圈的步数

        while(true){
            i = (i+1)%(v*2);
            if(i==0){
                r += 20;
            }
            double angel = i*Math.PI/v;	//i 从   0到2π
            int x = (int)Math.round( r* Math.cos(angel) );
            int y = (int)Math.round( r* Math.sin(angel) );
            String s = "data:"+x+","+y+"\n\n";

            try {
                resp.getOutputStream().write(s.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            try {
                resp.flushBuffer();//前端页面关闭或刷新
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
