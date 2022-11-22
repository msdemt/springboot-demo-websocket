package com.example.demo.ws.util;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtil {

    /**
     * 模拟存储 在线用户
     */
    public static final Map<String, Session> USERS_ONLINE = new ConcurrentHashMap<>();

    /**
     * 向所有在线用户发送消息
     * @param message
     */
    public static void sendMessageToAllOnlineUser(String message){
        USERS_ONLINE.forEach((user, session) -> sendMessage(session, message));
    }

    /**
     * 向指定用户发送消息
     * @param session
     * @param message
     */
    private static void sendMessage(Session session, String message){
        if(session == null) return;
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if(basic == null) return;

        try{
            basic.sendText(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
