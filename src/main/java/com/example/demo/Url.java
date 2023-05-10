package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class Url {

    String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "URL [url="+ url +"]";
    }

}

