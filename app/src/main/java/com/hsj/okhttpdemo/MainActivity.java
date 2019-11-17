package com.hsj.okhttpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hsj.bean.FileUploadResponse;
import com.hsj.bean.Tag;
import com.hsj.bean.Token;
import com.hsj.logger.HsjLogger;
import com.hsj.okhttp.WebSocketClient;
import com.hsj.okhttpdemo.net.callback.BaseCallback;
import com.hsj.okhttpdemo.net.subscribe.HsjSubscribe;
import com.hsj.okhttpdemo.permission.PermissionUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private WebSocketClient webSocketClient;
    private HsjWebSocketListener listener = new HsjWebSocketListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //刷新token
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtil.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                PermissionUtil.needPermission(this, 89, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        textView = findViewById(R.id.tv);
    }

    public void getToken(View view) {
        HsjLogger.d("getToken");
        HsjSubscribe.getToken(new BaseCallback<Token>() {
            @Override
            public void onSuccess(Token result) {
                HsjLogger.d("--getToken--onSuccess--");
            }

            @Override
            public void onFault(int code, String errorMsg) {
                HsjLogger.d("--getToken--onFault--");
            }

            @Override
            public void netError() {
                HsjLogger.d("--getToken--netError--");
            }
        });
    }

    public void uploadFile(View view) {
        File file = new File(getExternalFilesDir(null) + File.separator + "image/icon_weixin_kefu.png");
        HsjSubscribe.uploadFileToGetUrl(1, file, new BaseCallback<FileUploadResponse>() {
            @Override
            public void onSuccess(FileUploadResponse result) {
                HsjLogger.d("--upload--onSuccess--");
            }

            @Override
            public void onFault(int code, String errorMsg) {
                HsjLogger.d("--upload--onFault--");
            }

            @Override
            public void netError() {
                HsjLogger.d("--upload--netError--");
            }
        });
    }

    public void jsonRequest(View view) {
        HsjSubscribe.getTag(new BaseCallback<List<Tag>>() {

            @Override
            public void onSuccess(List<Tag> result) {
                HsjLogger.d("--onSuccess--");
            }

            @Override
            public void onFault(int code, String errorMsg) {
                HsjLogger.d("--onFault--" + errorMsg);
            }

            @Override
            public void netError() {
                HsjLogger.d("--netError--");
            }
        });
    }

    public void formRequest(View view) {
        HsjSubscribe.getCodeSms("13079079998", new BaseCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                HsjLogger.d("--login--onSuccess--");
            }

            @Override
            public void onFault(int code, String errorMsg) {
                HsjLogger.d("--login--onFault--");
            }

            @Override
            public void netError() {
                HsjLogger.d("--login--netError--");
            }
        });
    }


    StringBuilder stringBuilder = new StringBuilder();

    public void initSocket(View view) {
        webSocketClient = new WebSocketClient();
    }

    public void webSocket(View view) {
        if (webSocketClient == null) {
            Toast.makeText(this, "请初始化Socket", Toast.LENGTH_SHORT).show();
            return;
        }
        stringBuilder.setLength(0);
        stringBuilder.append(System.currentTimeMillis() + "-onClick\n");
        output();
        webSocketClient.start(listener);
    }

    public void closeSocket(View view) {
        webSocketClient.close();
    }

    private class HsjWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            stringBuilder.append(System.currentTimeMillis() + "\n");
            webSocket.send("hello world");
            webSocket.send("welcome");
            webSocket.send(ByteString.decodeHex("adef"));
            webSocket.close(1000, "再见");
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            stringBuilder.append(System.currentTimeMillis() + "-onMessage: " + text + "\n");
            output();
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            stringBuilder.append(System.currentTimeMillis() + "-onMessage byteString: " + bytes + "\n");
            output();
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            HsjLogger.d("onFailure: " + t.getMessage());
            stringBuilder.append(System.currentTimeMillis() + "-onFailure: " + t.getMessage() + "\n");
            output();
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            stringBuilder.append(System.currentTimeMillis() + "-onClosing: " + code + "/" + reason + "\n");
            output();
        }

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            stringBuilder.append(System.currentTimeMillis() + "-onClosed: " + code + "/" + reason + "\n");
            output();
        }
    }

    private void output() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(stringBuilder.toString());
            }
        });
    }
}
