package com.hyd.dnsserver.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hyd.dnsserver.repo.DnsRepository;
import io.netty.handler.codec.dns.DnsRecordType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DnsLookupService {

    @Autowired
    private DnsRepository dnsRepository;

    private final Cache<String, List<String>> dnsResultCache = Caffeine.newBuilder()
        .maximumSize(1000).expireAfterWrite(Duration.ofMinutes(1)).build();

    public List<String> lookup(DnsRecordType type, String name) {
        return dnsResultCache.get(type.name() + ":" + name, __ -> {
            List<String> lookupResult = Collections.emptyList();
            try {
                lookupResult = dnsRepository.lookup(type, name);
                if (lookupResult.isEmpty()) {
                    log.info("Repository record not found, name='{}', type={}", name, type);
                    lookupResult = SystemDnsLookup.lookup(type, name);
                }
            } catch (java.net.UnknownHostException e) {
                log.error("java.net.UnknownHostException: {}", e.toString());
            } catch (Exception e) {
                log.error("", e);
            }
            return lookupResult;
        });
    }
}
