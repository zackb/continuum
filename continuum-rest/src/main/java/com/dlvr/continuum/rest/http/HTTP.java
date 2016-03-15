package com.dlvr.continuum.rest.http;

import com.dlvr.continuum.util.IO;
import com.dlvr.continuum.util.JSON;

import javax.net.ssl.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * HTTP client
 * Created by zack on 3/14/16.
 */
public class HTTP {

    private static final Map<String, String> DEFAULT_HEADERS;
    private static final Map<String, String> JSON_CONTENT_TYPE = new HashMap<>();

    static {
        DEFAULT_HEADERS = new HashMap<>(5);
        DEFAULT_HEADERS.put("User-Agent",        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");
        DEFAULT_HEADERS.put("Accept",            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        DEFAULT_HEADERS.put("Accept-Language",   "en-us,en;q=0.8");
        DEFAULT_HEADERS.put("Accept-Charset",    "utf-8,ISO-8859-1;q=0.7,*;q=0.7");
        DEFAULT_HEADERS.put("Connection",        "keep-alive");

        JSON_CONTENT_TYPE.put("Content-Type",    "application/json");

        // disable cert checking
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((string, ssls) -> true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static InputStream openInputStrem(Request request) throws Exception {
        return performRequest(request).inputStream;
    }

    public static Response performRequest(Request request) throws Exception {
        String urlStr = request.url;
        if (request.method == "GET" && request.data != null && request.data instanceof Map) {

            Map<String, Object> data = (Map<String, Object>)request.data;
            if (data.size() > 0) {
                if (urlStr.contains("?"))
                    urlStr += "&";
                else
                    urlStr += "?";

                StringBuffer sb = new StringBuffer();

                data.forEach((name, o) -> {
                    sb.append(name);
                    sb.append('=');
                    sb.append(o);
                    sb.append('&');
                });
                urlStr += sb.toString();
            }
        }

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(request.method == null ? "GET" : request.method);

        Map<String, String> hdrs = new HashMap<>();
        hdrs.putAll(DEFAULT_HEADERS);
        if (request.headers != null) {
            hdrs.putAll(request.headers);
        }

        for (String name : hdrs.keySet()) {
            conn.setRequestProperty(name, hdrs.get(name));
        }

        conn.setReadTimeout(request.readTimeout);
        conn.setConnectTimeout(request.connectTimeout);
        if (request.method.equalsIgnoreCase("POST") || request.method.equalsIgnoreCase("PUT")) {
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            OutputStream outs = conn.getOutputStream();
            InputStream requestIns = request.getInputStream();
            IO.copyStream(requestIns, outs);
            IO.close(requestIns);
            IO.close(outs);
        }

        Response response = new Response();
        String contentType = conn.getHeaderField("Content-Type");

        response.contentType(contentType);

        try {
            response.inputStream = conn.getInputStream();
        } catch (Exception e) {
            response.inputStream = conn.getErrorStream();
        }

        response.status = conn.getResponseCode();
        response.contentType = contentType;
        response.headers = new HashMap<>();

        conn.getHeaderFields()
                .forEach((s, strings) ->
                        response.headers.put(s, conn.getHeaderField(s)));

        return response;
    }

    public static String get(Request request) throws Exception {
        return IO.readString(openInputStrem(request));
    }

    public static String get(String url, Map<String, String> headers) throws Exception {
        return get(new Request().url(url).headers(headers));
    }

    public static Map<String, Object> getJSON(String url) throws Exception {
        return (Map<String, Object>)getJSONObject(url, JSON.MAP_CLASS);
    }

    public static <T> T getJSONObject(String url, Class<T> clazz) throws Exception {
        Request request = new Request()
                .url(url)
                .headers(JSON_CONTENT_TYPE)
                .method("GET");
        return JSON.decodeObject(get(request), clazz);
    }

    public static String post(String url, Object data, Map<String, String> headers) throws Exception {
        Request request = new Request()
                .url(url)
                .headers(headers)
                .data(data)
                .method("POST");
        return IO.readString(openInputStrem(request));
    }

    public static String postJSON(String url, Object data) throws Exception {
        return postJSON(url, data, null);
    }

    public static String postJSON(String url, Object data, Map<String, String> headers) throws Exception {
        if (headers == null) headers = new HashMap<>();
        headers.putAll(JSON_CONTENT_TYPE);
        return post(url, JSON.encode(data), headers);
    }
}
