package com.seefly.collector.main;

import com.seefly.collector.process.Dytt1Crawler;
import com.seefly.collector.process.Dytt2Extender;
import com.seefly.collector.tools.DbTool;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by copy202 on 16/1/4.
 */
public class startup_day {

    public static void main(String[] args) throws Exception {

        /*--------------------------------一天多次删除sql文件--------------------*/
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String exportfilename = dateFormat.format(now)+".sql";
        try {
            FileUtils.forceDelete(new File("/Users/copy202/Desktop/"+exportfilename));
        }catch (FileNotFoundException e){
            System.out.println(".........");
        }

        /*--------------------------------种子 begin---------------------------*/
        int updatePageNum = 2;
        /*电影*/
        DbTool.execute("delete from tb_film_dytt_seed");

        Dytt1Crawler crawler = new Dytt1Crawler("/Users/copy202/test", "tb_film_dytt_seed");
        crawler.setThreads(20);
        ArrayList<String> seedList = new ArrayList<String>();
        String baseUrl = "http://www.dytt.com/fenlei/?.html";
        seedList.add(baseUrl.replace("?", "15"));
        for (int i = 2; i <= updatePageNum; i++) {
            seedList.add(baseUrl.replace("?", "15_" + i));
        }
        crawler.setSeeds(seedList);
        crawler.start(Integer.MAX_VALUE);

        /*电视剧*/
        DbTool.execute("delete from tb_film_dytt_seed_dsj");

        Dytt1Crawler crawler_dsj = new Dytt1Crawler("/Users/copy202/test", "tb_film_dytt_seed_dsj");
        crawler_dsj.setThreads(20);
        ArrayList<String> seedList_dsj = new ArrayList<String>();
        String baseUrl_dsj = "http://www.dytt.com/fenlei/?.html";
        seedList_dsj.add(baseUrl_dsj.replace("?", "16"));
        for (int i = 2; i <= updatePageNum; i++) {
            seedList_dsj.add(baseUrl_dsj.replace("?", "16_" + i));
        }
        crawler_dsj.setSeeds(seedList_dsj);
        crawler_dsj.start(Integer.MAX_VALUE);

        /*--------------------------------种子 end---------------------------*/


        /*--------------------------------文件 begin---------------------------*/
        //日常任务---电影
        ArrayList<String> seedsdy = (ArrayList) DbTool.queryIds("select url from tb_film_dytt_seed", "url");
        Dytt2Extender.execute(getIds(seedsdy), "电影");

        //日常任务---连续剧
        ArrayList<String> seedsdsj = (ArrayList) DbTool.queryIds("select url from tb_film_dytt_seed_dsj", "url");
        Dytt2Extender.execute(getIds(seedsdsj), "连续剧");
        /*--------------------------------文件 end---------------------------*/

        System.out.println("生成sql文件完成,正在转换链接...");


/*
        Runtime r = Runtime.getRuntime();
        Process p1 = r.exec("/usr/local/bin/node  /Users/copy202/workspace/javascript/2_pre.js");
        //int exitVal = p1.waitFor();
        //System.out.println("Process exitValue: " + exitVal);

        InputStream in1 = p1.getInputStream();
        InputStreamReader isr1= new InputStreamReader(in1);
        BufferedReader br1 = new BufferedReader(isr1);
        String line = null;
        while((line = br1.readLine()) != null){
            System.out.println(line);
        }
        p1.waitFor();
        br1.close();
        isr1.close();
        in1.close();

        Process p2 = r.exec("/usr/local/bin/node  /Users/copy202/workspace/javascript/2.js");
        InputStream in2 = p2.getInputStream();
        InputStreamReader isr2= new InputStreamReader(in2);
        BufferedReader br2 = new BufferedReader(isr2);
        String line2 = null;
        while((line2 = br2.readLine()) != null){
            System.out.println(line2);
        }
        p2.waitFor();
        br2.close();
        isr2.close();
        in2.close();

        System.out.println("转换链接完成");
*/
    }

    private static List<String> getIds(List<String> organList){
        List<String> ids = new ArrayList<String>();
        for(String obj: organList){
            obj = obj.replaceAll("http://www.dytt.com/xiazai/id","");
            obj = obj.replaceAll(".html","");
            ids.add(obj);
        }
        return ids;
    }
}
