package com.quwen.util.xunfei;

import com.google.gson.Gson;
import com.quwen.util.common.CacheUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;


@Slf4j
public class WebTtsWs {

    public static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(10, 50,
            5L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(60),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void startSocket(String hostUrl, String text, Integer key, String appid, String apiKey, String apiSecret,String VCN) throws Exception {
        String wsUrl = getAuthUrl(hostUrl, apiKey, apiSecret).replace("https://", "wss://");
        String filePath = AudioUtils.getTmpFilePath("pcm");
        OutputStream outputStream = new FileOutputStream(filePath);
        websocketWork(wsUrl, outputStream, key, filePath, text, appid,VCN);
    }

    // Websocket方法
    public static void websocketWork(String wsUrl, OutputStream outputStream, int key, String path, String text, String appid, String VCN) {
        try {
            URI uri = new URI(wsUrl);
            WebSocketClient webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("TtsWs建立连接成功...");
                }

                @Override
                public void onMessage(String text) {
                    JsonParse myJsonParse = new Gson().fromJson(text, JsonParse.class);
                    if (myJsonParse.code != 0) {
                        log.error("TTS发生错误，错误码为:{}, 本次请求的sid:{}", myJsonParse.code, myJsonParse.sid);
                    }
                    if (myJsonParse.data != null) {
                        try {
                            byte[] textBase64Decode = Base64.getDecoder().decode(myJsonParse.data.audio);
                            outputStream.write(textBase64Decode);
                            outputStream.flush();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        if (myJsonParse.data.status == 2) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            log.info("本次请求的sid==>" + myJsonParse.sid);
                            log.info("合成成功，文件保存路径为==>" + path);
                            CacheUtils.putPathIntoCache(key, path);
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("TtsWs链接已关闭，本次请求完成...");
                }

                @Override
                public void onError(Exception e) {
                    log.error("TtsWs Exception", e);
                    CacheUtils.clearPathCache(key);
                }
            };
            // 建立连接
            webSocketClient.connect();
            while (!webSocketClient.getReadyState().equals(ReadyState.OPEN)) {
                Thread.sleep(100);
            }
            THREAD_POOL.execute(() -> {
                String requestJson;//请求参数json串
                try {
                    requestJson = "{\n" +
                            "  \"common\": {\n" +
                            "    \"app_id\": \"" + appid + "\"\n" +
                            "  },\n" +
                            "  \"business\": {\n" +
                            "    \"aue\": \"raw\",\n" +
                            "    \"tte\": \"" + "UTF8" + "\",\n" +
                            "    \"ent\": \"intp65\",\n" +
                            "    \"vcn\": \"" + VCN + "\",\n" +
                            "    \"pitch\": 50,\n" +
                            "    \"speed\": 50\n" +
                            "  },\n" +
                            "  \"data\": {\n" +
                            "    \"status\": 2,\n" +
                            "    \"text\": \"" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)) +
                            "\"\n" +
                            "  }\n" +
                            "}";
                    webSocketClient.send(requestJson);
                    // 等待服务端返回完毕后关闭
                    Thread.sleep(500);
                    webSocketClient.close();
                    outputStream.close();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 鉴权方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).
                addQueryParameter("date", date).
                addQueryParameter("host", url.getHost()).
                build();

        return httpUrl.toString();
    }

    //返回的json结果拆解
    class JsonParse {
        int code;
        String sid;
        Data data;
    }

    class Data {
        int status;
        String audio;
    }
}
