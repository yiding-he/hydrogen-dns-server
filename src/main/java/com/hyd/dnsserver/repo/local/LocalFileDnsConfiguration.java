package com.hyd.dnsserver.repo.local;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dns.local")
public class LocalFileDnsConfiguration {

    private String configFolderPath;
}
