package com.seefly.collector.process;

import org.junit.Test;

/**
 * Created by copy202 on 15/12/25.
 */
public class Testyyets3 {
    @Test
    public void test1() throws Exception {
        yyets3 crawlerTotal =  new yyets3("/Users/copy202/test");


        crawlerTotal.start(Integer.MAX_VALUE);
        //crawlerTotal.start(3);
    }
}
