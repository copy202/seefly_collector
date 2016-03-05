package com.seefly.collector.tools;

import sun.misc.BASE64Encoder;

/**
 * Created by copy202 on 16/3/5.
 */
public class UrlUtil {
    public final static String convertToThunderUrl(String url){
        String src = "AA" + url +"ZZ";
        BASE64Encoder encoder = new BASE64Encoder();
        src = encoder.encode(src.getBytes());

        return "thunder://"+src;
    }
}
