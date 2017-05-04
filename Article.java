package com.example.k.newsgateway;

/**
 * Created by K on 4/30/2017.
 */

public class Article {

    public String author;

    public String getAuthor() {
        return author;
    }

    public String publishedAt;

    public Article(String author, String title, String description, String url, String urlToImage, String source, String publishedAt) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.source = source;
        this.publishedAt=publishedAt;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String source;


}
