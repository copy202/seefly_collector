package com.seefly.collector.process;


import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;

import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import com.google.common.base.Charsets;
import com.seefly.collector.tools.DbTool;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.WebElement;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;
import com.seefly.collector.tools.PageUtils;
/**
 * Created by copy202 on 15/12/27.
 */
public class Dytt2Crawler extends DeepCrawler {

    private String crawlPath;

    private String subtype;

    private static final String baseSide =  "http://www.dytt.com";

    public Dytt2Crawler(String crawlPath,String subtype) {
        super(crawlPath);
        this.crawlPath = crawlPath;
        this.subtype = subtype;

        HttpRequesterImpl requester=(HttpRequesterImpl) this.getHttpRequester();

        requester.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        requester.setHeader("Accept-Encoding", "gzip, deflate, sdch");
        requester.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        requester.setHeader("Cache-Control", "max-age=0");
        requester.setHeader("Connection", "keep-alive");
        requester.setHeader("Host", "www.lrts.me");
        requester.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        requester.setCookie("IESESSION=alive; pgv_pvi=2217399296; pgv_si=s6058928128; uid=14403409235890da1beeca29046729c5e611fc2503441; JSESSIONID=BA7D1250C1BC33EAFB9D0B5ECFE4D7CE; CNZZDATA1254668430=634734317-1440339846-null%7C1440339846; Hm_lvt_ada61571fd48bb3f905f5fd1d6ef0ec4=1440340922; Hm_lpvt_ada61571fd48bb3f905f5fd1d6ef0ec4=1440342478");
        requester.setMethod("GET");
    }

    private void parseHtml(String content) {

        if (StringUtils.isNotBlank(content)) {
            Document doc = Jsoup.parse(content);
            String sid = doc.select("div#SOHUCS").attr("sid");
            //String title = doc.select("h1").text();

            String showtime = "";
            String status = "";
            String film_type = "";
            String casts = "";
            String countries = "";
            String update_time = "";
            String group = "";
            String update_cycle = "";

            Elements liElements = doc.select("div.info").select("li");

            for(Element ele :liElements){
                if(ele.text().indexOf("上映")!=-1 && ele.text().indexOf("状态")!=-1){
                    showtime = ele.text().split(" ")[0].substring(ele.text().split(" ")[0].indexOf("：")+1);
                    status = ele.text().split(" ")[1].substring(ele.text().split(" ")[1].indexOf("：")+1);
                }else if(ele.text().indexOf("上映")!=-1 && ele.text().indexOf("状态")==-1){
                    showtime = ele.text().substring(ele.text().indexOf(":")+1);
                }else if(ele.text().indexOf("上映")==-1 && ele.text().indexOf("状态")!=-1){
                    status = ele.text().substring(ele.text().indexOf(":")+1);
                }
                if(ele.text().indexOf("类型")!=-1){
                    film_type = ele.text().substring(ele.text().indexOf("：") + 1);
                }
                if(ele.text().indexOf("主演")!=-1){
                    casts = ele.text().substring(ele.text().indexOf("：")+1);
                }
                if(ele.text().indexOf("地区")!=-1){
                    countries = ele.text().substring(ele.text().indexOf("：")+1);
                }
                if(ele.text().indexOf("更新日期")!=-1 && ele.text().indexOf("更新周期")!=-1){
                    update_time = ele.text().split(" ")[0].substring(ele.text().split(" ")[0].indexOf("：")+1);
                    update_cycle = ele.text().split(" ")[1].substring(ele.text().split(" ")[1].indexOf("：")+1);
                }else if(ele.text().indexOf("更新日期")!=-1 && ele.text().indexOf("更新周期")==-1){
                    update_time = ele.text().substring(ele.text().indexOf("：")+1);
                }else if(ele.text().indexOf("更新日期")==-1 && ele.text().indexOf("更新周期")!=-1){
                    update_cycle = ele.text().substring(ele.text().indexOf("：")+1);
                }
            }

            String rating = doc.select("span#filmStarScore").text();

            String summary = String.valueOf(doc.select("div.alltext").text());

            String img = doc.select("div.endinfo").select(".pic").select("img").attr("src");

            String title = doc.select("div.endinfo").select(".pic").select("img").attr("alt");


            Elements downlist = doc.select("div.downlist");
            for (Element downlistEle:downlist){
                group = downlistEle.select("h4").text();
                Elements scripts = downlistEle.select("script");
                String ed2ks = "";
                List<String> ed2kList = new ArrayList<String>();
                for (Element script:scripts){
                    if(script.childNodes().size()==0){
                        continue;
                    }
                    String data = script.childNode(0).attributes().get("data");
                    if(data.indexOf("GvodUrls")!=-1){
                        int start = data.indexOf("\"");
                        int end = data.lastIndexOf("\"");
                        ed2ks = data.substring(start + 1, end);
                    }
                }
                ed2kList.addAll(Arrays.asList(ed2ks.split("###")));
                if(ed2kList.size()>0){
                    for (String ed2k:ed2kList){
                        int test = DbTool.queryCount("select count(0) as count from tb_film_dytt_thunder where film_dytt_id='"+sid+"' and ed2k_url='"+ed2k+"'");
                        if(test==0){
                            //如果没有就新增
                            String sql = "insert into tb_film_dytt_thunder(film_dytt_id,title,ed2k_url,create_time,groupname) values('%s','%s','%s',now(),'%s')";
                            sql = String.format(sql, sid,title,ed2k,group);
                            //System.err.println(sql);
                            DbTool.execute(sql);
                        }
                    }
                }
            }





            int has = DbTool.queryCount("select count(0) as count from tb_film_dytt where sid='" + sid + "'");
            if(has>0){
                //更新
                String updateSQL = "update tb_film_dytt set status='%s',update_time='%s',update_cycle='%s',summary='%s' where sid='"+sid+"'";
                updateSQL = String.format(updateSQL,status,update_time,update_cycle,summary);
                boolean result = DbTool.execute(updateSQL);
            }else{
                //新增
                String sql = "insert into tb_film_dytt (sid,title,img,film_type,status,rating,casts,countries,summary,subtype,show_time,update_time,update_cycle) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
                sql = String.format(sql,sid,title,img,film_type,status,rating,casts,countries,summary,this.subtype,showtime,update_time,"");
                boolean result = DbTool.execute(sql);
                if(result==false){
                    String sql2 = "insert into tb_film_dytt (sid,title,img,film_type,status,rating,casts,countries,summary,subtype,show_time,update_time,update_cycle) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
                    sql2 = String.format(sql2,sid,title,img,film_type,status,rating,casts,countries,"",this.subtype,showtime,update_time,"");
                    DbTool.execute(sql2);
                }
            }
            //System.out.println(img);
        }
    }

    @Override
    public Links visitAndGetNextLinks(Page page) {
        /*HtmlUnitDriver可以抽取JS生成的数据*/
        //HtmlUnitDriver driver= PageUtils.getDriver(page, BrowserVersion.CHROME);
        /*HtmlUnitDriver也可以像Jsoup一样用CSS SELECTOR抽取数据
          关于HtmlUnitDriver的文档请查阅selenium相关文档*/
        //List<WebElement> divInfos=driver.findElementsByCssSelector("a[href]");
        //for(WebElement divInfo:divInfos){
        //    System.out.println(divInfo.getText());
        //}

        /*日期排序
        SELECT
            *
        FROM
            tb_film_dytt
        ORDER BY
            DATE_FORMAT(
                update_time,
                '%Y-%m-%d %H:%i:%s'
            ) DESC;
        */

        String homePageHtml = page.getHtml();
        System.out.println("正在抓取@~~~~~~~~~~~~~~~~@ "+page.getUrl());
        if(page.getResponse().getCode()==200) {
            parseHtml(homePageHtml);
        }
        return null;
    }
}
