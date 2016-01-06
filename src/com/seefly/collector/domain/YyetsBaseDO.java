package com.seefly.collector.domain;

/**
 * Created by copy202 on 15/12/25.
 */
public class YyetsBaseDO {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZhuyan() {
        return zhuyan;
    }

    public void setZhuyan(String zhuyan) {
        this.zhuyan = zhuyan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLabel_uptime() {
        return label_uptime;
    }

    public void setLabel_uptime(String label_uptime) {
        this.label_uptime = label_uptime;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCrt_time() {
        return crt_time;
    }

    public void setCrt_time(String crt_time) {
        this.crt_time = crt_time;
    }

    public String getUpt_time() {
        return upt_time;
    }

    public void setUpt_time(String upt_time) {
        this.upt_time = upt_time;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    private String id="";

    private String name="";

    private String zhuyan="";

    private String status="";

    private String category="";

    private String area="";

    private String times="";

    private String type="";

    private String language="";

    private String label_uptime="";

    private String director="";

    private String crt_time="";

    private String upt_time="";

    private String href = "";

    private String isUpdate = "";


    @Override
    public String toString() {
        return "YyetsBaseDO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", zhuyan='" + zhuyan + '\'' +
                ", status='" + status + '\'' +
                ", category='" + category + '\'' +
                ", area='" + area + '\'' +
                ", times='" + times + '\'' +
                ", type='" + type + '\'' +
                ", language='" + language + '\'' +
                ", label_uptime='" + label_uptime + '\'' +
                ", director='" + director + '\'' +
                ", crt_time='" + crt_time + '\'' +
                ", upt_time='" + upt_time + '\'' +
                ", href='" + href + '\'' +
                ", isUpdate='" + isUpdate + '\'' +
                '}';
    }
}
