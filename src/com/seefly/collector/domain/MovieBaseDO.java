package com.seefly.collector.domain;

/**
 * Created by copy202 on 15/8/9.
 */
public class MovieBaseDO {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String title;
    private String desc;

    @Override
    public String toString() {
        return "MovieBaseDO{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
