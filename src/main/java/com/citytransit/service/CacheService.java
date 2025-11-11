package com.citytransit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "userData", key = "#userId")
    public Object getUserData(Long userId) {
        // Este método será cacheado automáticamente por Spring
        return null;
    }

    @CacheEvict(value = "userData", key = "#userId")
    public void evictUserCache(Long userId) {
        // Limpia el caché para un usuario específico
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
