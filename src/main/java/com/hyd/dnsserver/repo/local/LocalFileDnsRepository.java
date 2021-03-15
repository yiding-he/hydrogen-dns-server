package com.hyd.dnsserver.repo.local;

import com.hyd.dnsserver.repo.DnsRepository;
import io.netty.handler.codec.dns.DnsRecordType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class LocalFileDnsRepository implements DnsRepository {

    public static final DnsRecordType[] SUPPORTED_TYPES = {
        DnsRecordType.A, DnsRecordType.AAAA
    };

    private final LocalFileDnsConfiguration configuration;

    private final Timer fileWatcherTimer = new Timer();

    private Map<DnsRecordType, Long> fileLastModified = new HashMap<>();

    private Map<DnsRecordType, List<CSVRecord>> recordsMap = new HashMap<>();

    public LocalFileDnsRepository(LocalFileDnsConfiguration configuration) throws IOException {
        this.configuration = configuration;
        this.startFileWatcher();
    }

    private void startFileWatcher() throws IOException {
        Path folderPath = Paths.get(this.configuration.getConfigFolderPath());
        if (!Files.exists(folderPath)) {
            throw new FileNotFoundException("Folder '" +
                this.configuration.getConfigFolderPath() + "' not found");
        }
        if (!Files.isDirectory(folderPath)) {
            throw new IOException("Folder '" +
                this.configuration.getConfigFolderPath() + "' is not a directory");
        }

        this.fileWatcherTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkFiles(folderPath);
                } catch (IOException e) {
                    log.error("Error checking config", e);
                }
            }
        }, 5000, 5000);
    }

    private void checkFiles(Path folderPath) throws IOException {
        for (DnsRecordType type : SUPPORTED_TYPES) {
            Path child = folderPath.resolve(type.name() + ".csv");

            if (!Files.exists(child)) {
                return;
            }

            if (Files.isDirectory(child)) {
                log.warn("Path '{}' is not a file.", child.toString());
                return;
            }

            long lastModified = Files.getLastModifiedTime(child).toMillis();
            if (!fileLastModified.containsKey(type)) {
                fileLastModified.put(type, lastModified);
            } else if (lastModified <= fileLastModified.get(type)) {
                return;
            }

            try {
                checkFile(type, child);
            } catch (IOException e) {
                log.error("Error reading file", e);
            }
        }
    }

    private void checkFile(DnsRecordType type, Path child) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(child)) {
            CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);
            this.recordsMap.put(type, parser.getRecords());
        }
    }

    @Override
    public List<String> lookup(DnsRecordType type, String name) {
        if (!this.recordsMap.containsKey(type)) {
            return Collections.emptyList();
        }

        return this.recordsMap.get(type).stream()
            .filter(record -> record.get("host").trim().equals(name))
            .map(record -> record.get("ip").trim())
            .collect(Collectors.toList());
    }
}
