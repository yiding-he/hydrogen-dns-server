package com.hyd.dnsserver.service;

import io.netty.handler.codec.dns.DnsRecordType;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 从操作系统获取 DNS 解析结果，当本地没有解析结果时使用
 */
@Slf4j
public class SystemDnsLookup {

    public static List<String> lookup(DnsRecordType type, String name) throws UnknownHostException {
        List<String> result = new ArrayList<>();
        InetAddress[] addresses = null;

        if (type == DnsRecordType.A) {
            addresses = Inet4Address.getAllByName(name);
        } else if (type == DnsRecordType.AAAA) {
            addresses = Inet6Address.getAllByName(name);
        }

        if (addresses != null) {
            for (InetAddress address : addresses) {
                if (address instanceof Inet4Address && type == DnsRecordType.A) {
                    result.add(address.getHostAddress());
                } else if (address instanceof Inet6Address && type == DnsRecordType.AAAA) {
                    result.add(address.getHostAddress());
                }
            }
        }

        log.info("System DNS lookup, name='{}', type={}, result={}", name, type.name(), result);
        return result;
    }

}
