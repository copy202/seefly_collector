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

import java.util.*;

/**
 * Created by copy202 on 15/8/9.
 */
public class lrtsBookCategorySecondCrawler extends DeepCrawler {

    private String crawlPath;

    public static final String sideUrl = "http://www.lrts.me/book/";

    private static List<String> bookIdList = new ArrayList<String>();

    private String bookId = new String();

    static {
        bookIdList = DbTool.queryBookId("select book_id from tb_books_lrts");
    }

    public lrtsBookCategorySecondCrawler(String crawlPath) {
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

        bookId = bookIdList.remove(0);
        addSeed(sideUrl+bookId);
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

                Element bookinfo = doc.getElementsByClass("d-r").get(0);
                String status = bookinfo.getElementsByClass("d-status").get(0).text();
                String updateStatus = "update tb_books_lrts set status='"+status+"',udt=now() where book_id='"+bookId+"'";

                DbTool.execute(updateStatus);
                /*
                Elements liList = bookinfo.getElementsByTag("li");
                Element booktypeLi = null;
                for (Element li: liList){
                    if(li.toString().indexOf("类型：")!=-1){
                        booktypeLi = li;
                        continue;
                    }
                }
                //String typename = booktypeinner.parent().text();
                String typename=booktypeLi.text().substring(booktypeLi.text().indexOf("：")+1);
                String updateSQL = "update tb_books_lrts set tag2='"+typename+"',udt=now() where book_id='"+bookId+"'";
                DbTool.execute(updateSQL);
                */
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(bookIdList.size()>0){
            bookId = bookIdList.remove(0);
            links.add(sideUrl+bookId);
        }
        return links;
    }
}
