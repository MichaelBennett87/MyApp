package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.repositories.model.PetRecord;
import com.kenzie.appserver.repositories.model.UserRecord;

import java.util.concurrent.TimeUnit;

public class CacheStore {
    private final Cache<String, UserRecord> userCache;
    private final Cache<String, PetRecord> petCache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // initialize the caches
        this.userCache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();

        this.petCache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public UserRecord getUser(String key) {
        return userCache.getIfPresent(key);
    }

    public void evictUser(String key) {
        userCache.invalidate(key);
    }

    public void addUser(String key, UserRecord value) {
        userCache.put(key, value);
    }

    public PetRecord getPet(String key) {
        return petCache.getIfPresent(key);
    }

    public void evictPet(String key) {
        petCache.invalidate(key);
    }

    public void addPet(String key, PetRecord value) {
        petCache.put(key, value);
    }


    public void updatePetStats(String petId, PetRecord petRecord) {
        petCache.put(petId, petRecord);
    }

    public void removeUser(String email) {
        // Remove the user from the userCache
        UserRecord removedUser = userCache.asMap().remove(email);
    }
}
