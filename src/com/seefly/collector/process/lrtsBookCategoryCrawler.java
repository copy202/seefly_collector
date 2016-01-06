package com.seefly.collector.process;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import com.seefly.collector.domain.DoubanMovieDO;
import com.seefly.collector.tools.DbTool;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by copy202 on 15/8/9.
 */
public class lrtsBookCategoryCrawler extends DeepCrawler {

    private String crawlPath;

    public static final String sideUrl = "http://www.lrts.me";

    private static Map<String,String> catagoryUrlMap = new HashMap<String,String>();

    private String catagoryName = new String();

    static {
        catagoryUrlMap.put("http://www.lrts.me/book/category/1/latest", "有声小说");
        catagoryUrlMap.put("http://www.lrts.me/book/category/78/latest","文学名著");
        catagoryUrlMap.put("http://www.lrts.me/book/category/4","曲艺戏曲");
        catagoryUrlMap.put("http://www.lrts.me/book/category/3","相声评书");
        catagoryUrlMap.put("http://www.lrts.me/book/category/6","少儿天地");
        catagoryUrlMap.put("http://www.lrts.me/book/category/7","外语学习");
        catagoryUrlMap.put("http://www.lrts.me/book/category/54","娱乐综艺");
        catagoryUrlMap.put("http://www.lrts.me/book/category/80","人文社科");
        catagoryUrlMap.put("http://www.lrts.me/book/category/1015","时事热点");
        catagoryUrlMap.put("http://www.lrts.me/book/category/3085","商业财经");
        catagoryUrlMap.put("http://www.lrts.me/book/category/55","纯乐梵音");
        catagoryUrlMap.put("http://www.lrts.me/book/category/3086","健康养生");
        catagoryUrlMap.put("http://www.lrts.me/book/category/1019","时尚生活");
        catagoryUrlMap.put("http://www.lrts.me/book/category/57","广播剧");
        catagoryUrlMap.put("http://www.lrts.me/book/category/79","职业技能");
        catagoryUrlMap.put("http://www.lrts.me/book/category/104","静雅思听");
    }

    public lrtsBookCategoryCrawler(String crawlPath) {
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
        if (nextLink.size()==0){
            if(catagoryUrlMap.size()>0){
                String catagoryUrl = "";
                for (String key : catagoryUrlMap.keySet()) {
                    catagoryUrl = key;
                    catagoryName = catagoryUrlMap.get(key);
                    break;
                }
                if(StringUtils.isNotBlank(catagoryUrl)){
                    catagoryUrlMap.remove(catagoryUrl);
                    nextLink.add(catagoryUrl);
                }
            }
        }
        return nextLink;
    }

    private Links parseHtml(String content) {
        Links links  = new Links();
        try {
            if (StringUtils.isNotBlank(content)) {
                Document doc = Jsoup.parse(content);

                Elements bookitems = doc.getElementsByClass("book-item");
                if (bookitems != null && bookitems.size() > 0) {
                    for (Element bookitem : bookitems) {
                        try {
                            String cover = bookitem.getElementsByClass("book-item-photo").get(0).getElementsByTag("img").get(0).attr("src");
                            Element book_item = bookitem.getElementsByClass("book-item-r").get(0);
                            String bookName = book_item.getElementsByTag("a").get(0).text();
                            String bookHref = book_item.getElementsByTag("a").get(0).attr("href");
                            String bookId = bookHref.substring(bookHref.lastIndexOf("/")+1);
                            String author = book_item.getElementsByClass("author").get(0).text();
                            String weaken = book_item.getElementsByClass("g-user").get(0).text();
                            int total = book_item.getElementsByClass("icon-star-s").size();
                            int disable = 0;
                            for (Element ele : book_item.getElementsByClass("icon-star-s")) {
                                if (ele.hasClass("disable")) {
                                    disable++;
                                }
                            }
                            int star = total - disable;
                            Element book_item_desc = bookitem.getElementsByClass("book-item-desc").get(0);
                            String desc = book_item_desc.text();


                            String selectSQL = "select count(0) as count from tb_books_lrts where title='"+bookName+"' and tag='"+catagoryName+"'";
                            String insertSQL = "insert into tb_books_lrts(id,cover,title,info,author,announcer,tag,crt,udt,create_time,book_id) values ('"+ UUID.randomUUID()+"','"+cover+"','"+bookName+"','"+desc+"','"+author+"','"+weaken+"','"+catagoryName+"',now(),now(),now(),'"+bookId+"')";
                            int count = DbTool.queryCount(selectSQL);
                            if(count==0){
                                DbTool.execute(insertSQL);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                Elements pagination = doc.getElementsByClass("pagination");
                if (pagination != null && pagination.size() > 0) {
                    Elements nexts = pagination.get(0).getElementsByClass("next");
                    if (nexts != null && nexts.size() > 0) {
                        String href = nexts.get(0).attr("href");
                        links.add(sideUrl + href);
                        System.out.println(sideUrl + href);
                        return links;
                    }
                }
            }
        }catch (Exception e){

        }
        return links;
    }
}
