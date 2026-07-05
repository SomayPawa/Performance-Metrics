package com.somay.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;

public class UrlRequest {
    @NotBlank(message = "URL cannot be blank")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
