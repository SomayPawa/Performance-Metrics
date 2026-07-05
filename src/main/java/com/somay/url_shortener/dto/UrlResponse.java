package com.somay.url_shortener.dto;

public class UrlResponse {
    private String shortUrl;

    public UrlResponse(String shortCode){
        this.shortUrl = shortCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
