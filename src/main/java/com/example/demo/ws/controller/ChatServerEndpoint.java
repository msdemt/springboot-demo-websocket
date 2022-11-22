package com.example.demo.ws.controller;

import com.example.demo.ws.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Slf4j
@Controller
@ServerEndpoint("/chat/{username}") //创建websocket的endpoint
public class ChatServerEndpoint {

    /**
     * 访问聊天室页面
     * @return
     */
    @GetMapping("/chatPage")
    public String chatPage(){
        return "chat.html";
    }

    /**
     * 连接创建（用户进入聊天室）时触发
     * @param username
     * @param session
     */
    @OnOpen
    public void openSession(@PathParam("username") String username, Session session){
        //存储用户
        WebSocketUtil.USERS_ONLINE.put(username, session);
        //向所有在线用户发送用户上线通知
        String msg = "[" + username + "]进入聊天室";
        log.info(msg);
        WebSocketUtil.sendMessageToAllOnlineUser(msg);
    }

    /**
     * 连接断开（用户离开聊天室）时触发
     * @param username
     * @param session
     */
    @OnClose
    public void closeSession(@PathParam("username")String username, Session session){
        //删除用户
        WebSocketUtil.USERS_ONLINE.remove(username);
        //向所有在线用户发送用户下线通知
        String msg = "[" + username + "]离开聊天室";
        log.info(msg);
        WebSocketUtil.sendMessageToAllOnlineUser(msg);
        //下线后关闭session
        try {
            session.close();
        } catch (IOException e) {
            log.error("error:{}", e);
        }
    }

    /**
     * 在接收到消息时触发
     * @param username
     * @param msg
     */
    @OnMessage
    public void onMessage(@PathParam("username") String username, String msg){
        //向聊天室中的人发送消息
        msg = "[" + username + "]: " + msg;
        log.info(msg);
        WebSocketUtil.sendMessageToAllOnlineUser(msg);
    }

    /**
     * 在连接发生异常时触发
     * @param session
     * @param throwable
     */
    @OnError
    public void sessionError(Session session, Throwable throwable){
        try {
            session.close();
        } catch (IOException e) {
            log.error("error:{}", e);
        }
        log.error("WebSocket连接发生异常，message:{}", throwable.getMessage());
    }




}
