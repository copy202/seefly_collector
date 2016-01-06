import com.seefly.collector.process.Dytt2Crawler;
import com.seefly.collector.process.Dytt2Extender;
import com.seefly.collector.tools.DbTool;
import org.junit.Test;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.lang.reflect.Array;
import java.util.*;
/**
 * Created by copy202 on 15/12/27.
 */
public class TestDytt2Crawler {
    //@Test
    public void testDytt2Crawler() throws Exception {
        ArrayList<String> urlList = (ArrayList)DbTool.queryIds("select url from tb_film_dytt_seed","url");
        Dytt2Crawler crawler = new Dytt2Crawler("/Users/copy202/test","电影");
        crawler.setThreads(20);
        crawler.setSeeds(urlList);
        //crawler.addSeed("http://www.dytt.com/xiazai/id10727.html");
        crawler.start(Integer.MAX_VALUE);
    }

    //@Test
    public void testDytt2Crawler_2() throws Exception {
        ArrayList<String> urlList = (ArrayList)DbTool.queryIds("select url from tb_film_dytt_seed_dsj","url");
        Dytt2Crawler crawler = new Dytt2Crawler("/Users/copy202/test","连续剧");
        crawler.setThreads(20);
        crawler.setSeeds(urlList);

        //crawler.addSeed("http://www.dytt.com/xiazai/id22171.html");
        crawler.start(Integer.MAX_VALUE);
    }

    @Test
    public void testDytt2Extend() throws Exception{
        //List<String> seeds = new ArrayList<String>();
        //ArrayList<String> seedsdy = (ArrayList)DbTool.queryIds("select sid from tb_film_dytt where update_time='' and subtype ='电影'","sid");

        //seeds.add("http://www.dytt.com/xiazai/id4666.html");
        //Dytt2Extender.execute(seedsdy,"电影");

        //ArrayList<String> seedsdsj = (ArrayList)DbTool.queryIds("select sid from tb_film_dytt where id>='19028' and subtype ='电影'","sid");

        //Dytt2Extender.execute(seedsdsj,"电影");

        //String[] test = "4657,4666,7544,9963,10042,11619,13668,13887,16298,18707".split(",");
        // List<String> seedsdsj = Arrays.asList(test);
        //Dytt2Extender.execute(seedsdsj,"连续剧");



        //日常任务---电影
        ArrayList<String> seedsdy = (ArrayList)DbTool.queryIds("select url from tb_film_dytt_seed","url");
        Dytt2Extender.execute(getIds(seedsdy),"电影");

        //日常任务---连续剧
        ArrayList<String> seedsdsj = (ArrayList)DbTool.queryIds("select url from tb_film_dytt_seed_dsj","url");
        Dytt2Extender.execute(getIds(seedsdsj),"电视剧");

    }

    private List<String> getIds(List<String> organList){
        List<String> ids = new ArrayList<String>();
        //http://www.dytt.com/xiazai/id18868.html
        for(String obj: organList){
            obj = obj.replaceAll("http://www.dytt.com/xiazai/id","");
            obj = obj.replaceAll(".html","");
            ids.add(obj);
        }
        return ids;
    }
}
