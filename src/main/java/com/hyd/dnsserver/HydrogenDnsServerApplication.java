package com.hyd.dnsserver;

import com.hyd.dnsserver.data.DnsLookupService;
import com.hyd.dnsserver.netty.DNSServer;
import com.hyd.dnsserver.repo.DnsRepository;
import com.hyd.dnsserver.repo.local.LocalFileDnsConfiguration;
import com.hyd.dnsserver.repo.local.LocalFileDnsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({
    LocalFileDnsConfiguration.class
})
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

    @Bean
    DnsRepository dnsRepository(LocalFileDnsConfiguration conf) throws Exception {
        return new LocalFileDnsRepository(conf);
    }
}
