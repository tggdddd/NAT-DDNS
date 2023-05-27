package icu.stopit.Utils;

import jakarta.websocket.Session;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class SessionPool {
    private final static String SERVER_IDENTIFY = "server_identity_1qaxr1dasg1135135ssasa!$!$4";
    private static HashMap<String, Session> sessionHashMap = new HashMap<>();
    private static HashMap<Session, String> convertMap = new HashMap<>();
    private static HashMap<String, Process> processHashMap = new HashMap<>();

    public static Session get(String name) {
        return sessionHashMap.get(name);
    }

    public static void set(String name, Session session, Process process) {
        sessionHashMap.put(name, session);
        convertMap.put(session, name);
        processHashMap.put(name, process);
    }

    public static void del(String name) {
        Session remove = sessionHashMap.remove(name);
        Process process = processHashMap.get(name);
        if (process != null)
            process.destroy();
        processHashMap.remove(name);
        if (remove != null)
            convertMap.remove(remove);
    }

    public static void del(Session session) {
        String remove = convertMap.remove(session);
        if (remove != null) {
            sessionHashMap.remove(remove);
            Process process = processHashMap.get(remove);
            if (process != null) {
                process.destroy();
                processHashMap.remove(remove);
            }
        }
    }

    public static void setServer(Session session) {
        if (session == null) {
            del(SERVER_IDENTIFY);
            return;
        }
        set(SERVER_IDENTIFY, session, null);
    }

    public static void sendToServer(Object o) {
        Session session = get(SERVER_IDENTIFY);
        if (session != null) {
            WebsocketUtil.send(session, o);
        }
    }

    public static void sendToServer(String o) {
        Session session = get(SERVER_IDENTIFY);
        if (session != null) {
            session.getAsyncRemote().sendText(o);
        }
    }

    public static void sendToServer(ByteBuffer o) {
        Session session = get(SERVER_IDENTIFY);
        if (session != null) {
            session.getAsyncRemote().sendBinary(o);
        }
    }
}
