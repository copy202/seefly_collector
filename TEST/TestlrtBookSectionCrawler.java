import com.seefly.collector.process.lrtsBookSectionCrawler;
import com.seefly.collector.tools.DbTool;
import org.junit.Test;

import java.util.List;

/**
 * Created by copy202 on 15/8/27.
 */
public class TestlrtBookSectionCrawler {
    @Test
    public void test1() throws Exception {

        List<String> bookIdList = DbTool.queryBookId("select book_id from tb_books_lrts");
        for(String bookId:bookIdList){
            lrtsBookSectionCrawler crawler = new lrtsBookSectionCrawler("/Users/copy202/test",bookId,"0");
            crawler.start(Integer.MAX_VALUE);
        }
    }
}
