package icu.stopit.Utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.Decoder;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import org.springframework.util.StringUtils;

public class ObjectMessageDecoder implements Decoder.Text<Object>, Encoder.Text<Object> {
    @Override
    public Object decode(String s) {
        return StringUtils.hasText(s) ? JSON.parseObject(s) : new JSONObject();
    }

    @Override
    public boolean willDecode(String s) {
        return JSON.isValidObject(s);
    }

    @Override
    public String encode(Object o) {
        return o == null ? new JSONObject().toJSONString() : JSON.toJSONString(o);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        Decoder.Text.super.init(endpointConfig);
    }

    @Override
    public void destroy() {
        Decoder.Text.super.destroy();
    }
}
