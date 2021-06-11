package com.hyd.dnsserver.admin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "dns.admin")
public class AdminConfiguration {

    private Map<String, String> users;
}
