package com.hsj.okhttp;

import com.hsj.logger.HsjLogger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class WebSocketClient {
    private Request request;
    private OkHttpClient client;
    private WebSocket webSocket;

    public WebSocketClient() {
        client = new OkHttpClient();
        request = new Request.Builder()
                .url("ws://echo.websocket.org")
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void start(WebSocketListener listener) {
        client.dispatcher().cancelAll();
        HsjLogger.d("request id = " + request.toString());
        HsjLogger.d("listener id = " + listener.toString());
        webSocket = client.newWebSocket(request, listener);
        HsjLogger.d("webSocket id = " + webSocket.toString());
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, null);
        }
        client.dispatcher().executorService().shutdown();
    }
}
