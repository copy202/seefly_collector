import com.seefly.collector.process.Dytt1Crawler;
import com.seefly.collector.tools.DbTool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by copy202 on 15/12/27.
 */
public class TestDytt1Crawler {

    //电影启动类
    @Test
    public void testDytt1() throws Exception{

        DbTool.execute("delete from tb_film_dytt_seed");

        Dytt1Crawler crawler = new Dytt1Crawler("/Users/copy202/test","tb_film_dytt_seed");
        crawler.setThreads(20);
        ArrayList<String> seedList = new ArrayList<String>();
        String baseUrl = "http://www.dytt.com/fenlei/?.html";
        seedList.add(baseUrl.replace("?", "15"));
        for(int i=2;i<=100;i++){
        //for(int i=2;i<=2;i++){
            seedList.add(baseUrl.replace("?","15_"+i));
        }
        crawler.setSeeds(seedList);
        crawler.start(Integer.MAX_VALUE);
    }

    //电视剧启动类
    //@Test
    public void testDytt2() throws Exception{

        DbTool.execute("delete from tb_film_dytt_seed_dsj");

        Dytt1Crawler crawler = new Dytt1Crawler("/Users/copy202/test","tb_film_dytt_seed_dsj");
        crawler.setThreads(20);
        ArrayList<String> seedList = new ArrayList<String>();
        String baseUrl = "http://www.dytt.com/fenlei/?.html";
        seedList.add(baseUrl.replace("?", "16"));
        for(int i=2;i<=100;i++){
            //for(int i=2;i<=2;i++){
            seedList.add(baseUrl.replace("?","16_"+i));
        }
        crawler.setSeeds(seedList);
        crawler.start(Integer.MAX_VALUE);
    }
}
