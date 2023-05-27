package icu.stopit.Controller;

import com.alibaba.fastjson2.JSONObject;
import icu.stopit.Utils.SessionPool;
import icu.stopit.WebSocketApplication;
import icu.stopit.configuration.ObjectMessageDecoder;
import icu.stopit.entity.InitResponse;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*与客户端的交互*/
@Component
@ServerEndpoint(value = "/websocket/{identify}/{port}", decoders = {ObjectMessageDecoder.class}, encoders = {ObjectMessageDecoder.class})
public class WebSocket {
    private static Process startServer(String name, int port) {

        // TODO: 2023/5/22 动态配置端口
        String jarFilePath = null;
        jarFilePath = WebSocketApplication.class.getResource("server.jar").getPath();
        if (jarFilePath.startsWith("/")) {
            jarFilePath = jarFilePath.substring(1);
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarFilePath, "--server.port=" + port, "--name=" + name);
            Process process = processBuilder.start();
            System.out.println(process.pid() + "运行");
            return process;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @OnMessage
    public void handleMessage(JSONObject iResponse) {
        InitResponse request = iResponse.to(InitResponse.class);
        //将结果回转给控制器
        SessionPool.sendToServer(iResponse);
    }

    @OnOpen
    public void afterConnectionEstablished(Session session, @PathParam("identify") String name, @PathParam("port") String port) throws Exception {
        session.setMaxTextMessageBufferSize(1024000);
        session.setMaxBinaryMessageBufferSize(1024000);
        // 启动jar包
        Process process = startServer(name, Integer.parseInt(port));
        // 保存
        SessionPool.set(name, session, process);
    }

    @OnError
    public void handleTransportError(Session session, Throwable exception) {
        SessionPool.del(session);
    }

    @OnClose
    public void afterConnectionClosed(Session session) throws Exception {
        SessionPool.del(session);
    }
}
