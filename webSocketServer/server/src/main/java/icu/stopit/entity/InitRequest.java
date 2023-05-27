package icu.stopit.entity;


import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class InitRequest implements Serializable {
    private String id;
    private String name;
    private String uuid;
    private String scheme;
    private String path;
    private String queryString;
    private String method;
    private HashMap<String, String> headers;
    private String contentType;
    private Map<String, String[]> parameters;
    private byte[] inputBytes;

    public InitRequest(String name, HttpServletRequest request) {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
        this.scheme = request.getScheme();
        this.path = request.getRequestURI();
        this.queryString = request.getQueryString();
        this.method = request.getMethod();
        this.headers = new HashMap<String, String>();
        for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements(); ) {
            String headerName = e.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        this.contentType = request.getContentType();
        this.parameters = request.getParameterMap();
        try {
            ServletInputStream inputStream = request.getInputStream();
            this.inputBytes = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
