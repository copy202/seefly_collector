package com.seefly.collector.process;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequester;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.RequestConfig;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by copy202 on 15/8/9.
 */
public class EdsCrawler extends DeepCrawler {

    private String crawlPath;

    public EdsCrawler(String crawlPath) {
        super(crawlPath);
        this.crawlPath = crawlPath;
        HttpRequesterImpl requester=(HttpRequesterImpl) this.getHttpRequester();
        RequestConfig rc = requester.getRequestConfig();
        rc.setMethod("POST");
        //rc.setHeader("Accept-Encoding", "gzip, deflate");
        //rc.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        //rc.setHeader("Cache-Control", "max-age=0");
        //rc.setHeader("Connection", "keep-alive");
        //rc.setHeader("Content-Type", "application/x-www-form-urlencoded");
        //rc.setHeader("Origin", "http://eds.newtouch.cn");
        //rc.setHeader("Host", "eds.newtouch.cn");
        //rc.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
        //rc.setHeader("Referer","http://eds.newtouch.cn/");
        //rc.setHeader("UserId","14493fsdf");
        //rc.setHeader("UserPsd", "1qaz2wsxxsdf");



        //requester.setHeader("Accept-Encoding", "gzip, deflate");
        //requester.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        //requester.setHeader("Cache-Control", "max-age=0");
        //requester.setHeader("Connection", "keep-alive");
        //requester.setHeader("Content-Type", "application/x-www-form-urlencoded");
        //requester.setHeader("Origin", "http://eds.newtouch.cn");
        //requester.setHeader("Host", "eds.newtouch.cn");
        //requester.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
        //requester.setCookie("bid=\"kurlmstJI8o\"; ll=\"108309\"; ap=1; _pk_ref.100001.8cb4=%5B%22%22%2C%22%22%2C1439121430%2C%22http%3A%2F%2Fmovie.douban.com%2Ftag%2F%22%5D; _pk_id.100001.8cb4=c07960c45819019e.1403859829.8.1439121430.1439111681.; _pk_ses.100001.8cb4=*; __utmt=1; __utma=30149280.2001245108.1375193550.1439111224.1439121431.12; __utmb=30149280.2.9.1439121431; __utmc=30149280; __utmz=30149280.1439111224.11.6.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/tag/%E7%88%B1%E6%83%85/");
        //requester.setHeader("Referer","http://eds.newtouch.cn/");
        //requester.setHeader("UserId","14493");
        //requester.setHeader("UserPsd", "1qaz2wsx");
        //requester.setMethod("POST");

/*
        URL url = null;
        try {
            url = new URL("http://eds.newtouch.cn/eds36web/DefaultLogin.aspx?lan=zh-cn");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

    }


    @Override
    public Links visitAndGetNextLinks(Page page) {

        System.out.println(page.getHtml());
        //String jsonStr = page.getHtml();
        //System.out.println(jsonStr);
        //parseHtml(jsonStr);
        return null;
    }

    private void parseHtml(String content) {
        if(StringUtils.isNotBlank(content)){
//            Document doc = Jsoup.parse(content);
//            for (Element dlEle : doc.getElementsByTag("dl")) {
//                Element img = dlEle.getElementsByTag("img").get(0);
//                String imageUrl = img.attr("src");
//                Element titleEle = dlEle.getElementsByClass("title").get(0);
//                String title = titleEle.text();
//                String url = titleEle.attr("href");
//                String desc = dlEle.getElementsByClass("desc").get(0).text();
//                float rate = 0;
//                try {
//                    rate = Float.valueOf(dlEle.getElementsByClass("rating_nums").get(0).text());
//                }catch (Exception e){
//
//                }
//
//                String left = url.substring(0, url.lastIndexOf("/?from=tag"));
//                String subjectId = left.substring(left.lastIndexOf("/")+1);
//                DoubanMovieDO dm = new DoubanMovieDO();
//                dm.setDetailUrl(url);
//                dm.setImageUrl(imageUrl);
//                dm.setRating_nums(rate);
//                dm.setDesc(desc);
//                dm.setTitle(title);
//                dm.setSubjectId(subjectId);
//                //System.out.println(dm);
//                try {
//                    FileOutputStream fos=new FileOutputStream(new File("/tmp/woXXX"),true);
//                    fos.write(dm.toString().getBytes());
//                    fos.close();
//                }catch (Exception e){
//
//                }finally {
//
//                }
//            }
        }
    }
}
