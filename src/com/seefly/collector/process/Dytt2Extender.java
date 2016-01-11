package com.seefly.collector.process;

import com.seefly.collector.tools.DbTool;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import  java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 * Created by copy202 on 15/12/28.
 */
public class Dytt2Extender {


    public static void execute(List<String> seeds,String subtype){

        List<String> executeSqlList = new ArrayList<String>();

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String exportfilename = dateFormat.format(now)+".sql";

        if(seeds==null || seeds.size()==0){
            return;
        }


        HttpClient httpclient = new DefaultHttpClient();
        HttpClientParams.setCookiePolicy(httpclient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.getParams().setParameter("http.protocol.content-charset", HTTP.ISO_8859_1);

        for(String seed:seeds){

            seed = "http://www.dytt.com/xiazai/id"+seed+".html";
            try{
                HttpGet httpgets = new HttpGet(seed);

                httpgets.addHeader(new BasicHeader("Cookie","ASPSESSIONIDQCQSAADD=OCLNNFOALEHEPHOIIONEDNDL; safedog-flow-item=33A90FABFD56C7518AF22C30108A699A"));
                //httpgets.addHeader(new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
                //httpgets.addHeader(new BasicHeader("Accept-Encoding","gzip, deflate, sdch"));
                //httpgets.addHeader(new BasicHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6"));
                //httpgets.addHeader(new BasicHeader("Cache-Control","max-age=0"));
                //httpgets.addHeader(new BasicHeader("Connection","keep-alive"));
                //httpgets.addHeader(new BasicHeader("Host","www.dytt.com"));
                httpgets.addHeader(new BasicHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36"));

                HttpResponse response = httpclient.execute(httpgets);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //String str = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset().toString());
                    //InputStream instreams = entity.getContent();
                    //String str = convertStreamToString(instreams);
                    //System.out.println();

                    String content = EntityUtils.toString(entity, "GBK");
                    httpgets.abort();
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

                    //DbTool.execute("delete from tb_film_dytt_thunder where film_dytt_id='"+sid+"'");
                    //System.err.println("delete from tb_film_dytt_thunder where film_dytt_id='"+sid+"';");
                    //FileUtils.writeStringToFile(new File("/Users/copy202/Desktop/2.sql"), "delete from tb_film_dytt_thunder where film_dytt_id='"+sid+"';\n",true);

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
                                    //System.err.println(sql+";");
                                    FileUtils.writeStringToFile(new File("/Users/copy202/Desktop/"+exportfilename), sql+";\n",true);
                                    executeSqlList.add(sql);
                                    //DbTool.execute(sql);
                                }
                            }
                        }
                    }


                    int has = DbTool.queryCount("select count(0) as count from tb_film_dytt where sid='" + sid + "'");
                    if(has>0){
                        //更新
                        String updateSQL = "update tb_film_dytt set title='%s',status='%s',update_time='%s',update_cycle='%s',summary='%s' where sid='"+sid+"'";
                        updateSQL = String.format(updateSQL,title,status,update_time,update_cycle,summary);
                        //boolean result = DbTool.execute(updateSQL);
                        FileUtils.writeStringToFile(new File("/Users/copy202/Desktop/"+exportfilename), updateSQL+";\n",true);
                        executeSqlList.add(updateSQL);
                        //System.err.println(updateSQL+";");
                    }else{
                        //新增
                        String sql = "insert into tb_film_dytt (sid,title,img,film_type,status,rating,casts,countries,summary,subtype,show_time,update_time,update_cycle,url) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
                        sql = String.format(sql,sid,title,img,film_type,status,rating,casts,countries,summary,subtype,showtime,update_time,update_cycle,seed);
                        //boolean result = DbTool.execute(sql);
                        //System.err.println(sql+";");
                        FileUtils.writeStringToFile(new File("/Users/copy202/Desktop/"+exportfilename), sql + ";\n",true);
                        executeSqlList.add(sql);
                        /*
                        if(false==false){
                            String sql2 = "insert into tb_film_dytt (sid,title,img,film_type,status,rating,casts,countries,summary,subtype,show_time,update_time,update_cycle) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
                            sql2 = String.format(sql2,sid,title,img,film_type,status,rating,casts,countries,"",subtype,showtime,update_time,"");
                            DbTool.execute(sql2);
                        }
                        */
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        for(String exesql:executeSqlList){
            DbTool.execute(exesql);
        }
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
