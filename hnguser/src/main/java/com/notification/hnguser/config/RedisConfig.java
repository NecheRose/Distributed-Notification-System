package com.notification.hnguser.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;
import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.username:}")
    private String redisUsername;

    @Value("${REDIS_URL:}")
    private String redisUrl;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();

        // Parse REDIS_URL if provided (Railway format: redis://default:password@host:port)
        if (redisUrl != null && !redisUrl.isEmpty() && redisUrl.startsWith("redis://")) {
            try {
                URI uri = URI.create(redisUrl);
                config.setHostName(uri.getHost());
                config.setPort(uri.getPort());
                
                // Parse password from userInfo (format: default:password or just password)
                String userInfo = uri.getUserInfo();
                if (userInfo != null && !userInfo.isEmpty()) {
                    String[] parts = userInfo.split(":");
                    if (parts.length >= 2) {
                        config.setUsername(parts[0]);
                        config.setPassword(parts[1]);
                    } else if (parts.length == 1) {
                        config.setPassword(parts[0]);
                    }
                }
            } catch (Exception e) {
                // Fallback to individual properties if URL parsing fails
                config.setHostName(redisHost);
                config.setPort(redisPort);
                if (redisPassword != null && !redisPassword.isEmpty()) {
                    config.setPassword(redisPassword);
                }
                if (redisUsername != null && !redisUsername.isEmpty()) {
                    config.setUsername(redisUsername);
                }
            }
        } else {
            // Use individual properties
            config.setHostName(redisHost);
            config.setPort(redisPort);
            if (redisPassword != null && !redisPassword.isEmpty()) {
                config.setPassword(redisPassword);
            }
            if (redisUsername != null && !redisUsername.isEmpty()) {
                config.setUsername(redisUsername);
            }
        }

        return new LettuceConnectionFactory(config);
    }

    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()
                        )
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()
                        )
                )
                .disableCachingNullValues();
        
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig)
                .build();
    }


}
