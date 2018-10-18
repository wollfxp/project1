package com.dataart.project1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceHandlerConfiguration implements WebMvcConfigurer {

    @Value("${resources.cache.period.override}")
    private final Integer cacheDuration = 600;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/*.*")
                .addResourceLocations("classpath:/static/images/")
                .setCachePeriod(cacheDuration); // this fixes spring security + static resources caching issue

        registry
                .addResourceHandler("/webfonts/*.*")
                .addResourceLocations("classpath:/static/webfonts/")
                .setCachePeriod(cacheDuration); // cache font-awesome
    }

}
