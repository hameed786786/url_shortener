package com.example.url_shortener.service;

import com.example.url_shortener.model.Url;
import com.example.url_shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(String originalUrl) {
        // Generate a unique short code (using first 8 chars of UUID)
        String shortCode = UUID.randomUUID().toString().substring(0, 8);

        // Save to database
        Url url = new Url(originalUrl, shortCode);
        urlRepository.save(url);

        return shortCode;
    }

    public String getOriginalUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode);
        return (url != null) ? url.getOriginalUrl() : null;
    }
}