package com.one_id_nepal.resource_server.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class FileUrlUtil {

    private static String baseUrl;

    @Value("${server.port}")
    private int serverPort;

    @Value("${server.base-url:}")
    private String applicationBaseUrl;

    @PostConstruct
    public void init() {

        if (applicationBaseUrl == null || applicationBaseUrl.isBlank()) {
            baseUrl = "http://localhost:" + serverPort;
        } else {
            baseUrl = applicationBaseUrl.endsWith("/")
                    ? applicationBaseUrl.substring(0, applicationBaseUrl.length() - 1)
                    : applicationBaseUrl;
        }

        log.info("Application Base URL : {}", baseUrl);
    }

    public static URI getFileUri(String relativePath) {

        if (relativePath == null || relativePath.isBlank()) {
            return null;
        }

        String cleanPath = relativePath
                .replace("\\", "/")
                .replaceFirst("^/+", "");

        String encodedPath = URLEncoder.encode(cleanPath, StandardCharsets.UTF_8)
                .replace("%2F", "/");

        return URI.create(baseUrl + "/uploads/" + encodedPath);
    }
}