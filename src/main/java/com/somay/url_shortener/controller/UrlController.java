package com.somay.url_shortener.controller;

import com.somay.url_shortener.dto.UrlRequest;
import com.somay.url_shortener.dto.UrlResponse;
import com.somay.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public UrlResponse shorten(@Valid @RequestBody UrlRequest request){
        String code = urlService.createShortUrl(request.getUrl());
        return new UrlResponse(
                "http://localhost:8081/api/"+code
        );
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        System.out.println("Received : "+shortCode);
        String originalUrl = urlService.getOriginalUrl(shortCode);
        System.out.println("Original URL "+originalUrl);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @PostMapping("/generate")
    public String generateData(){
        List<Map<String,String>> result = new ArrayList<>();
        for(int i=0;i<1000000;i++){
            String originalUrl = "https://slpdev.netlify.app/page/"+i;
            String shortCode = urlService.createShortUrl(originalUrl);
            Map<String,String> data = new HashMap<>();
            data.put("originalUrl",originalUrl);
            data.put("shortCode",shortCode);
            result.add(data);
        }

//        for(int i=9990;i<10000;i++){
//            String originalUrl = "https://slpdev.netlify.app/page/"+i;
//            String shortCode = urlService.createShortUrl(originalUrl);
//            Map<String,String> data = new HashMap<>();
//            data.put("originalUrl",originalUrl);
//            data.put("shortCode",shortCode);
//            result.add(data);
//        }
        return "done";
    }
}
