package icu.stopit.Controller;


import com.alibaba.fastjson2.JSON;
import icu.stopit.Utils.HandleSocketUtils;
import icu.stopit.Utils.WebsocketUtil;
import icu.stopit.entity.InitRequest;
import icu.stopit.entity.InitResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;

@Controller
public class RedirectController {
    static String[] name = new String[1];

    public static String getName() {
        return name[0];
    }

    public static void setName(String name) {
        RedirectController.name[0] = name;
    }

    //    @RequestMapping(value = "/{name:^(?!websocke/t$).*$}/**")
    @RequestMapping("/**")
    public void redirect(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException {
        InitRequest initRequest = new InitRequest(getName(), request);
        String jsonString = JSON.toJSONString(initRequest);
        // 发送消息
        WebsocketUtil.send(HandleSocketUtils.getSession(), initRequest);
        // 等待回复
        int time = 0;
        // 最长等待5秒
//        time < 100
        while (time < 100) {
            InitResponse source = HandleSocketUtils.getResponse(initRequest.getName(), initRequest.getUuid());
            // 收到回复
            if (source != null) {
                response.setStatus(source.getStatus());
                Map<String, String> headers = source.getHeaders();
                headers.forEach(response::setHeader);
                try {
                    response.getOutputStream().write(source.getBytes());
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // 循环等待
            time++;
            Thread.sleep(50);
        }
        response.setStatus(500);
        response.getWriter().write("服务器回复超时！");
    }
}
