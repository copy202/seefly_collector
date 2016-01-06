package com.seefly.collector.process;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import com.seefly.collector.domain.YyetsBaseDO;
import com.seefly.collector.tools.DbTool;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.UUID;

/**
 * Created by copy202 on 15/12/25.
 */
public class yyets3 extends DeepCrawler {

    private String crawlPath;

    public static final String sideUrl = "http://yyets.cc";

    public static List<YyetsBaseDO> yyetsList;

    private static YyetsBaseDO currentDO;

    static {
        yyetsList = DbTool.getAllyyetsNeedUpdate();
    }

    public yyets3(String crawlPath) {
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

        if(yyetsList!=null && yyetsList.size()>0){
            currentDO = yyetsList.remove(0);
            this.addSeed(sideUrl + currentDO.getHref().replace("//","/"));
        }

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
                Elements info = doc.getElementsByClass("info");

                Elements labelList = info.get(0).getElementsByTag("label");

                for (Element div:labelList){
                    String label = div.text();
                    int index = label.indexOf("：");
                    String key = label.substring(0, index)==null?"":label.substring(0, index);
                    String value = label.substring(index + 1)==null?"":label.substring(index + 1);
                    if(key.equalsIgnoreCase("状态")){
                        currentDO.setStatus(value);
                    }else if(key.equalsIgnoreCase("分类")){
                        currentDO.setCategory(value);
                    }else if(key.equalsIgnoreCase("地区")){
                        currentDO.setArea(value);
                    }else if(key.equalsIgnoreCase("年代")){
                        currentDO.setTimes(value);
                    }else if(key.equalsIgnoreCase("类型")){
                        currentDO.setType(value);
                    }else if(key.equalsIgnoreCase("语言")){
                        currentDO.setLanguage(value);
                    }else if(key.equalsIgnoreCase("更新时间")){
                        currentDO.setLabel_uptime(value);
                    }else if(key.equalsIgnoreCase("导演")){
                        currentDO.setDirector(value);
                    }
                }

                currentDO.setIsUpdate("1");

                DbTool.updateYyets(currentDO);

                String pro_id = currentDO.getId();

                //删除原有下载链接
                DbTool.execute("delete from yyets_url where pro_id='"+pro_id+"'");

                //新增下载链接

                Elements downurlList = doc.getElementsByClass("downurl");
                for (Element downurl : downurlList){
                    if(downurl.hasAttr("id")){
                        Elements liList = downurl.getElementsByTag("a");
                        String type = downurl.attr("id");

                        for(Element li:liList){
                            String href = li.attr("href");
                            String title = li.attr("title");

                            DbTool.execute("insert into yyets_url(id,pro_id,type,title,url) values ('"+UUID.randomUUID()+"','"+pro_id+"','"+type+"','"+title+"','"+href+"')");
                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (yyetsList.size() > 0) {
                currentDO = yyetsList.remove(0);
                System.out.println("当前size:" + yyetsList.size() + ";" + currentDO);
                links.add(sideUrl + currentDO.getHref().replace("//","/"));
                return links;
            } else {
                return null;
            }
        }
    }
}
