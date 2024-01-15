package corp.click.touroffice;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static corp.click.touroffice.OkHttpClass.SERVER_URL_HOST;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * Created by son on 2017-11-23.
 * 웹뷰에서 앱 메서드 실행
 */

public class JavaScriptInterface implements TextToSpeech.OnInitListener{
    private Context mContext;
    private TextToSpeech tts;
    private String ttsMent;
    private String ttsLocale; //로케일 지정

    JavaScriptInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();

    }

    @JavascriptInterface
    public void webViewClose () {
        Toast.makeText(mContext, "앱 종료됩니다~", Toast.LENGTH_SHORT).show();

        //일반 클래스에서 자신을 생성한 Activity finish 하기
        ((Activity) mContext).finish();
    }

    @JavascriptInterface
    public void speakTts(String ment, String lang) {
        ttsMent = ment;
        tts = new TextToSpeech(mContext, this);
        ttsLocale = lang;
        //tts.shutdown(); //앱 종료시 오류가 있길래 추가
    }

    @Override
    public void onInit(int i) {
        Toast.makeText(mContext, ttsMent, Toast.LENGTH_LONG).show();
        //발음 로케일 변경
        if(ttsLocale.equals("영어")) {
            tts.setLanguage(Locale.US);
        } else {
            tts.setLanguage(Locale.KOREAN);
        }
        tts.speak(ttsMent, TextToSpeech.QUEUE_FLUSH, null);
    }

    //안드로이드 아이디와 사용자 아이디 매칭
    @JavascriptInterface
    public void setUserCheck(String userid) {
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
                        String msg = "FCM 토큰 매칭 : " + token;
                        //Log.d(TAG, msg);
                        //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        sendTokenUseridToServer(token,userid); //토큰 매칭

                    }
                });
        // [END log_reg_token]

    }

    private void sendTokenUseridToServer(String token,String userid) {
        // Create a new thread to send the token to the server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //서버에 저장
                    OkHttpClass okHttpClass = new OkHttpClass();
                    okHttpClass.sendTokenUserid(token,userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
