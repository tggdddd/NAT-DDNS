package icu.stopit.Utils;

import icu.stopit.entity.InitRequest;
import icu.stopit.entity.InitResponse;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HandleSocketUtils {
    private static HashMap<String, ConcurrentLinkedQueue<InitResponse>> pools = new HashMap<>();
    private static Session[] sessions = new Session[1];

    public static Session getSession() {
        if (sessions[0] == null) {
            connected();
        }
        return sessions[0];
    }

    public static void connected() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            Session s1 = container.connectToServer(WebSocketClient.class, URI.create("ws://localhost:3090/websocket/communication"));
            HandleSocketUtils.sessions[0] = s1;
        } catch (DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void toProxy(InitRequest initRequest) {
        if (getSession() != null) {
            Session session = getSession();
            WebsocketUtil.send(session, initRequest);
        }
    }

    public static void putResponse(String name, String uuid, InitResponse response) {
        ConcurrentLinkedQueue<InitResponse> queue = pools.getOrDefault(name + uuid, new ConcurrentLinkedQueue<>());
        queue.offer(response);
        pools.putIfAbsent(name + uuid, queue);
    }

    private static ConcurrentLinkedQueue<InitResponse> getQueue(String name, String uuid) {
        ConcurrentLinkedQueue<InitResponse> queue = pools.getOrDefault(name + uuid, new ConcurrentLinkedQueue<>());
        pools.putIfAbsent(name + uuid, queue);
        return queue;
    }

    public static InitResponse getResponse(String name, String uuid) {
        // TODO: 2023/5/21 未注册的映射  处理
        ConcurrentLinkedQueue<InitResponse> queue = getQueue(name, uuid);
        return queue.poll();
    }

}
