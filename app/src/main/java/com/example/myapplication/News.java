package com.example.myapplication;

public class News {

    private String city;
    private String wendu;
    private String shidu;
    private String quality;
    private String notice;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public News(String city, String wendu, String shidu, String quality, String notice) {
        super();
        this.city = city;
        this.wendu = wendu;
        this.shidu = shidu;
        this.quality = quality;
        this.notice = notice;
    }

    public News() {
        super();
    }

}
