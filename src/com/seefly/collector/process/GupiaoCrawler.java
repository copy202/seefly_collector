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

import java.util.List;
import java.util.UUID;

/**
 * Created by copy202 on 15/10/16.
 */
public class GupiaoCrawler extends DeepCrawler {
    private String crawlPath;
    private String type;
    private List<String> ids;
    public static final String sideUrl ="http://vip.stock.finance.sina.com.cn/corp/go.php/vISSUE_NewStock/stockid/";
    public static String currentId;
    public GupiaoCrawler(String crawlPath){
        super(crawlPath);
        this.crawlPath = crawlPath;
    }

    public GupiaoCrawler(String crawlPath,List<String> ids,String type){
        super(crawlPath);
        this.crawlPath = crawlPath;
        this.type = type;
        this.ids = ids;
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
                if(type==null) {
                    Elements elements = doc.getElementsByClass("bbsilst_wei3");
                    int count = 1;
                    if (elements != null && elements.size() > 0) {
                        for (Element element : elements) {
                            if (count > 2) {
                                continue;
                            }
                            Elements liList = element.getElementsByTag("ul").get(0).getElementsByTag("a");
                            System.out.println("ulList size=" + liList.size());
                            for (Element ul : liList) {
                                int lastspace = ul.text().lastIndexOf(" ");
                                String name = ul.text().substring(0, lastspace);
                                String code = ul.text().substring(lastspace + 1);
                                String[] ulsplit = ul.text().split(" ");
                                //if(ulsplit.length!=2){
                                //    System.out.println(ul+",name="+name+",code="+code);
                                //}
                                String insertSQL = "insert into gpinfo(id,gpcode,gpname) values ('" + UUID.randomUUID() + "','" + code + "','" + name + "')";
                                DbTool.execute(insertSQL);
                            }
                            count++;
                        }
                    }
                }else if(type.equalsIgnoreCase("getstartdate")){
                    if(ids.size()>0){
                        String id = ids.remove(0);
                        links.add(sideUrl+id+".phtml");
                        Element table = doc.getElementById("comInfo1");
                        if(table!=null && table.hasText()){
                            Elements trs = table.getElementsByTag("tr");
                            for(Element tr:trs){
                                if(tr.getElementsByTag("strong").get(0).text().equalsIgnoreCase("上市日期")){
                                    String startdate = tr.getElementsByTag("a").get(0).text();
                                    String sql = "update gpinfo set startdate='"+startdate+"' where gpcode='"+currentId+"'";
                                    DbTool.execute(sql);
                                }
                            }
                        }
                        currentId = id;
                    }
                }
            }
        }catch (Exception e){

        }
        return links;
    }

}
