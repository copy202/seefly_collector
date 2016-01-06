package com.seefly.collector.process;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import com.seefly.collector.tools.DbTool;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by copy202 on 15/12/27.
 */
public class Dytt1Crawler extends DeepCrawler {

    private String crawlPath;

    private String inserttable;

    private static final String baseSide =  "http://www.dytt.com";

    public Dytt1Crawler(String crawlPath,String inserttable) {
        super(crawlPath);
        this.crawlPath = crawlPath;
        this.inserttable = inserttable;

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
        parseHtml(homePageHtml);
        return null;
    }

    private void parseHtml(String content) {
        if (StringUtils.isNotBlank(content)) {
            Document doc = Jsoup.parse(content);
            Elements aList = doc.select("p.s1").select("a[href]");
            if(aList!=null && aList.size()>0){
                for (int j=0;j<aList.size();j++){
                    String subhref = aList.get(j).attr("href");
                    String fullhref = baseSide + subhref;
                    DbTool.execute("insert into "+this.inserttable+"(url,status) values ('"+fullhref+"','0')");
                }
            }
        }
    }
}
