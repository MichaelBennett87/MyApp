package com.kenzie.appserver.config;

import com.kenzie.capstone.service.client.ReviewLambdaServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaServiceClientConfiguration {

    @Bean
    public ReviewLambdaServiceClient referralServiceClient() {
        return new ReviewLambdaServiceClient();
    }
}
