package icu.stopit.client.Controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import icu.stopit.client.Util.ObjectMessageDecoder;
import icu.stopit.client.Util.ProxyUtil;
import icu.stopit.client.Util.WebsocketUtil;
import icu.stopit.client.entity.InitRequest;
import icu.stopit.client.entity.InitResponse;
import jakarta.websocket.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@ClientEndpoint(decoders = {ObjectMessageDecoder.class}, encoders = {ObjectMessageDecoder.class})
@Component
public class MyWebSocketClient {
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("连接到服务器" + session.getId());
        session.setMaxTextMessageBufferSize(1024000);
        session.setMaxBinaryMessageBufferSize(1024000);
    }

    @OnMessage
    public void onMessage(JSONObject iRequest) throws IOException {
        InitRequest request = iRequest.to(InitRequest.class);
        InitResponse response = ProxyUtil.connect(request, Controller.getHost());
        // 回转
        if (Controller.getSession() == null) {
            System.out.println("会话已断开");
        }
        WebsocketUtil.send(Controller.getSession(), response);
        System.out.println("完成请求：" + JSON.toJSONString(response));
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Disconnected. Reason: " + reason.getReasonPhrase());
        Controller.setSession(null);
//        System.exit(1);
    }

    @OnError
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }
}
