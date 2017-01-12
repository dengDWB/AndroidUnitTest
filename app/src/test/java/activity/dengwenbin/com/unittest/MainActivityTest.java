package activity.dengwenbin.com.unittest;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 40284 on 2017/1/12.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {
//    @Spy
//    OkhttpInterceptors okhttpInterceptors;
     HttpUtil httpUtil;
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void httpGet() throws Exception {
//        okhttpInterceptors = Mockito.mock(OkhttpInterceptors.class);

        httpUtil = Mockito.mock(HttpUtil.class);
        Map<String, String> headers = new HashMap<String, String>();
        String result = httpUtil.httpGet("http://www.baidu.com", headers).toString();
//        verify(httpUtil).httpGet("http://www.baidu.com", headers);

//        verify(okhttpInterceptors).setParameter(404);
//        String result = httpUtil.httpGet("http://www.baidu.com", headers).toString();
//        Assert.assertNotNull(okhttpInterceptors);
        Assert.assertEquals(result,"{code=404, content-type=application/json, body=12345}");
    }



}