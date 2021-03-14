package com.hyd.dnsserver.repo.local;

import com.hyd.dnsserver.repo.DnsRepository;
import io.netty.handler.codec.dns.DnsRecordType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LocalFileDnsRepository implements DnsRepository {

    private final LocalFileDnsConfiguration configuration;

    public LocalFileDnsRepository(LocalFileDnsConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public List<String> lookup(DnsRecordType type, String name) throws IOException {
        return Collections.emptyList();
    }
}
