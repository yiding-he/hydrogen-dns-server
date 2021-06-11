package com.hyd.dnsserver.repo.local;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dns.local")
public class LocalFileDnsConfiguration {

    /**
     * CSV 文件所在目录，相对或绝对路径皆可
     */
    private String configFolderPath;

    /**
     * 检查文件修改定时任务执行间隔（秒）
     */
    private int fileWatcherIntervalSec = 5;
}
