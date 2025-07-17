package com.example.url_shortener.controller;

import com.example.url_shortener.model.Url;
import com.example.url_shortener.model.ShortenUrlResponse;
import com.example.url_shortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UrlController {
    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody Url request, HttpServletRequest httpServletRequest) {
        String shortCode = urlService.shortenUrl(request.getOriginalUrl());
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
                .replacePath(null)
                .build()
                .toUriString();
        String shortUrl = baseUrl + "/" + shortCode;
        return ResponseEntity.ok(new ShortenUrlResponse(shortUrl));
    }

    public UrlService getUrlService() {
        return urlService;
    }

    @GetMapping("/{shortCode}")
    public RedirectView redirectToOriginalUrl(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode);
        if (originalUrl != null) {
            return new RedirectView(originalUrl);
        } else {
            // Handle not found case - redirect to a default page or show error
            return new RedirectView("/error");
        }
    }
}