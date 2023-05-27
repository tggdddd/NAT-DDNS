package icu.stopit.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@TableName("response")
@AllArgsConstructor
@NoArgsConstructor
public class InitResponse implements Serializable {
    @TableId
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
