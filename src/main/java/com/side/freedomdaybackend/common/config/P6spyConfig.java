package com.side.freedomdaybackend.common.config;

import com.p6spy.engine.spy.P6SpyOptions;
import com.side.freedomdaybackend.common.P6spyPrettySqlFormatter;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class P6spyConfig {
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spyPrettySqlFormatter.class.getName());
    }
}