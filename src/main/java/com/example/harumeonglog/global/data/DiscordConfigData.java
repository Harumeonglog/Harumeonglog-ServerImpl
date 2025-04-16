package com.example.harumeonglog.global.data;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "discord")
public class DiscordConfigData {

    private String webHookUrl;
}
