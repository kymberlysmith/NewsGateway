package com.example.k.newsgateway;

/**
 * Created by K on 4/30/2017.
 */

public class Source {

    public String iD;
    public String name;
    public String url;
    public String category;

    public Source(String iD, String name, String url, String category) {
        this.iD = iD;
        this.name = name;
        this.url = url;
        this.category = category;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

}
