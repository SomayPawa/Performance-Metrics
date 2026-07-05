package com.somay.url_shortener.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "short_url",
        indexes = {
                @Index(
                        name = "idx_short_code",
                        columnList = "short_code"
                )
        }

)
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalUrl;

    @Column(unique = true)
    private String shortCode;

    private Long clickCount = 0L;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
