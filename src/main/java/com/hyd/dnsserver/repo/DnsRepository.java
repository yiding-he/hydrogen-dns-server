package com.hyd.dnsserver.repo;

import io.netty.handler.codec.dns.DnsRecordType;

import java.io.IOException;
import java.util.List;

public interface DnsRepository {

    List<String> lookup(DnsRecordType type, String name) throws IOException;

    void invalidate(DnsRecordType type);

    void put(DnsRecordType type, String host, List<String> ip);

    void persist();

    void removeByHost(DnsRecordType type, String host);
}
