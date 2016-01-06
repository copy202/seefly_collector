package com.seefly.collector.process;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import com.seefly.collector.tools.DbTool;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Created by copy202 on 15/8/27.
 */
public class lrtsBookSectionCrawler extends DeepCrawler {

    private String crawlPath;
    private int count;
    private int page;
    private int currentIndex;
    private String bookId;
    public lrtsBookSectionCrawler(String crawlPath,String bookId,String currentIndex){
        super(crawlPath);
        this.crawlPath = crawlPath;
        this.count = 0;
        this.page = 0;
        this.currentIndex=0;
        this.bookId = bookId;
        addSeed("http://www.lrts.me/ajax/playlist/2/"+bookId+"/"+currentIndex);
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
                if(count == 0){
                    count = Integer.valueOf(doc.getElementsByClass("detail").get(0).getElementsByTag("span").get(1).text());
                    page = count/10;
                }

                if(currentIndex<count){
                    currentIndex = currentIndex + 10;
                    links.add("http://www.lrts.me/ajax/playlist/2/"+bookId+"/"+currentIndex);
                }

                Elements elements = doc.getElementsByClass("section");
                if(elements!=null && elements.size()>0){
                    Elements sectionlist = elements.get(0).getElementsByTag("li");
                    for (Element element: sectionlist){
                        String url = element.getElementsByAttributeValue("name","source").attr("value");
                        String sectionName = element.getElementsByAttributeValue("name","player-r-name").attr("value");
                        String number = element.getElementsByAttributeValue("name","number").attr("value");
                        String selectSQL = "select count(0) as count from tb_audio_lrts where book_id='"+bookId+"' and number="+number;
                        int ct = DbTool.queryCount(selectSQL);
                        if(ct==0){
                            String insertSQL = "insert into tb_audio_lrts(book_id,name,url,number) values ('"+bookId+"','"+sectionName+"','"+url+"',"+number+")";
                            DbTool.execute(insertSQL);
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return links;
    }
}
