package com.somay.url_shortener.service;

import com.somay.url_shortener.Repository.UrlRepository;
import com.somay.url_shortener.entity.ShortUrl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public UrlService(UrlRepository urlRepository, StringRedisTemplate stringRedisTemplate) {
        this.urlRepository = urlRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String createShortUrl(String originalUrl){
        Optional<String> existingCode = urlRepository.findByURL(originalUrl);
        if(existingCode.isPresent()){
            return existingCode.get();
        }
        String shortCode = generateCode();
        ShortUrl shortUrl = new ShortUrl();

        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setShortCode(shortCode);
        shortUrl.setClickCount(0L);

        urlRepository.save(shortUrl);

        return shortCode;
    }

    private String generateCode(){
        return UUID.randomUUID()
                .toString()
                .replace("-","")
                .substring(0,8);
    }

    public String getOriginalUrl(String shortCode){
        String redisKey = "url:"+shortCode;

        // check redis

        String cacheUrl = stringRedisTemplate.opsForValue().get(redisKey);
        if(cacheUrl != null){
            System.out.println("Cache Hit");
            urlRepository.incrementClickCount(shortCode);
            return cacheUrl;
        }

        // Mysql Check

        System.out.println("Cache Miss");

        ShortUrl shortUrl = urlRepository.findByCd(shortCode)
                .orElseThrow(()->new RuntimeException("URL not found"));

        stringRedisTemplate.opsForValue().set(
                redisKey,shortUrl.getOriginalUrl(), Duration.ofHours(1)
        );

        // Count update Code

        // urlRepository.incrementClickCount(shortCode);

        stringRedisTemplate.opsForValue().increment("click: "+shortCode);
        return shortUrl.getOriginalUrl();
    }
}
