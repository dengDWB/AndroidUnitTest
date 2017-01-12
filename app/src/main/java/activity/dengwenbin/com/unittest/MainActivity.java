package activity.dengwenbin.com.unittest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public String userAgent = "Mozilla/5.0 (Linux; U; Android 4.3; en-us; HTC One - 4.3 - API 18 - 1080x1920 Build/JLS36G) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 default-by-hand";
    OkhttpInterceptors okInter = new OkhttpInterceptors(404);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Map<String, String> headers = new HashMap<String, String>();
//        Map<String, String> a = new HttpUtil().httpGet("http://www.baidu.com", headers);
//        Log.d("OkHttp3",a.toString());

        startHttpGet();
    }

    public void startHttpGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Map<String, String> headers = new HashMap<String, String>();
//                Map<String, String> a = httpGet("http://www.baidu.com", headers);
//                Log.d("OkHttp3",a.toString());

                Map<String, String> headers = new HashMap<String, String>();
                Map<String, String> a = HttpUtil.httpGet("http://www.baidu.com", headers);
                Log.d("OkHttp3",a.toString());
            }
        }).start();
    }

    public Map<String, String> httpGet(String urlString, Map<String, String> headers) {
        Map<String, String> retMap = new HashMap<>();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(okInter)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        okhttp3.Request.Builder builder = new Request.Builder()
                .url(urlString)
                .addHeader("User-Agent", userAgent);

        if (headers.containsKey("ETag")) {
            builder = builder.addHeader("IF-None-Match", headers.get("ETag"));
        }
        if (headers.containsKey("Last_Modified")) {
            builder = builder.addHeader("If-Modified-Since", headers.get("Last_Modified"));
        }
        Response response;
        Request request = builder.build();
        try {
            response = client.newCall(request).execute();
            Headers responseHeaders = response.headers();
            boolean isJSON = false;
            for (int i = 0, len = responseHeaders.size(); i < len; i++) {
                retMap.put(responseHeaders.name(i), responseHeaders.value(i));
                // Log.i("HEADER", String.format("Key : %s, Value: %s", responseHeaders.name(i), responseHeaders.value(i)));
                isJSON = responseHeaders.name(i).equalsIgnoreCase("Content-Type") && responseHeaders.value(i).contains("application/json");
            }
            retMap.put("code", String.format("%d", response.code()));
            retMap.put("body", response.body().string());

//            if(isJSON) {
//                LogUtil.d("code", retMap.get("code"));
//                LogUtil.d("responseBody", retMap.get("body"));
//            }
        } catch (UnknownHostException e) {
            // 400: Unable to resolve host "yonghui.idata.mobi": No address associated with hostname
//            if(e != null && e.getMessage() != null) {
//                LogUtil.d("UnknownHostException2", e.getMessage());
//            }
            retMap.put("code", "400");
            retMap.put("body", "{\"info\": \"请检查网络环境！\"}");
        } catch (Exception e) {
            // Default Response
            retMap.put("code", "400");
            retMap.put("body", "{\"info\": \"请检查网络环境！\"}");

            if(e != null && e.getMessage() != null) {
                String errorMessage = e.getMessage().toLowerCase();
//                LogUtil.d("Exception", errorMessage);
                if (errorMessage.contains("unable to resolve host") || errorMessage.contains("failed to connect to")) {
                    retMap.put("code", "400");
                    retMap.put("body", "{\"info\": \"请检查网络环境！\"}");
                } else if (errorMessage.contains("unauthorized")) {
                    retMap.put("code", "401");
                    retMap.put("body", "{\"info\": \"用户名或密码错误\"}");
                }
            }
        }
        return retMap;
    }

}
