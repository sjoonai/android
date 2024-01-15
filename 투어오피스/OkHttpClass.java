package corp.click.touroffice;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpClass {

    public static final String SERVER_URL_HOST = "https://tour39ml.touroffice.co.kr";

    public void sendToken(String token) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token", token)
                .build();

        //request
        Request request = new Request.Builder()
                .url(SERVER_URL_HOST+"/son/fcm.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //아이디 매칭
    public void sendTokenUserid(String token,String userid) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token", token)
                .add("userid", userid)
                .build();

        //request
        Request request = new Request.Builder()
                .url(SERVER_URL_HOST+"/son/fcm.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
