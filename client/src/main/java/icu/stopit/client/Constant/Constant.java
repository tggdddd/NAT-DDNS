package icu.stopit.client.Constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class Constant {
    @Value("${icu.websocket.host}")
    private String serverWebSocketHost;
    @Value("${icu.websocket.port}")
    private String serverWebSocketPort;
}
