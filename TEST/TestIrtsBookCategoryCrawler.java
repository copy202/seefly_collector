/**
 * Created by copy202 on 15/8/23.
 */
import com.seefly.collector.process.lrtsBookCategoryCrawler;
import org.junit.Test;
public class TestIrtsBookCategoryCrawler {

    @Test
    public void test1() throws Exception {
        lrtsBookCategoryCrawler crawlerTotal =  new lrtsBookCategoryCrawler("/Users/copy202/test");
        crawlerTotal.addSeed("http://www.baidu.com");
        //crawlerTotal.addSeed("http://www.lrts.me/book/category/1/latest/1/20");
        //crawlerTotal.addSeed("http://www.lrts.me/book/category/1/latest/133/20");
        crawlerTotal.start(Integer.MAX_VALUE);
        //crawlerTotal.start(3);
    }
}
