package corp.click.touroffice;

import static com.google.firebase.messaging.Constants.TAG;
import static corp.click.touroffice.OkHttpClass.SERVER_URL_HOST;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //뒤로 버튼 두번 클릭 종료
        backPressCloseHandler = new BackPressCloseHandler(this);

        //네트워크 상태 확인
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); //javascript 활성화
        webView.setWebViewClient(new WebViewClient());

        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if(wifi.isConnected() || mobile.isConnected()) {
            webView.loadUrl(SERVER_URL_HOST);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            String netname = ni.getTypeName();
            Toast.makeText(getApplicationContext(), netname + "로 연결되었습니다.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해 주세요.", Toast.LENGTH_LONG).show();
            finish();
        }

        //웹뷰 javascript 처리시 인터페이스 이름 필요
        webView.addJavascriptInterface(new JavaScriptInterface(this),"android");

        //alert, confirm 허용
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url,
                                     String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.confirm();
                                    }
                                }).setCancelable(false).create().show();

                return true;
            };

            public boolean onJsConfirm(WebView view, String url,
                                       String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.cancel();
                                    }
                                }).setCancelable(false).create().show();
                return true;
            }

        });

        //fcm token
        logRegToken();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    private void logRegToken() {
        // [START log_reg_token]
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = "FCM 토큰 : " + token;
                        //Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        sendTokenToServer(token); //앱 실행시 토큰 저장

                    }
                });
        // [END log_reg_token]
    }

    private void sendTokenToServer(String token) {
        // Create a new thread to send the token to the server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //서버에 저장
                    OkHttpClass okHttpClass = new OkHttpClass();
                    okHttpClass.sendToken(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}