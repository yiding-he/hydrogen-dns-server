package com.hyd.dnsserver.data;

import lombok.Data;

import java.util.List;

@Data
public class DnsRecord {

    private String host;

    private List<String> ipList;
}
