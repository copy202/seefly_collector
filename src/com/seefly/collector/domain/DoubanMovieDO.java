package com.seefly.collector.domain;

/**
 * Created by copy202 on 15/8/9.
 */
public class DoubanMovieDO extends MovieBaseDO {
    private String detailUrl;

    private String imageUrl;

    private float rating_nums;

    private String subjectId;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public float getRating_nums() {
        return rating_nums;
    }

    public void setRating_nums(float rating_nums) {
        this.rating_nums = rating_nums;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    @Override
    public String toString() {
        return "DoubanMovieDO{" +
                "detailUrl='" + detailUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", rating_nums=" + rating_nums +
                ", subjectId='" + subjectId + '\'' +
                "} " + super.toString();
    }
}
