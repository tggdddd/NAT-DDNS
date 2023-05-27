package icu.stopit.client.Util;

import jakarta.websocket.Session;

public class WebsocketUtil {
    public static void send(Session session, Object o) {
        if (o == null) {
            session.getAsyncRemote().sendText("");
            return;
        }
        session.getAsyncRemote().sendObject(o);
    }
}
