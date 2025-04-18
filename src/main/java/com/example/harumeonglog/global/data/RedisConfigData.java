package com.example.harumeonglog.global.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfigData {
    private String host;
    private int port;

    public static final String BLACKLIST_PREFIX = "BLACK:";
    public static final String REFRESH_TOKEN_PREFIX = "REFRESH:";
}
