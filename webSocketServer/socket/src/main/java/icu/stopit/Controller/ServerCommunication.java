package icu.stopit.Controller;


import com.alibaba.fastjson2.JSONObject;
import icu.stopit.Utils.SessionPool;
import icu.stopit.Utils.WebsocketUtil;
import icu.stopit.configuration.ObjectMessageDecoder;
import icu.stopit.entity.InitRequest;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;


@Component
@ServerEndpoint(value = "/websocket/communication", decoders = {ObjectMessageDecoder.class}, encoders = {ObjectMessageDecoder.class})
public class ServerCommunication {
    @OnOpen
    public void afterConnectionEstablished(Session session) throws Exception {
        session.setMaxTextMessageBufferSize(1024000);
        session.setMaxBinaryMessageBufferSize(1024000);
        SessionPool.setServer(session);
        //    连接无需配置
    }

    @OnMessage
    public void handleMessage(JSONObject iRequest) {
        InitRequest request = iRequest.to(InitRequest.class);
//        if(!(iRequest instanceof InitRequest request)){
//            System.err.println("错误类型："+iRequest);
//            return;
//        }
        Session session = SessionPool.get(request.getName());
        if (session == null) {
            System.err.println("结果返回无法找到对应的session:" + request.getName());
            SessionPool.sendToServer((Object) null);
            return;
        }
        WebsocketUtil.send(session, request);
    }

    @OnError
    public void handleTransportError(Session session, Throwable exception) {
        SessionPool.setServer(null);
    }

    @OnClose
    public void afterConnectionClosed(Session session) throws Exception {
        SessionPool.setServer(null);
    }
}
