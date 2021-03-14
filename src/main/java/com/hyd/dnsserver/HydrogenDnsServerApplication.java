package com.hyd.dnsserver;

import com.hyd.dnsserver.data.DnsLookupService;
import com.hyd.dnsserver.netty.DNSServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HydrogenDnsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydrogenDnsServerApplication.class, args);
    }

    @Autowired
    private DnsLookupService dnsLookupService;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            DNSServer dnsServer = new DNSServer(dnsLookupService);
            dnsServer.start();
        };
    }
}
