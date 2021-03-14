package com.hyd.dnsserver.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DnsLookupService {

    public String lookup(String name) {
        String answer = "11.22.33.44";
        log.info("查询域名 {} 的 IP 地址，返回 {}", name, answer);
        return answer;
    }
}
