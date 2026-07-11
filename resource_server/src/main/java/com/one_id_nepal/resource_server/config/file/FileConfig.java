package com.one_id_nepal.resource_server.config.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {

    @Value("${app.filepath.windows}")
    private String windows;

    @Value("${app.filepath.linux}")
    private String linux;

    @Value("${app.filepath.mac}")
    private String mac;

    @Bean
    public String getFilePath() {

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return windows;
        }

        if (os.contains("mac")) {
            return mac;
        }

        return linux;
    }
}