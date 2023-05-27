package icu.stopit.client.Util;

import icu.stopit.client.entity.InitRequest;
import icu.stopit.client.entity.InitResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ProxyUtil {
    public static InitResponse connect(InitRequest request, String host) {
        try {
            String basePath = request.getScheme() + "://" + host;
            String urlString = basePath + request.getPath();
            String queryString = request.getQueryString();
            urlString += queryString == null ? "" : "?" + queryString;
            URL url = new URL(urlString);
            System.out.println("请求链接：" + urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String methodName = request.getMethod();
            con.setRequestMethod(request.getMethod());
            con.setDoOutput(true);
            con.setDoInput(true);
            HttpURLConnection.setFollowRedirects(false);
            con.setUseCaches(true);
            // 将host修改为对应的host
            HashMap<String, String> headers = request.getHeaders();
            headers.put("host", host);
            request.setHeaders(headers);
            request.getHeaders().forEach(con::setRequestProperty);

            System.out.println("请求头:");
            request.getHeaders().forEach((k, v) -> System.out.println(k + ":" + v));
            con.connect();
            if ("POST".equalsIgnoreCase(methodName)) {
                String contentType = request.getContentType();
                if (contentType.toLowerCase().indexOf("application/x-www-form") != -1) {
                    Map<String, String[]> params = request.getParameters();
                    queryString = "";
                    for (String key : params.keySet()) {
                        String[] values = params.get(key);
                        for (int i = 0; i < values.length; i++) {
                            String value = values[i];
                            queryString += key + "=" + value + "&";
                        }
                    }
                    queryString = queryString.substring(0, queryString.length() - 1);
                    BufferedOutputStream proxyToWebBuf = new BufferedOutputStream(con.getOutputStream());
                    System.out.println("Post数据：" + queryString);
                    proxyToWebBuf.write(queryString.getBytes());
                    proxyToWebBuf.flush();
                    proxyToWebBuf.close();
                } else {
                    BufferedOutputStream proxyToWebBuf = new BufferedOutputStream(con.getOutputStream());
                    proxyToWebBuf.write(request.getInputBytes());
                    proxyToWebBuf.flush();
                    proxyToWebBuf.close();
                }
            }
            int statusCode = con.getResponseCode();
            InitResponse response = new InitResponse();
            response.setStatus(statusCode);
            for (Iterator<Map.Entry<String, List<String>>> i = con.getHeaderFields().entrySet().iterator(); i
                    .hasNext(); ) {
                Map.Entry<String, List<String>> mapEntry = i.next();
                if (mapEntry.getKey() != null)
                    response.addHeader(mapEntry.getKey().toString(), (mapEntry.getValue()).get(0).toString());
            }
            BufferedInputStream webToProxyBuf = new BufferedInputStream(con.getInputStream());
            response.setBytes(webToProxyBuf.readAllBytes());
            response.setName(request.getName());
            response.setUuid(request.getUuid());
            webToProxyBuf.close();
            con.disconnect();
            return response;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void writeResponse(InitResponse source, HttpServletResponse response) {
        response.setStatus(source.getStatus());
        source.getHeaders().forEach(response::setHeader);
        try {
            response.getWriter().write(Arrays.toString(source.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
