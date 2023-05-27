package icu.stopit.Utils;

import com.alibaba.fastjson2.JSONObject;
import icu.stopit.entity.InitResponse;
import jakarta.websocket.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@ClientEndpoint(decoders = {ObjectMessageDecoder.class}, encoders = {ObjectMessageDecoder.class})
@Component
public class WebSocketClient {
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("连接到Socket服务器" + session.getId());
        session.setMaxTextMessageBufferSize(1024000);
        session.setMaxBinaryMessageBufferSize(1024000);
    }

    @OnMessage
    public void onMessage(JSONObject iResponse) throws IOException {
        InitResponse response = iResponse.to(InitResponse.class);
        HandleSocketUtils.putResponse(response.getName(), response.getUuid(), response);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Disconnected. Reason: " + reason.getReasonPhrase());
    }
}
