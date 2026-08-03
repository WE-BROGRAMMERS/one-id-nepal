package com.one_id_nepal.resource_server.config.file;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String filePath;

    public WebConfig(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String location = "file:";

        if (!filePath.endsWith("/") && !filePath.endsWith("\\")) {
            location += filePath + "/";
        } else {
            location += filePath;
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location)
                .setCachePeriod(0);

        System.out.println("Serving files from : " + location);
    }
}