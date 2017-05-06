package com.example.k.newsgateway;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by K on 4/30/2017.
 */

public class Article implements Serializable, Parcelable {

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

    public int describeContents(){
        return hashCode();
    }

    public static final Parcelable.Creator<Article> CREATOR
            = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    private Article(Parcel in){

        author=in.readString();
        title=in.readString();
        description=in.readString();
        url=in.readString();
        urlToImage=in.readString();
        source=in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(urlToImage);
        dest.writeString(source);
    }
}
