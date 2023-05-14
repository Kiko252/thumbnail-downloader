package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class VideoLink {

   private String url ="https://pickywallpapers.com/img/2018/10/youtube-red-desktop-wallpaper-526-534-hd-wallpapers.jpg";

    private String id;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "URL [url="+ url +"]";
    }

}

