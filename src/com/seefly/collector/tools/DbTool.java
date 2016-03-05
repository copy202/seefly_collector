package com.seefly.collector.tools;

import com.seefly.collector.domain.YyetsBaseDO;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by copy202 on 15/8/27.
 */
public class DbTool {
    private static String conn_url;
    private static String conn_driver;
    private static String conn_user;
    private static String conn_password;
    private static DataSource dataSource;

    static {
        Properties prop = new Properties();

        String dbconf = "/Users/copy202/workspace/selfwork/seefly_collector/conf/db.properties";
        System.out.println(dbconf);
        try {
            prop.load(new FileInputStream(dbconf));
            //prop.load(new FileInputStream("conf/db.properties"));
            conn_url = prop.getProperty("url");
            conn_driver = prop.getProperty("driver");
            conn_user = prop.getProperty("user");
            conn_password = prop.getProperty("password");

            initDataSource();
        } catch (IOException e) {
            System.err.println("load db config file error");
        }
    }

    private static void initDataSource(){
        PoolProperties p = new PoolProperties();
        p.setUrl(conn_url);
        p.setDriverClassName(conn_driver);
        p.setUsername(conn_user);
        p.setPassword(conn_password);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setMaxIdle(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        dataSource = new DataSource();
        dataSource.setPoolProperties(p);
    }

    public static boolean execute(String sql){

        System.out.println("execute sql <"+sql + ">");

        boolean result = true;
        Connection conn  = null;
        Statement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            st.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }finally {
            if (st!=null){
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    result = false;
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    result = false;
                }
            }
        }
        return result;
    }


    public static int queryCount(String sql){

        Connection conn  = null;
        Statement st = null;
        int count=0;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()){
                count = rs.getInt("count");
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (st!=null){
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }

    public static List<YyetsBaseDO> getAllyyetsNeedUpdate(){
        List<YyetsBaseDO> list = new ArrayList<YyetsBaseDO>();

        Connection conn = null;
        Statement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from yyets_lianxuju where isupdate!='1'");
            while(rs.next()){
                YyetsBaseDO yyetsBaseDO = new YyetsBaseDO();
                yyetsBaseDO.setId(rs.getString("id"));
                yyetsBaseDO.setArea(rs.getString("area"));
                yyetsBaseDO.setCategory(rs.getString("category"));
                yyetsBaseDO.setCrt_time(rs.getString("crt_time"));
                yyetsBaseDO.setDirector(rs.getString("director"));
                yyetsBaseDO.setLabel_uptime(rs.getString("label_uptime"));
                yyetsBaseDO.setLanguage(rs.getString("language"));
                yyetsBaseDO.setName(rs.getString("name"));
                yyetsBaseDO.setStatus(rs.getString("status"));
                yyetsBaseDO.setTimes(rs.getString("times"));
                yyetsBaseDO.setType(rs.getString("type"));
                yyetsBaseDO.setUpt_time(rs.getString("upt_time"));
                yyetsBaseDO.setZhuyan(rs.getString("zhuyan"));
                yyetsBaseDO.setHref(rs.getString("href"));
                yyetsBaseDO.setIsUpdate(rs.getString("isupdate"));

                list.add(yyetsBaseDO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (st!=null){
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    public static List<YyetsBaseDO> getAllyyets(){
        List<YyetsBaseDO> list = new ArrayList<YyetsBaseDO>();

        Connection conn = null;
        Statement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from yyets_lianxuju");
            while(rs.next()){
                YyetsBaseDO yyetsBaseDO = new YyetsBaseDO();
                yyetsBaseDO.setId(rs.getString("id"));
                yyetsBaseDO.setArea(rs.getString("area"));
                yyetsBaseDO.setCategory(rs.getString("category"));
                yyetsBaseDO.setCrt_time(rs.getString("crt_time"));
                yyetsBaseDO.setDirector(rs.getString("director"));
                yyetsBaseDO.setLabel_uptime(rs.getString("label_uptime"));
                yyetsBaseDO.setLanguage(rs.getString("language"));
                yyetsBaseDO.setName(rs.getString("name"));
                yyetsBaseDO.setStatus(rs.getString("status"));
                yyetsBaseDO.setTimes(rs.getString("times"));
                yyetsBaseDO.setType(rs.getString("type"));
                yyetsBaseDO.setUpt_time(rs.getString("upt_time"));
                yyetsBaseDO.setZhuyan(rs.getString("zhuyan"));
                yyetsBaseDO.setHref(rs.getString("href"));
                yyetsBaseDO.setIsUpdate(rs.getString("isupdate"));

                list.add(yyetsBaseDO);
            }
        }catch (Exception e){
                e.printStackTrace();
        }finally {
            if (st!=null){
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }


    public static void updateYyets(YyetsBaseDO yyetsBaseDO){


        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement("update yyets_lianxuju set area=?,category=?,crt_time=?,director=?,label_uptime=?,language=?,name=?,status=?,times=?,type=?,upt_time=?,zhuyan=?,isupdate=? where id=?");

            st.setString(1,yyetsBaseDO.getArea());
            st.setString(2,yyetsBaseDO.getCategory());
            st.setString(3,yyetsBaseDO.getCrt_time());
            st.setString(4,yyetsBaseDO.getDirector());
            st.setString(5,yyetsBaseDO.getLabel_uptime());
            st.setString(6,yyetsBaseDO.getLanguage());
            st.setString(7,yyetsBaseDO.getName());
            st.setString(8,yyetsBaseDO.getStatus());
            st.setString(9,yyetsBaseDO.getTimes());
            st.setString(10,yyetsBaseDO.getType());
            st.setString(11,yyetsBaseDO.getUpt_time());
            st.setString(12,yyetsBaseDO.getZhuyan());
            st.setString(13,yyetsBaseDO.getIsUpdate());
            st.setString(14,yyetsBaseDO.getId());

            st.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (st!=null){
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static List<String> queryBookId(String sql){
        List<String> bookIdList = new ArrayList<String>();
        Connection conn  = null;
        Statement st = null;

        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()){
                bookIdList.add(rs.getString("book_id"));
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (st!=null){
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookIdList;
    }


    public static List<String> queryIds(String sql,String idfield){
        List<String> resultIdList = new ArrayList<String>();
        Connection conn  = null;
        Statement st = null;

        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()){
                resultIdList.add(rs.getString(idfield));
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (st!=null){
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultIdList;
    }

    public static void main(String[] args) {
        String testInsert = "insert into tb_books_lrts(cover,title,info,author,announcer,recommend) values ('xx','xx','xx','xx','xx',2)";
        DbTool.execute(testInsert);
    }
}
