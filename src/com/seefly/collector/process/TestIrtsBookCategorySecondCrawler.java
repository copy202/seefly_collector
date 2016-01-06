package com.seefly.collector.process; /**
 * Created by copy202 on 15/8/23.
 */
import org.junit.Test;

public class TestIrtsBookCategorySecondCrawler {

    @Test
    public void test1() throws Exception {
        lrtsBookCategorySecondCrawler crawlerTotal =  new lrtsBookCategorySecondCrawler("/Users/copy202/test");

        crawlerTotal.start(Integer.MAX_VALUE);
        //crawlerTotal.start(3);
    }
}
