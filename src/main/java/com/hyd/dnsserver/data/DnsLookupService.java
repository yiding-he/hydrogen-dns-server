package com.hyd.dnsserver.data;

import io.netty.handler.codec.dns.DnsRecordType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.List;

@Service
@Slf4j
public class DnsLookupService {

    public List<String> lookup(DnsRecordType type, String name) throws UnknownHostException {
        return SystemDnsLookup.lookup(type, name);
    }
}
