package com.seefly.collector.process;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import com.seefly.collector.tools.DbTool;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.UUID;

/**
 * Created by copy202 on 15/12/25.
 */
public class yyets1 extends DeepCrawler {

    private String crawlPath;

    public static final String sideUrl = "http://yyets.cc";

    public static int totalPage = 0;

    public static int currentPage = 1;

    public yyets1(String crawlPath) {
        super(crawlPath);
        this.crawlPath = crawlPath;
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

    @Override
    public Links visitAndGetNextLinks(Page page) {
        String homePageHtml = page.getHtml();
        Links nextLink = parseHtml(homePageHtml);

        return nextLink;
    }

    private Links parseHtml(String content) {
        Links links  = new Links();
        try {
            if (StringUtils.isNotBlank(content)) {
                Document doc = Jsoup.parse(content);
                if(this.totalPage == 0 ) {
                    Element lastPage = doc.getElementsByClass("page").get(0).getElementsByTag("a").last();
                    String flag = "index";
                    int startIndex = lastPage.attr("href").lastIndexOf(flag);
                    int endIndex = lastPage.attr("href").lastIndexOf(".");
                    this.totalPage = Integer.valueOf(lastPage.attr("href").substring(startIndex + flag.length(), endIndex));
                    System.err.println(totalPage);
                }

                Elements liElements = doc.getElementsByClass("shannel").get(0).getElementsByTag("li");

                for(Element liElement:liElements){
                    String href = liElement.getElementsByClass("ah").get(0).attr("href");
                    String name = liElement.getElementsByTag("h2").get(0).getElementsByTag("a").get(0).text();
                    String zhuyanStr = liElement.getElementsByTag("p").get(0).text();
                    int index = zhuyanStr.indexOf("：");
                    String zhuyan = zhuyanStr.substring(index + 1);
                    String imgUrl = liElement.getElementsByClass("ah").get(0).getElementsByTag("img").get(0).attr("original");

                    System.out.println("href="+href+",name="+name);

                    String selectSQL = "select count(0) as count from yyets_lianxuju where name='"+name+"'";
                    String insertSQL = "insert into yyets_lianxuju(id,name,href,crt_time,zhuyan,imgurl,isupdate) values ('"+ UUID.randomUUID()+"','"+name+"','"+href+"', now(),'"+zhuyan+"','"+imgUrl+"','0')";
                    String updateSQL = "update yyets_lianxuju set href='"+href+"',upt_time=now(),zhuyan='"+zhuyan+"',imgurl='"+imgUrl+"',isupdate='0' where name='"+name+"'";
                    int count = DbTool.queryCount(selectSQL);
                    if(count==0){
                        System.out.println(insertSQL);
                        DbTool.execute(insertSQL);
                    }else{
                        System.out.println(updateSQL);
                        DbTool.execute(updateSQL);
                    }
                }

                currentPage ++;
                if(currentPage<=totalPage) {
                    links.add("http://yyets.cc/juju/index" + currentPage + ".html");
                    return links;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
