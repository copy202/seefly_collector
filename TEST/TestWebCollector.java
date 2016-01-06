import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.net.RequestConfig;
import org.junit.Test;

/**
 * Created by copy202 on 15/8/9.
 */
public class TestWebCollector {
    @Test
    public void demo1() throws Exception {

        HttpRequest request = new HttpRequest("http://www.csdn.net");

        HttpResponse response = request.getResponse();
        String html = response.getHtmlByCharsetDetect();

        //System.out.println(html);
    }

    //@Test
    public void demo2() throws Exception {
        RequestConfig requestConfig = new RequestConfig();
        requestConfig.setMethod("GET");
        requestConfig.setUserAgent("WebCollector");
        requestConfig.setCookie("xxxxxxxxxxxxxx");
        requestConfig.addHeader("xxx", "xxxxxxxxx");

        HttpRequest request = new HttpRequest("http://www.csdn.net", requestConfig);

        HttpResponse response = request.getResponse();
        String html = response.getHtmlByCharsetDetect();

        System.out.println(html);
        System.out.println("response code=" + response.getCode());
        System.out.println("Server=" + response.getHeader("Server"));
    }


    
}
