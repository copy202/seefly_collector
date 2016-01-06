package com.seefly.collector.process;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;
import com.seefly.collector.domain.DoubanMovieDO;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by copy202 on 15/8/9.
 */
public class MoviceJsonCrawler extends DeepCrawler {

    private int total;

    private String crawlPath;

    public MoviceJsonCrawler(String crawlPath) {
        super(crawlPath);
        this.crawlPath = crawlPath;
        HttpRequesterImpl requester=(HttpRequesterImpl) this.getHttpRequester();

        requester.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        requester.setHeader("Accept-Encoding", "gzip, deflate, sdch");
        requester.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        requester.setHeader("Cache-Control", "max-age=0");
        requester.setHeader("Connection", "keep-alive");
        requester.setHeader("Host", "www.douban.com");
        requester.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        requester.setCookie("bid=\"kurlmstJI8o\"; ll=\"108309\"; ap=1; _pk_ref.100001.8cb4=%5B%22%22%2C%22%22%2C1439121430%2C%22http%3A%2F%2Fmovie.douban.com%2Ftag%2F%22%5D; _pk_id.100001.8cb4=c07960c45819019e.1403859829.8.1439121430.1439111681.; _pk_ses.100001.8cb4=*; __utmt=1; __utma=30149280.2001245108.1375193550.1439111224.1439121431.12; __utmb=30149280.2.9.1439121431; __utmc=30149280; __utmz=30149280.1439111224.11.6.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/tag/%E7%88%B1%E6%83%85/");
        requester.setMethod("GET");
    }

    @Override
    public Links visitAndGetNextLinks(Page page) {
        String jsonStr = page.getHtml();
        System.out.println(jsonStr);
        JSONObject json = new JSONObject(jsonStr);
        this.total = json.getInt("total");
        String html = json.getString("html");
        parseHtml(html);
        return null;
    }

    private void parseHtml(String content) {
        if(StringUtils.isNotBlank(content)){
            Document doc = Jsoup.parse(content);
            for (Element dlEle : doc.getElementsByTag("dl")) {
                Element img = dlEle.getElementsByTag("img").get(0);
                String imageUrl = img.attr("src");
                Element titleEle = dlEle.getElementsByClass("title").get(0);
                String title = titleEle.text();
                String url = titleEle.attr("href");
                String desc = dlEle.getElementsByClass("desc").get(0).text();
                float rate = 0;
                try {
                    rate = Float.valueOf(dlEle.getElementsByClass("rating_nums").get(0).text());
                }catch (Exception e){

                }

                String left = url.substring(0, url.lastIndexOf("/?from=tag"));
                String subjectId = left.substring(left.lastIndexOf("/")+1);
                DoubanMovieDO dm = new DoubanMovieDO();
                dm.setDetailUrl(url);
                dm.setImageUrl(imageUrl);
                dm.setRating_nums(rate);
                dm.setDesc(desc);
                dm.setTitle(title);
                dm.setSubjectId(subjectId);
                //System.out.println(dm);
                try {
                    FileOutputStream fos=new FileOutputStream(new File("/tmp/woXXX"),true);
                    fos.write(dm.toString().getBytes());
                    fos.close();
                }catch (Exception e){

                }finally {

                }
            }
        }
    }

    public int getTotal(){
        return this.total;
    }
}
