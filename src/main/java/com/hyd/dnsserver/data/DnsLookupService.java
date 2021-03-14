package com.hyd.dnsserver.data;

import com.hyd.dnsserver.repo.DnsRepository;
import io.netty.handler.codec.dns.DnsRecordType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class DnsLookupService {

    @Autowired
    private DnsRepository dnsRepository;

    public List<String> lookup(DnsRecordType type, String name) throws IOException {

        List<String> lookupResult = dnsRepository.lookup(type, name);
        if (lookupResult.isEmpty()) {
            lookupResult = SystemDnsLookup.lookup(type, name);
        }

        return lookupResult;
    }
}
