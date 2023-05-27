package icu.stopit.client.Controller;

import icu.stopit.client.Constant.Constant;
import icu.stopit.client.Util.ContextUtils;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

@org.springframework.stereotype.Controller
@RequestMapping("/connect")
public class Controller {
    static Session[] session = new Session[1];
    static String[] name = new String[3];

    public static Session getSession() {
        if (session[0] == null) {
            connected();
        }
        return session[0];
    }

    public static void setSession(Session session) {
        Controller.session[0] = session;
    }

    public static String getName() {
        return name[0];
    }

    public static void setName(String name) {
        Controller.name[0] = name;
    }

    public static String getServerPort() {
        return name[2];
    }

    public static void setServerPort(String serverPort) {
        Controller.name[2] = serverPort;
    }

    public static String getHost() {
        return name[1];
    }

    public static void setHost(String host) {
        Controller.name[1] = host;
    }

    private static void connected() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Session s1 = null;
        try {
            Constant constant = ContextUtils.getBean(Constant.class);
            s1 = container.connectToServer(MyWebSocketClient.class, URI.create("ws://" + constant.getServerWebSocketHost() + ":" + constant.getServerWebSocketPort() + "/websocket/" + getName() + "/" + getServerPort()));
            setSession(s1);
        } catch (DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{name}/{host}/{serverPort}")
    @ResponseBody
    public boolean connected(@PathVariable("name") String name, @PathVariable("host") String host, @PathVariable("serverPort") String serverPort) throws DeploymentException, IOException {
        // 建立通道
        try {
            setName(name);
            setHost(host);
            setServerPort(serverPort);
            connected();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @GetMapping("/ping")
    @ResponseBody
    public boolean isReach() {
        boolean isReachable = false;
        try {
            Constant constant = ContextUtils.getBean(Constant.class);
            InetAddress inetAddress = InetAddress.getByName(constant.getServerWebSocketHost());
            isReachable = inetAddress.isReachable(5000); // 设置超时时间为5000毫秒
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return isReachable;
    }
}
