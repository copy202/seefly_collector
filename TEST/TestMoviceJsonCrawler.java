import com.seefly.collector.process.MoviceJsonCrawler;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by copy202 on 15/8/9.
 */
public class TestMoviceJsonCrawler {
    @Test
    public void test1() throws Exception {
        MoviceJsonCrawler crawlerTotal =  new MoviceJsonCrawler("/Users/copy202/test");
        String baseUrl ="http://www.douban.com/j/tag/items?start=%d&limit=%d&topic_id=60443&mod=movie";
        crawlerTotal.addSeed("http://www.douban.com/j/tag/items?start=10000000&limit=10&topic_id=60443&mod=movie");
        //crawlerTotal.addSeed("http://www.douban.com");
        crawlerTotal.start(1);

        int total = crawlerTotal.getTotal();
        System.err.println("total="+total);

        MoviceJsonCrawler crawler =  new MoviceJsonCrawler("/Users/copy202/test");
        for(int i=0;i<total;i=i+10){
            crawler.addSeed(String.format("http://www.douban.com/j/tag/items?start=%d&limit=%d&topic_id=60443&mod=movie", i, 10));
        }

        crawler.start(1);


    }
}
