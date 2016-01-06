package com.seefly.collector.process; /**
 * Created by copy202 on 15/8/23.
 */
import org.junit.Test;

public class Testyyets1 {

    @Test
    public void test1() throws Exception {
        yyets1 crawlerTotal =  new yyets1("/Users/copy202/test");
        crawlerTotal.addSeed("http://yyets.cc/juju/index.html");

        crawlerTotal.start(Integer.MAX_VALUE);
        //crawlerTotal.start(3);
    }
}
