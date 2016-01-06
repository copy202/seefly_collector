import com.seefly.collector.process.GupiaoCrawler;
import com.seefly.collector.process.MailCrawler;
import com.seefly.collector.tools.DbTool;
import org.junit.Test;

import java.util.List;

/**
 * Created by copy202 on 15/10/16.
 */
public class TestGupiaoCrawler {

    public void test1() throws Exception{
        GupiaoCrawler mc = new GupiaoCrawler("/Users/copy202/test");
        mc.addSeed("http://bbs.10jqka.com.cn/codelist.html");
        mc.start(1);

    }

    @Test
    public void test2() throws Exception{
        List<String> codes = DbTool.queryIds("select * from gpinfo", "gpcode");
        GupiaoCrawler mc = new GupiaoCrawler("/Users/copy202/test",codes,"getstartdate");
        mc.addSeed("http://www.163.com");
        mc.start(Integer.MAX_VALUE);
    }
    
}
