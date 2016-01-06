import com.seefly.collector.process.MailCrawler;
import org.junit.Test;

/**
 * Created by copy202 on 15/9/11.
 */
public class TestMailCrawler {
    @Test
    public void test1() throws Exception{
        MailCrawler mc = new MailCrawler("/Users/copy202/test");
        mc.addSeed("http://mail.newtouch.cn/extmail/cgi/index.cgi");
        mc.start(1);
    }
}
