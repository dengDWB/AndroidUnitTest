package activity.dengwenbin.com.unittest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by 40284 on 2017/1/12.
 */

public class OkhttpInterceptors implements Interceptor {
    public int code ;

    public OkhttpInterceptors (int code) {
        this.code = code;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        chain.proceed(chain.request());
        Response response = new Response.Builder()
                .code(code)
                .message("12345")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), "12345".getBytes()))
                .addHeader("content-type", "application/json")
                .build();
        return response;
    }

}
