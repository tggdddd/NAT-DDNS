package icu.stopit.client.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class InitResponse implements Serializable {
    private String id;
    /* message的处理器 */
    private String name;
    /* 消息id */
    private String uuid;
    private int status;
    private HashMap<String, String> headers;
    private byte[] bytes;

    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }
}
