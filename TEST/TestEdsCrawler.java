import com.seefly.collector.process.EdsCrawler;
import org.junit.Test;

/**
 * Created by copy202 on 15/11/22.
 */
public class TestEdsCrawler {
    @Test
    public void test1() throws Exception{
        EdsCrawler edsCrawler = new EdsCrawler("/Users/copy202/test");
        edsCrawler.addSeed("http://eds.newtouch.cn/eds36web/DefaultLogin.aspx?lan=zh-cn");
        edsCrawler.start(1);
    }
}
